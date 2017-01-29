package com.genband.example.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ReflectionUtils;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

//@Component
public class SimplerErrorFilter extends ZuulFilter {

  private static final Logger logger = LoggerFactory.getLogger(SimplerErrorFilter.class);

  @Override
  public String filterType() {
    return "post";
  }

  @Override
  public int filterOrder() {
    return -1; // Needs to run before SendErrorFilter which has filterOrder == 0
  }

  @Override
  public boolean shouldFilter() {
    return RequestContext.getCurrentContext().containsKey("error.status_code");
  }

  @Override
  public Object run() {
    try {
      RequestContext ctx = RequestContext.getCurrentContext();
      Object e = ctx.get("error.exception");

      if (e != null && e instanceof ZuulException) {
        ZuulException zuulException = (ZuulException) e;
        logger.error("Zuul failure detected: " + zuulException.getMessage(), "");

        // Remove error code to prevent sendErrorFilter handling in follow up filters
        ctx.remove("error.status_code");

        // Populate context with new response values
        ctx.setResponseBody(zuulException.getMessage());
        ctx.getResponse().setContentType("text");
        ctx.setResponseStatusCode(404); // Can set any error code as excepted
      }
    } catch (Exception ex) {
      logger.error("Exception filtering in custom error filter", ex);
      ReflectionUtils.rethrowRuntimeException(ex);
    }

    return null;
  }
}
