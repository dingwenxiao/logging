package org.slf4j.impl;

import java.util.List;
import java.util.Locale;

import org.apache.logging.log4j.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.genband.util.k8s.KubernetesNetworkService;
import com.genband.util.k8s.config.ConfigManager;
import com.genband.util.k8s.config.KafkaConfigManager;

/**
 * This file should be removed after Dingwen checked.
 * 
 * @author hakuang
 *
 */
public class SampleCode {
  private static Logger logger = LoggerFactory.getLogger(SampleCode.class.toString());

  public static void main(String[] args) {
    logger.info("sdf");
    
    org.apache.logging.log4j.LogManager.getLogger("sdfd").info("test");
    
//    logger.info(new I18nResource() {
//      @Override
//      public String text(Object... arguments) {
//        return "sdf";
//      }
//
//      @Override
//      public String text(Locale locale, Object... arguments) {
//        return "sdf";
//      }
//    }, "ss");
    // ConfigManager configManager = new KafkaConfigManager("config.properties");
    // /**
    // * move these into test cases which can be used as example as well.
    // */
    // LogConfigurationUtil.addKafkaAppender(configManager, getKakfaAddress(), "kafka-info", "info",
    // Level.INFO, Level.WARN);
    // LogConfigurationUtil.addKafkaAppender(configManager, getKakfaAddress(), "kafka-debug",
    // "debug",
    // Level.DEBUG, Level.INFO);
    // LogConfigurationUtil.addKafkaAppender(configManager, getKakfaAddress(), "kafka-trace",
    // "trace",
    // Level.TRACE, Level.DEBUG);
    // LogConfigurationUtil.addKafkaAppender(configManager, getKakfaAddress(), "kafka-error",
    // "error",
    // Level.ERROR, Level.FATAL);
    // LogConfigurationUtil.addKafkaAppender(configManager, getKakfaAddress(), "kafka-warn", "warn",
    // Level.WARN, Level.ERROR);

    // logger.info("I am test hello");
    // logger.debug("i am test debug");
    // logger.trace("I am test trace");
    // logger.warn("I am a test warn");
    // logger.error("I am a test error");
  }

  private static String getKakfaAddress() {

    new KubernetesNetworkService.SingletonBuilder(new KafkaConfigManager("config.properties"))
        .build();
    KubernetesNetworkService kubernetesNetworkService = KubernetesNetworkService.getInstance();

    List<String> fetchKafkaAddress = kubernetesNetworkService.getEndPointsAddressFromConfigMap();

    if (fetchKafkaAddress != null) {
      //logger.info(LogString.getInstance("sdf"));
    } else {
     // logger.info(LogString.getInstance("Kafka adderss is empty"));
    }

    // the kafka address is not ready yet.
    return "172.28.247.239:9092";
  }
}
