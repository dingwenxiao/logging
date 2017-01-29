package com.genband.util.log;

import java.util.List;

import org.apache.logging.log4j.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.genband.util.k8s.EndPointsWatcherCallback;
import com.genband.util.k8s.KubernetesNetworkService;

/**
 * This file should be removed after Dingwen checked.
 * 
 * @author hakuang
 *
 */
public class SampleCode {
  private static Logger logger = LoggerFactory.getLogger(SampleCode.class.getName());

  public static void main(String[] args) {
    logger.info("hello");

    /**
     * move these into test cases which can be used as example as well.
     */
    LogConfigurationUtil.addKafkaAppender(getKakfaAddress(), "kafka-info", "info", Level.INFO,
        Level.WARN);
    LogConfigurationUtil.addKafkaAppender(getKakfaAddress(), "kafka-debug", "debug", Level.DEBUG,
        Level.INFO);
    LogConfigurationUtil.addKafkaAppender(getKakfaAddress(), "kafka-trace", "trace", Level.TRACE,
        Level.DEBUG);
    LogConfigurationUtil.addKafkaAppender(getKakfaAddress(), "kafka-error", "error", Level.ERROR,
        Level.FATAL);
    LogConfigurationUtil.addKafkaAppender(getKakfaAddress(), "kafka-warn", "warn", Level.WARN,
        Level.ERROR);

    logger.info("I am test hello");
    logger.debug("i am test debug");
    logger.trace("I am test trace");
    logger.warn("I am a test warn");
    logger.error("I am a test error");
  }

  private static String getKakfaAddress() {
    KubernetesNetworkService kubernetesNetworkService =
        KubernetesNetworkService.getInstance(new EndPointsWatcherCallback() {
          @Override
          public void sendAddressList(List<String> addressList) {
            logger.info(addressList.toString());
          }
        });

    List<String> fetchKafkaAddress = kubernetesNetworkService.fetchKafkaAddress();
    if (fetchKafkaAddress != null) {
      logger.info(fetchKafkaAddress.toString());
    } else {
      logger.info("Kafka adderss is empty");
    }

    // the kafka address is not ready yet.
    return "172.28.247.239:9092";
  }
}
