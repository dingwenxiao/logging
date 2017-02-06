package com.genband.util.log;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.logging.log4j.simple.SimpleLogger;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;

import com.genband.util.k8s.connection.LoggerOperation;

public class GbLoggerFactory implements ILoggerFactory {
  ConcurrentMap<String, Logger> loggerMap;
  boolean isAppenderInitialized = false;

  public GbLoggerFactory() {
    loggerMap = new ConcurrentHashMap<>();
  }

  /**
   * Return an appropriate {@link SimpleLogger} instance by name.
   */
  @Override
  public Logger getLogger(String name) {

//    if (!isAppenderInitialized) {
//      isAppenderInitialized = true;
//      LoggerOperation.LoggerAppenderUpdate();
//      LoggerOperation.startWatch();
//    }

    Logger gbLogger = loggerMap.get(name);
    if (gbLogger != null) {
      return gbLogger;
    } else {
      Logger newInstance = new GbLoggerAdapter(org.apache.logging.log4j.LogManager.getLogger(name));
      Logger oldInstance = loggerMap.putIfAbsent(name, newInstance);
      return oldInstance == null ? newInstance : oldInstance;
    }
  }

  void reset() {
    loggerMap.clear();
  }

}
