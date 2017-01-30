package org.slf4j.impl;

import java.util.List;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.slf4j.Log4jMarker;
import org.slf4j.Logger;
import org.slf4j.Marker;

import com.genband.util.k8s.KubernetesNetworkService;
import com.genband.util.k8s.KubernetesNetworkServiceClass;
import com.genband.util.k8s.config.ConfigManager;
import com.genband.util.k8s.config.KafkaConfigManager;
import com.genband.util.log.CONSTANT;
import com.genband.util.log.LogConfigurationUtil;

/**
 * Users can customize their logger by implementing following functions
 * 
 * @author dixiao
 *
 */
public class GbLoggerAdapter implements Logger {

  org.apache.logging.log4j.Logger logger4j;

  String name;
  KubernetesNetworkService kubernetesNetworkService;

  public GbLoggerAdapter(String name) {
    logger4j = LogManager.getLogger(name);
    this.name = name;
  }

  public void loggerAppenderInit() {
    ConfigManager configManager = new KafkaConfigManager(CONSTANT.LOG_CONIG_PATH);
    // kubernetesNetworkServiceClass = new KubernetesNetworkServiceClass(configManager);
    new KubernetesNetworkService.SingletonBuilder(new KafkaConfigManager("config.properties"))
        .build();
    kubernetesNetworkService = KubernetesNetworkService.getInstance();
    LogConfigurationUtil.addKafkaAppender(configManager, getKakfaAddress(), "kafka-info", "info",
        Level.INFO, Level.WARN);
    LogConfigurationUtil.addKafkaAppender(configManager, getKakfaAddress(), "kafka-debug", "debug",
        Level.DEBUG, Level.INFO);
    LogConfigurationUtil.addKafkaAppender(configManager, getKakfaAddress(), "kafka-trace", "trace",
        Level.TRACE, Level.DEBUG);
    LogConfigurationUtil.addKafkaAppender(configManager, getKakfaAddress(), "kafka-error", "error",
        Level.ERROR, Level.FATAL);
    LogConfigurationUtil.addKafkaAppender(configManager, getKakfaAddress(), "kafka-warn", "warn",
        Level.WARN, Level.ERROR);
  }

  private String getKakfaAddress() {

    List<String> fetchKafkaAddress = kubernetesNetworkService.getEndPointsAddressFromConfigMap();

    if (fetchKafkaAddress != null) {
      logger4j.info(fetchKafkaAddress.toString());
    } else {
      logger4j.info("Kafka adderss is empty");
    }

    // the kafka address is not ready yet.
    return "172.28.247.239:9092";
  }

  public void debug(String arg0) {
    loggerAppenderInit();
    logger4j.debug(arg0);
  }

  public void debug(String arg0, Object arg1) {
    loggerAppenderInit();
    logger4j.debug(arg0, arg1);
  }

  public void debug(String arg0, Object... arg1) {
    loggerAppenderInit();
    logger4j.debug(arg0, arg1);
  }

  public void debug(String arg0, Throwable arg1) {
    loggerAppenderInit();
    logger4j.debug(arg0, arg1);
  }

  public void debug(Marker arg0, String arg1) {
    loggerAppenderInit();
    logger4j.debug(arg0);
    logger4j.debug(arg1);
  }

  public void debug(String arg0, Object arg1, Object arg2) {
    loggerAppenderInit();
    logger4j.debug(arg0, arg1, arg2);
  }

  public void debug(Marker arg0, String arg1, Object arg2) {
    logger4j.debug(arg0);
    logger4j.debug(arg1, arg2);
    loggerAppenderInit();
  }

  public void debug(Marker arg0, String arg1, Object... arg2) {
    logger4j.debug(arg0);
    logger4j.debug(arg1, arg2);
    loggerAppenderInit();
  }

  public void debug(Marker arg0, String arg1, Throwable arg2) {
    logger4j.debug(arg0);
    logger4j.debug(arg1, arg2);
    loggerAppenderInit();
  }

  public void debug(Marker arg0, String arg1, Object arg2, Object arg3) {
    logger4j.debug(arg0);
    logger4j.debug(arg1, arg2, arg3);
    loggerAppenderInit();
  }

  public void error(String arg0) {
    logger4j.error(arg0);
    loggerAppenderInit();
  }

  public void error(String arg0, Object arg1) {
    logger4j.error(arg0, arg1);
    loggerAppenderInit();
  }

  public void error(String arg0, Object... arg1) {
    logger4j.error(arg0,arg1);
    loggerAppenderInit();
  }

  public void error(String arg0, Throwable arg1) {
    logger4j.error(arg0, arg1);
    loggerAppenderInit();
  }

  public void error(Marker arg0, String arg1) {
    logger4j.error(arg0);
    logger4j.error(arg1);
    loggerAppenderInit();
  }

  public void error(String arg0, Object arg1, Object arg2) {
    logger4j.error(arg0, arg1, arg2);
    loggerAppenderInit();
  }

  public void error(Marker arg0, String arg1, Object arg2) {
    logger4j.error(arg0 + ": " + arg1);
    loggerAppenderInit();
  }

  public void error(Marker arg0, String arg1, Object... arg2) {
    logger4j.error(arg0 + ": " + arg1);
    loggerAppenderInit();
  }

  public void error(Marker arg0, String arg1, Throwable arg2) {
    logger4j.error(arg0 + ": " + arg1);
    loggerAppenderInit();
  }

