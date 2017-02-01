package com.genband.util.log;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.genband.util.k8s.KubernetesNetworkService;

import io.fabric8.kubernetes.client.KubernetesClientException;
import io.fabric8.kubernetes.client.Watch;

public class KubernetesMonitorThread implements Runnable {
  final CountDownLatch closeLatch = new CountDownLatch(1);
  private static final Logger logger = Logger.getLogger(KubernetesMonitorThread.class);

  KubernetesNetworkService kubernetesNetworkService;

  public KubernetesMonitorThread(KubernetesNetworkService kubernetesNetworkService) {
    this.kubernetesNetworkService = kubernetesNetworkService;
  }

  @Override
  public void run() {
    try (Watch watch = kubernetesNetworkService.getEndPointsWatcher()) {
      closeLatch.await(10, TimeUnit.SECONDS);
    } catch (KubernetesClientException | InterruptedException e) {
      logger.error("Could not watch resources", e);
    }
    try {
      Thread.sleep(60000l);
    } catch (InterruptedException e) {
      logger.error(e);
    }
  }

}
