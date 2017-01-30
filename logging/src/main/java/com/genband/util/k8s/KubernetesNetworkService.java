package com.genband.util.k8s;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.genband.util.k8s.config.ConfigManager;
import com.genband.util.k8s.config.KafkaConfigManager;

import io.fabric8.kubernetes.api.model.EndpointAddress;
import io.fabric8.kubernetes.api.model.EndpointPort;
import io.fabric8.kubernetes.api.model.EndpointSubset;
import io.fabric8.kubernetes.api.model.Endpoints;
import io.fabric8.kubernetes.api.model.EndpointsList;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientException;
import io.fabric8.kubernetes.client.Watcher;

public enum KubernetesNetworkService {

  SERVICE_INSTANCE;
  private static String KUBERNETES_MASTER_URL_KEY = "kubernetesMasterUrl";
  private final static String kubernetesMasterTestUrl = "http://172.28.250.4:8080/";

  private Logger logger = LoggerFactory.getLogger(KubernetesNetworkService.class);

  private String kubernetesMasterUrl;
  private Config config;
  private KubernetesClient client;
  private List<String> addressList;
  private EndPointsWatcherCallback endPointsWatcherCallback;
  private ConfigManager configManager = null;

  public static KubernetesNetworkService getInstance(EndPointsWatcherCallback callback) {
    return SERVICE_INSTANCE;
  }

  private void build(SingletonBuilder builder) {
    this.kubernetesMasterUrl =
        (builder.kubernetesMasterUrl == null || builder.kubernetesMasterUrl.isEmpty())
            ? getMasterUrlFromSystemVariable() : builder.kubernetesMasterUrl;
    this.endPointsWatcherCallback = builder.endPointsWatcherCallback;
    this.configManager = builder.configManager;

    if (kubernetesMasterUrl != null) {
      config = new ConfigBuilder().withMasterUrl(kubernetesMasterUrl).build();
      client = new DefaultKubernetesClient(this.config);
      addressList = new ArrayList<>();
    }
  }

  public List<String> getEndPointsAddressFromConfigMap() {
    return getEndpointsAddressByLabel(configManager.getLabelMap());
  }

  private List<String> getEndPointsAddressList(Endpoints endpoints) {
    /**
     * currently we don't have kafka server ready, so we use another label which has return value to
     * test it, which should be replaced by kafka label like "service":"kafka"
     * 
     * @author hakuang
     * @date 2017/1/25
     */
    // currently only have one kafka entry address.
    List<String> endPointAddressList = new ArrayList<>();
    if (endpoints != null) {
      for (EndpointSubset subset : endpoints.getSubsets()) {
        EndpointAddress address = subset.getAddresses().iterator().next();
        EndpointPort port = subset.getPorts().iterator().next();

        // instances.add(new DefaultServiceInstance(name, address.getIp(), port.getPort(), false,
        // endpoints.getMetadata().getLabels()));
        endPointAddressList.add(address.getIp() + ":" + port.getPort());
      }
    }
    return endPointAddressList;
  }

  /**
   * start to watch those endpoints with same specified label (in a same service)
   * 
   */
  public void startEndPointsWatcher() {
    if (client != null && !configManager.getLabelMap().isEmpty()) {
      client.endpoints().withLabels(configManager.getLabelMap()).watch(new EndpointWatcher());
    }
  }

  public void setEndPointsWatcherCallback(EndPointsWatcherCallback endPointsWatcherCallback) {
    this.endPointsWatcherCallback = endPointsWatcherCallback;
  }

  public void close() {
    client.close();
  }

  /**
   * The logic of fetching endpoints might be changed in the new k8s environment. Currently, it only
   * consider one item.
   * 
   * @param labelsMap
   * @return
   * @author hakuang
   * @date 2017/01/25
   */
  public List<String> getEndpointsAddressByLabel(HashMap<String, String> labelsMap) {
    List<String> resAddressList = new ArrayList<>();
    EndpointsList endpointsList = client.endpoints().withLabels(labelsMap).list();
    for (Endpoints endPoints : endpointsList.getItems()) {
      resAddressList.addAll(getEndPointsAddressList(endPoints));
    }
    return resAddressList;
  }

  /**
   * get kubernetes url from system variable which will be set in docker file. sometimes, this
   * doesn't work under windows system. Dont't know the reason yet.
   * 
   * @return
   */
  private String getMasterUrlFromSystemVariable() {
    return kubernetesMasterUrl = System.getenv().get(KUBERNETES_MASTER_URL_KEY);
  }

  private class EndpointWatcher implements Watcher<Endpoints> {

    @Override
    public void eventReceived(io.fabric8.kubernetes.client.Watcher.Action action,
        Endpoints endpoints) {
      if (action == Action.MODIFIED) {
        processModifiedAction(endpoints);
      } else if (action == Action.ADDED) {
        processAddedAction(endpoints);
      } else if (action == Action.DELETED) {
        processDeletedFunction(endpoints);
      }
      if (endPointsWatcherCallback != null && addressList != null) {
        endPointsWatcherCallback.sendAddressList(addressList);
      }
    }

    private void processDeletedFunction(Endpoints endpoints) {
      logger.debug("Deleting event on endpoints");
      addressList = getEndPointsAddressList(endpoints);;
    }

    private void processAddedAction(Endpoints endpoints) {
      addressList = getEndPointsAddressList(endpoints);
      logger.info("Adding event on endpoints ", addressList);
    }

    private void processModifiedAction(Endpoints endpoints) {
      addressList = getEndPointsAddressList(endpoints);
      logger.info("Modifying event on endpoints", addressList);
    }

    @Override
    public void onClose(KubernetesClientException cause) {
      logger.debug("kafka watch closed");
    }
  }

  public static class SingletonBuilder {

    private String kubernetesMasterUrl; // Mandatory
    private ConfigManager configManager = null; // Mandatory
    private EndPointsWatcherCallback endPointsWatcherCallback = null;

    public SingletonBuilder(ConfigManager configManager) {
      this.kubernetesMasterUrl = configManager.getKubernetesMasterUrl();
      this.configManager = configManager;
    }

    public SingletonBuilder endPointsWatcherCallback(
        EndPointsWatcherCallback endPointsWatcherCallback) {
      this.endPointsWatcherCallback = endPointsWatcherCallback;
      return this;
    }

    public void build() {
      KubernetesNetworkService.SERVICE_INSTANCE.build(this);
    }
  }
}