  public void error(Marker arg0, String arg1, Object arg2, Object arg3) {
    logger4j.error(arg0);
    logger4j.error(arg1, arg2, arg3);
    loggerAppenderInit();
  }

  public String getName() {
    return this.name;
  }

  public void info(String arg0) {
    logger4j.info("This is customzed log " + arg0);
    loggerAppenderInit();
  }

  public void info(String arg0, Object arg1) {
    logger4j.info(arg0, arg1);
    loggerAppenderInit();
  }

  public void info(String arg0, Object... arg1) {
    logger4j.info(arg0, arg1);
    loggerAppenderInit();
  }

  public void info(String arg0, Throwable arg1) {
    logger4j.info(arg0, arg1);
    loggerAppenderInit();
  }

  public void info(Marker arg0, String arg1) {
    logger4j.info(arg0);
    logger4j.info(arg1);
    loggerAppenderInit();
  }

  public void info(String arg0, Object arg1, Object arg2) {
    logger4j.info(arg0, arg1, arg2);
  }

  public void info(Marker arg0, String arg1, Object arg2) {
    logger4j.info(arg0);
    logger4j.info(arg1, arg2);
  }

  public void info(Marker arg0, String arg1, Object... arg2) {
    logger4j.info(arg0);
    logger4j.info(arg1, arg2);
  }

  public void info(Marker arg0, String arg1, Throwable arg2) {
    logger4j.info(arg0);
    logger4j.info(arg1, arg2);
  }

  public void info(Marker arg0, String arg1, Object arg2, Object arg3) {
    logger4j.info(arg0);
    logger4j.info(arg1, arg2, arg3);
  }

  public boolean isDebugEnabled() {
    return logger4j.isDebugEnabled();
  }

  public boolean isDebugEnabled(Marker marker) {
    return logger4j.isDebugEnabled();
  }

  public boolean isErrorEnabled() {
    return true;
  }

  public boolean isErrorEnabled(Marker marker) {
    return true;
  }

  public boolean isInfoEnabled() {
    return logger4j.isInfoEnabled();
  }

  public boolean isInfoEnabled(Marker marker) {
    return logger4j.isInfoEnabled();
  }

  public boolean isTraceEnabled() {
    return logger4j.isTraceEnabled();
  }

  public boolean isTraceEnabled(Marker arg0) {
    return logger4j.isTraceEnabled();
  }

  public boolean isWarnEnabled() {
    return logger4j.isWarnEnabled();
  }

  public boolean isWarnEnabled(Marker arg0) {
    return logger4j.isWarnEnabled();
  }

  public void trace(String message) {
    logger4j.trace(message);
  }

  public void trace(String arg0, Object arg1) {
    logger4j.trace(arg0 + ": " + arg1);
  }

  public void trace(String arg0, Object... arg1) {
    logger4j.trace(arg0 + ": " + arg1);
  }

  public void trace(String arg0, Throwable arg1) {
    logger4j.trace(arg0, arg1);
  }

  public void trace(Marker arg0, String arg1) {
    logger4j.trace(arg0 + ": " + arg1);
  }

  public void trace(String arg0, Object arg1, Object arg2) {
    logger4j.trace(arg0 + ": " + arg1 + " , " + arg2);
  }

  public void trace(Marker arg0, String arg1, Object arg2) {
    logger4j.trace(arg0);
    logger4j.trace(arg1);
    logger4j.trace(arg2);
  }

  public void trace(Marker arg0, String arg1, Object... arg2) {
    logger4j.trace(arg0);
    logger4j.trace(arg1);
    logger4j.trace(arg2);
  }

  public void trace(Marker arg0, String arg1, Throwable arg2) {
    logger4j.trace(arg0);
    logger4j.trace(arg1);
    logger4j.trace(arg2);
  }

  public void trace(Marker arg0, String arg1, Object arg2, Object arg3) {
    logger4j.trace(arg0);
    logger4j.trace(arg1);
    logger4j.trace(arg2);
    logger4j.trace(arg2);
  }

  public void warn(String arg0) {
    logger4j.warn(arg0);
  }

  public void warn(String arg0, Object arg1) {
    logger4j.warn(arg0 + ": " + arg1);
  }

  public void warn(String arg0, Object... arg1) {
    logger4j.warn(arg0 + ": " + arg1);
  }

  public void warn(String arg0, Throwable arg1) {
    logger4j.warn(arg0);
    logger4j.warn(arg1);
  }

  public void warn(Marker arg0, String arg1) {
    logger4j.warn(arg0);
    logger4j.warn(arg1);
  }

  public void warn(String arg0, Object arg1, Object arg2) {
    logger4j.warn(arg0);
    logger4j.warn(arg1);
    logger4j.warn(arg2);
  }

  public void warn(Marker arg0, String arg1, Object arg2) {
    logger4j.warn(arg0);
    logger4j.warn(arg1);
    logger4j.warn(arg2);
  }

  public void warn(Marker arg0, String arg1, Object... arg2) {
    logger4j.warn(arg0);
    logger4j.warn(arg1);
    logger4j.warn(arg2);
  }

  public void warn(Marker arg0, String arg1, Throwable arg2) {
    logger4j.warn(arg0);
    logger4j.warn(arg1);
    logger4j.warn(arg2);
  }

  public void warn(Marker arg0, String arg1, Object arg2, Object arg3) {
    logger4j.warn(arg0);
    logger4j.warn(arg1);
    logger4j.warn(arg2);
    logger4j.warn(arg3);
  }
}
