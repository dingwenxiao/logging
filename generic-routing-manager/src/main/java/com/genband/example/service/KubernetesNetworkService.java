package com.genband.example.service;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import io.fabric8.kubernetes.api.model.EndpointAddress;
import io.fabric8.kubernetes.api.model.EndpointPort;
import io.fabric8.kubernetes.api.model.EndpointSubset;
import io.fabric8.kubernetes.api.model.Endpoints;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientException;
import io.fabric8.kubernetes.client.Watcher;

@Service
public class KubernetesNetworkService {
  // java -jar xxx.jar --kubernetes.master_url=xxx
  @Value("${kubernetes.master.url:http://172.28.250.4:8080/}")
  private String kubernetes_master_url;

  private Object fetch_lock = new Object();
  private Logger logger = LoggerFactory.getLogger(KubernetesNetworkService.class);

  private List<String> currentEndPointsAddress = Lists.newArrayList();
  private Map<String, String> labelsMap = Maps.newHashMap();
  private Map<String, String> userServiceAddressMap = Maps.newHashMap();

  private Config config;
  private KubernetesClient client;

  @PostConstruct
  public void init() {
    initKubernetesLabelsFromEnv();

    initKubernetesClient();

    fetchKubernetesEndpointsAddress();

    initKubernetesLabelsWatcher();
  }

  @PreDestroy
  public void close() {
    client.close();
  }

  public void fetchKubernetesEndpointsAddress() {
    List<Endpoints> items = client.endpoints().withLabel("tier", "business_logic")
        .withLabels(labelsMap).list().getItems();
    // for windows test
    // List<Endpoints> items =
    // client.endpoints().withLabel("layer", "protocol").withLabel("type", "xmpp")
    // .withLabel("service", "xmpp_cmanager").withLabel("version", "v1.0.0.1")
    // .withLabel("tier", "business_logic").withLabel("site", "local").list().getItems();
    // List<Endpoints> items =
    // client.endpoints().withLabel("tier", "business_logic").list().getItems();
    // so far the result size should be 1
    if (items != null && items.size() == 1) {
      currentEndPointsAddress = processEndpointsAddress(items.get(0));
    } else {
      logger.warn("More than one service endpoints or it's null");
    }

    logger.info("Finished loading networks info: " + currentEndPointsAddress);
  }

  public List<String> getCurrentEndPointsAddress() {
    return currentEndPointsAddress;
  }

  public Map<String, String> getUserServiceAddressMap() {
    synchronized (fetch_lock) {
      return userServiceAddressMap;
    }
  }

  private void initKubernetesClient() {
    logger.info("kubernetes master url: " + kubernetes_master_url);
    config = new ConfigBuilder().withMasterUrl(kubernetes_master_url).build();
    client = new DefaultKubernetesClient(config);
  }

  // TODO make label name as static variable
  private void initKubernetesLabelsFromEnv() {
    Map<String, String> env = System.getenv();
    if (env.containsKey("layer")) {
      labelsMap.put("layer", env.get("layer"));
      logger.info("fetch kubernetes label: layer=" + env.get("layer"));
    }
    if (env.containsKey("type")) {
      labelsMap.put("type", env.get("type"));
      logger.info("fetch kubernetes label: type=" + env.get("type"));
    }
    if (env.containsKey("service")) {
      labelsMap.put("service", env.get("service"));
      logger.info("fetch kubernetes label: service=" + env.get("service"));
    }
    if (env.containsKey("version")) {
      labelsMap.put("version", env.get("version"));
      logger.info("fetch kubernetes label: version=" + env.get("version"));
    }
    if (env.containsKey("tier")) {
      labelsMap.put("tier", env.get("tier"));
      logger.info("fetch kubernetes label: tier=" + env.get("tier"));
    }
    if (env.containsKey("site")) {
      labelsMap.put("site", env.get("site"));
      logger.info("fetch kubernetes label: site=" + env.get("site"));
    }
  }

  private void initKubernetesLabelsWatcher() {
    Watcher<Endpoints> watcher = new EndpointWatcher();
    client.endpoints().withLabel("tier", "business_logic").withLabels(labelsMap).watch(watcher);
  }

  private List<String> processEndpointsAddress(Endpoints endpoints) {
    List<String> endPointsAddress = Lists.newArrayList();

    for (EndpointSubset subset : endpoints.getSubsets()) {
      for (EndpointAddress address : subset.getAddresses()) {
        for (EndpointPort port : subset.getPorts()) {
          endPointsAddress.add(address.getIp() + ":" + port.getPort());
        }
      }
    }

    return endPointsAddress;
  }

  private class EndpointWatcher implements Watcher<Endpoints> {

    @Override
    public void eventReceived(io.fabric8.kubernetes.client.Watcher.Action action,
        Endpoints endpoints) {
      logger.debug("Get Endpoints Action:   " + action.toString() + " \n " + endpoints.toString());
      synchronized (fetch_lock) {
        if (action == Action.MODIFIED) {
          // if there are three instances closed, not sure the modified message
          // will be sent one by one, or two in one and one in one, or three in one.
          processModifiedAction(endpoints);
        } else if (action == Action.ADDED) {
          processAddedAction(endpoints);
        } else if (action == Action.DELETED) {
          processDeletedFunction();
        }
      }

    }

    private void processDeletedFunction() {
      // clean service end points
      currentEndPointsAddress.clear();
      // kick out all users
      userServiceAddressMap.clear();
      logger.info("Cleaning address related info...");
      logger.info("address size: " + currentEndPointsAddress.size() + " map size: "
          + userServiceAddressMap.size());
    }

    private void processAddedAction(Endpoints endpoints) {
      assert (currentEndPointsAddress.size() == 0);
      currentEndPointsAddress = processEndpointsAddress(endpoints);
      logger.info("current ip address list: " + currentEndPointsAddress);
    }

    private void processModifiedAction(Endpoints endpoints) {
      List<String> newEndPointsAddress = processEndpointsAddress(endpoints);
      logger.info("watched new ip address list: " + newEndPointsAddress);
      // get those addresses which are not in the new list
      if (currentEndPointsAddress.removeAll(newEndPointsAddress)) {
        // clean the user hash map
        for (String removedAddress : currentEndPointsAddress) {
          Iterator<Entry<String, String>> it = userServiceAddressMap.entrySet().iterator();
          while (it.hasNext()) {
            Map.Entry<String, String> pair = (Map.Entry<String, String>) it.next();
            String username = pair.getKey();
            String address = pair.getValue();
            if (address.equals(removedAddress)) {
              it.remove();
              logger.info(
                  username + " was on " + removedAddress + " who will be treated as a new user");
            }
          }
        }
      } else {
        // the new address is empty and the current address list must be one
        if (newEndPointsAddress.size() == 0) {
          logger.info("the new ip address is empty and the map will be cleaned");
          if (userServiceAddressMap.size() != 0) {
            userServiceAddressMap.clear();
          }
        } else {
          // the new address is one and the current address list must be empty
          logger.info("the current address is empty: " + currentEndPointsAddress);
        }
      }

      currentEndPointsAddress = newEndPointsAddress;
      logger.info("current ip address list: " + currentEndPointsAddress);
    }

    @Override
    public void onClose(KubernetesClientException cause) {
      logger.info("Exception close.");
    }
  }
}
