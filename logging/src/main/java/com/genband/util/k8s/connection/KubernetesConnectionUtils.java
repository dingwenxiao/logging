package com.genband.util.k8s.connection;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.fabric8.kubernetes.api.model.EndpointsList;
import io.fabric8.kubernetes.client.KubernetesClient;

public class KubernetesConnectionUtils {

  private final static Logger logger = LoggerFactory.getLogger(KubernetesConnectionUtils.class);
  
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
