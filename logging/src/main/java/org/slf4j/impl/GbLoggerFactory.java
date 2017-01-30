package org.slf4j.impl;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;

public class GbLoggerFactory implements ILoggerFactory {
  ConcurrentMap<String, Logger> loggerMap;

  public GbLoggerFactory() {
    loggerMap = new ConcurrentHashMap();
  }

  /**
   * Return an appropriate {@link SimpleLogger} instance by name.
   */
  public Logger getLogger(String name) {
    Logger gbLogger = loggerMap.get(name);
    if (gbLogger != null) {
      return gbLogger;
    } else {
      Logger newInstance = new GbLoggerAdapter(name);
      Logger oldInstance = loggerMap.putIfAbsent(name, newInstance);
      return oldInstance == null ? newInstance : oldInstance;
    }
  }

  void reset() {
    loggerMap.clear();
  }
}
