package com.genband.util.k8s;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.genband.util.k8s.config.ConfigManager;
import com.genband.util.k8s.connection.KubernetesConnectionUtils;

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

public class KubernetesNetworkServiceClass {

  //private static KubernetesNetworkServiceClass INSTANCE;

  private static String KUBERNETES_MASTER_URL_KEY = "kubernetesMasterUrl";
  private final static String kubernetesMasterTestUrl = "http://172.28.250.4:8080/";
  private final static int CONNECTION_RETRY_NUM = 3;// the maximum times of retry connection

  private static Logger logger = Logger.getLogger(KubernetesNetworkServiceClass.class);

  private String kubernetesMasterUrl;
  private static Config config;
  private KubernetesClient client;
  private List<String> addressList;// pods address list
  private EndPointsWatcherCallback endPointsWatcherCallback;
  private ConfigManager configManager = null;

  /**
   * Get instance of KubernetesNetworkService
   * 
   * @return
   */
  public KubernetesNetworkServiceClass(ConfigManager configManager) {
    this.configManager = configManager;
    this.kubernetesMasterUrl = (configManager.getKubernetesMasterUrl() == null
        || configManager.getKubernetesMasterUrl().isEmpty()) ? getMasterUrlFromSystemVariable()
            : configManager.getKubernetesMasterUrl();

    if (kubernetesMasterUrl != null) {
      config = new ConfigBuilder().withMasterUrl(kubernetesMasterUrl).build();
   //   client = new DefaultKubernetesClient(this.config);
      addressList = new ArrayList<>();
    }
  }

//  public static synchronized KubernetesNetworkServiceClass getInstance(
//      ConfigManager configManager) {
//    if (INSTANCE == null) {
//      INSTANCE = new KubernetesNetworkServiceClass(configManager);
//    }
//    return INSTANCE;
//
//  }

  /**
   * Get all address of endpoints with same label in config map
   * 
   * @return endpoints address list
   */
  public List<String> getEndPointsAddressFromConfigMap() {
    return getEndpointsAddressByLabel(configManager.getLabelMap());
  }

  /**
   * Get all address from endpoints
   * 
   * @return endpoints address list
   */
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
    EndpointsList endpointsList =
        KubernetesConnectionUtils.getEndPointsListBylabels(client, labelsMap, CONNECTION_RETRY_NUM);

    if (endpointsList != null) {
      for (Endpoints endPoints : endpointsList.getItems()) {
        resAddressList.addAll(getEndPointsAddressList(endPoints));
      }
    }
    return resAddressList;
  }

  /**
   * Get kubernetes url from system variable which will be set in docker file. sometimes, this
   * doesn't work under windows system. Dont't know the reason yet.
   * 
   * @return kubernetes Master Url
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
      logger.info("Adding event on endpoints :" + addressList);
    }

    private void processModifiedAction(Endpoints endpoints) {
      addressList = getEndPointsAddressList(endpoints);
      logger.info("Modifying event on endpoints :" + addressList);
    }

    @Override
    public void onClose(KubernetesClientException cause) {
      logger.info("Watch closed");
    }
  }

}
