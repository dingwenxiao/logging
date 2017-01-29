package com.genband.example.filter;

import java.net.MalformedURLException;
import java.net.URL;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.genband.example.exception.KubernetesServicesNotAvailableException;
import com.genband.example.service.KubernetesNetworkService;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

@Component
public class SimpleRequestFilter extends ZuulFilter {
  private static int new_user_index = 0;

  private Logger logger = LoggerFactory.getLogger(SimpleRequestFilter.class);

  @Autowired
  private KubernetesNetworkService kubernetesNetworkService;

  /**
   * Indicates which type of Zuul filter this is. In our case, the "route" value is returned to
   * indicate to the Zuul server that this is a route filter.
   * 
   * @return "route"
   */
  @Override
  public String filterType() {
    return "route"; // it could be pre if we only need to change the request content.
  }

  @Override
  public int filterOrder() {
    return 0;
  }

  @PostConstruct
  public void init() {
    logger.info("finish construct simple route filter");
  }

  /**
   * Indicates if a request should be filtered or not. In our case, all requests are taken into
   * account (always return true).
   *
   * @return true
   */
  public boolean shouldFilter() {
    return true;
  }

  /**
   * The filter execution
   *
   * @return
   */
  public Object run() {

    // logging the interception of the query
    logger.info("query interception");

    // retrieving the Zuul request context
    RequestContext ctx = RequestContext.getCurrentContext();
    try {
      // if the requested url is authorized, the route host is set to the requested one
      if (isAuthorizedRequest(ctx.getRequest())) {
        setRouteHost(ctx);
        // ctx.setRouteHost(new URL("http://172.28.19.60:8080"));
      } else {
        // TODO if the requested URL is not authorized, the route host is set to the urlRedirect
        // value the client will be redirected to the new host
      }
    } catch (MalformedURLException e) {
      logger.error("", e);
    } catch (KubernetesServicesNotAvailableException e) {
      logger.error(e.getMessage(), "");
    }
    return null;
  }

  /**
   * Indicates if the provided request is authorized or not.
   *
   * @param request the provided request
   *
   * @return true if the provided request is authorized, false otherwise
   */
  private boolean isAuthorizedRequest(HttpServletRequest request) {
    // apply your filter here
    return true;
  }

  /**
   * This method allows to set the route host into the Zuul request context provided as parameter.
   * The url is extracted from the orginal request and the host is extracted from it.
   *
   * @param ctx the provided Zuul request context
   *
   * @throws MalformedURLException
   */
  private void setRouteHost(RequestContext ctx)
      throws MalformedURLException, KubernetesServicesNotAvailableException {
    logger.info("get into set route host");
    String urlStr = ctx.getRequest().getRequestURL().toString();
    URL url = new URL(urlStr);

    String userNameFromURI = getUserNameFromURI(url.getPath());

    if (userNameFromURI == null) {
      throw new MalformedURLException("url is not parsable");
    }

    logger.info("username: " + userNameFromURI);
    String addressWithPort = null;
    int size = kubernetesNetworkService.getCurrentEndPointsAddress().size();
    if (size == 0) {
      throw new KubernetesServicesNotAvailableException(
          "no kubernetes services end points available");
    }

    if (kubernetesNetworkService.getUserServiceAddressMap().get(userNameFromURI) == null) {
      logger.info("new_user_index： " + new_user_index);
      // TODO this might be empty concurrent problem
      addressWithPort = getAddressFromList(userNameFromURI);

    } else {
      addressWithPort = kubernetesNetworkService.getUserServiceAddressMap().get(userNameFromURI);

      // might be triggered
      if (addressWithPort == null) {
        logger.info(
            "move " + userNameFromURI + " to new user group: new_user_index： " + new_user_index);

        addressWithPort = getAddressFromList(userNameFromURI);
      }
    }

    if (size == new_user_index) {
      new_user_index = 0;
      logger.info("reset new_user_index to 0");
    }

    String newUrl = url.getProtocol() + "://" + addressWithPort;
    logger.info("redirect to: " + newUrl);
    ctx.setRouteHost(new URL(newUrl));
  }

  private String getAddressFromList(String userNameFromURI) {
    // this might be null because the update message hasn't come yet.
    String addressWithPort =
        kubernetesNetworkService.getCurrentEndPointsAddress().get(new_user_index++);
    kubernetesNetworkService.getUserServiceAddressMap().put(userNameFromURI, addressWithPort);
    return addressWithPort;
  }

  // Example: http://172.28.247.232:8080/rest/version/1/user/{username}/app/xmpp/im/send
  private String getUserNameFromURI(String url) {
    String[] splits = url.split("/");
    if (splits.length > 6) {
      // if (email_pattern.matcher(splits[5]).matches()) {
      return splits[5];
      // }
    }
    return null;
  }
}
