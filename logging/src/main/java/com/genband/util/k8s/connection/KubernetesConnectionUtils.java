package com.genband.util.k8s.connection;

import java.util.HashMap;

import org.apache.log4j.Logger;

import io.fabric8.kubernetes.api.model.EndpointsList;
import io.fabric8.kubernetes.client.KubernetesClient;

/**
 * Manage k8s connection. This class wrap all the methods 
 * that try to connect k8s, and once the microservice failed to connect the k8s, it will retry connecting n times.
 * @author dixiao
 *
 */
public class KubernetesConnectionUtils {

  private final static Logger logger = Logger.getLogger(KubernetesConnectionUtils.class);
  
  /**
   * 
   * @param client kubernetes client
   * @param labelsMap labels
   * @param retryNum number of retry connecting
   * @return  EndpointsList
   */
  public static EndpointsList getEndPointsListBylabels(KubernetesClient client,
      HashMap<String, String> labelsMap, int retryNum) {

    EndpointsList endPointsList = null;

    while (retryNum > 0 && endPointsList == null) {
      try {
        endPointsList = client.endpoints().withLabels(labelsMap).list();
      } catch (Exception ex) {
        logger.error("failed to connect",ex);
        logger.error("retry connection countdown " + retryNum);
      }
      retryNum--;
    }
    return endPointsList;
  }


}
