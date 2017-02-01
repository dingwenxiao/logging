package com.genband.util.k8s.connection;

import java.util.List;

import org.apache.logging.log4j.Level;

import com.genband.util.k8s.KubernetesNetworkService;
import com.genband.util.k8s.config.ConfigManager;
import com.genband.util.k8s.config.KafkaConfigManager;
import com.genband.util.log.CONSTANT;
import com.genband.util.log.KubernetesMonitorThread;
import com.genband.util.log.LogConfigurationUtil;

public class LoggerOperation {

  public static void LoggerAppenderUpdate() {
    ConfigManager configManager = new KafkaConfigManager(CONSTANT.LOG_CONIG_PATH);
    new KubernetesNetworkService.SingletonBuilder(configManager).build();
    KubernetesNetworkService kubernetesNetworkService = KubernetesNetworkService.getInstance();
    String kafkaAddress = getKakfaAddress(kubernetesNetworkService);
    LogConfigurationUtil.addKafkaAppender(configManager, kafkaAddress, "kafka-info", "info",
        Level.INFO, Level.WARN);
    LogConfigurationUtil.addKafkaAppender(configManager, kafkaAddress, "kafka-debug", "debug",
        Level.DEBUG, Level.INFO);
    LogConfigurationUtil.addKafkaAppender(configManager, kafkaAddress, "kafka-trace", "trace",
        Level.TRACE, Level.DEBUG);
    LogConfigurationUtil.addKafkaAppender(configManager, kafkaAddress, "kafka-error", "error",
        Level.ERROR, Level.FATAL);
    LogConfigurationUtil.addKafkaAppender(configManager, kafkaAddress, "kafka-warn", "warn",
        Level.WARN, Level.ERROR);
  }

  private static String getKakfaAddress(KubernetesNetworkService kubernetesNetworkService) {
    String address = "172.28.247.239:9092";
    List<String> fetchKafkaAddress = kubernetesNetworkService.getEndPointsAddressFromConfigMap();

    if (fetchKafkaAddress != null && !fetchKafkaAddress.isEmpty()) {
      // logger4j.info(fetchKafkaAddress.toString());
      address = fetchKafkaAddress.get(0);
    } else {
      // logger4j.info("Kafka adderss is empty");

    }

    // the kafka address is not ready yet.
    return address;
  }

  public static void startWatch() {
    ConfigManager configManager = new KafkaConfigManager(CONSTANT.LOG_CONIG_PATH);
    new KubernetesNetworkService.SingletonBuilder(configManager).build();
    new Thread(new KubernetesMonitorThread(KubernetesNetworkService.getInstance())).start();
  }

}
