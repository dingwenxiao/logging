package org.slf4j.impl;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.slf4j.Logger;
import org.slf4j.Marker;

public class GbLoggerAdapter implements Logger {

  org.apache.log4j.Logger logger4j;
  String name;

  public GbLoggerAdapter(String name) {
    logger4j = LogManager.getLogger(name);
    this.name = name;
  }

  public void debug(String arg0) {
    logger4j.debug(arg0);
  }

  public void debug(String arg0, Object arg1) {
    logger4j.debug(arg0 + ": " + arg1);
  }

  public void debug(String arg0, Object... arg1) {
    logger4j.debug(arg0 + ": " + arg1);
  }

  public void debug(String arg0, Throwable arg1) {
    logger4j.debug(arg0 + ": " + arg1);
  }

  public void debug(Marker arg0, String arg1) {
    logger4j.debug(arg0 + ": " + arg1);
  }

  public void debug(String arg0, Object arg1, Object arg2) {
    logger4j.debug(arg0 + ": " + arg1);
  }

  public void debug(Marker arg0, String arg1, Object arg2) {
    logger4j.debug(arg0);
    logger4j.debug(arg1);
    logger4j.debug(arg2);
  }

  public void debug(Marker arg0, String arg1, Object... arg2) {
    logger4j.debug(arg0);
    logger4j.debug(arg1);
    logger4j.debug(arg2);
  }

  public void debug(Marker arg0, String arg1, Throwable arg2) {
    logger4j.debug(arg0);
    logger4j.debug(arg1);
    logger4j.debug(arg2);
  }

  public void debug(Marker arg0, String arg1, Object arg2, Object arg3) {
    logger4j.debug(arg0);
    logger4j.debug(arg1);
    logger4j.debug(arg2);
  }

  public void error(String arg0) {
    logger4j.error(arg0);
  }

  public void error(String arg0, Object arg1) {
    logger4j.error(arg0 + ": " + arg1);
  }

  public void error(String arg0, Object... arg1) {
    logger4j.error(arg0 + ": " + arg1);
  }

  public void error(String arg0, Throwable arg1) {
    logger4j.error(arg0 + ": " + arg1);
  }

  public void error(Marker arg0, String arg1) {
    logger4j.error(arg0 + ": " + arg1);
  }

  public void error(String arg0, Object arg1, Object arg2) {
    logger4j.error(arg0 + ": " + arg1);
  }

  public void error(Marker arg0, String arg1, Object arg2) {
    logger4j.error(arg0 + ": " + arg1);
  }

  public void error(Marker arg0, String arg1, Object... arg2) {
    logger4j.error(arg0 + ": " + arg1);
  }

  public void error(Marker arg0, String arg1, Throwable arg2) {
    logger4j.error(arg0 + ": " + arg1);
  }

  public void error(Marker arg0, String arg1, Object arg2, Object arg3) {
    logger4j.error(arg0 + ": " + arg1);
  }

  public String getName() {
    return this.name;
  }

  public void info(String arg0) {
    logger4j.info("This is customzed log " + arg0);
  }

  public void info(String arg0, Object arg1) {
    logger4j.info(arg0 + arg1);
  }

  public void info(String arg0, Object... arg1) {
    logger4j.info(arg0 + ": " + arg1);
  }

  public void info(String arg0, Throwable arg1) {
    logger4j.info(arg0);
    logger4j.info(arg1);
  }

  public void info(Marker arg0, String arg1) {
    logger4j.info(arg0);
    logger4j.info(arg1);
  }

  public void info(String arg0, Object arg1, Object arg2) {
    logger4j.info(arg0);
    logger4j.info(arg1);
    logger4j.info(arg2);
  }

  public void info(Marker arg0, String arg1, Object arg2) {
    logger4j.info(arg0);
    logger4j.info(arg1);
    logger4j.info(arg2);
  }

  public void info(Marker arg0, String arg1, Object... arg2) {
    logger4j.info(arg0);
    logger4j.info(arg1);
    logger4j.info(arg2);
  }

  public void info(Marker arg0, String arg1, Throwable arg2) {
    logger4j.info(arg0);
    logger4j.info(arg1);
    logger4j.info(arg2);
  }

  public void info(Marker arg0, String arg1, Object arg2, Object arg3) {
    logger4j.info(arg0);
    logger4j.info(arg1);
    logger4j.info(arg2);
    logger4j.info(arg2);
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
    return logger4j.getLevel().equals(Level.WARN);
  }

  public boolean isWarnEnabled(Marker arg0) {
    return logger4j.getLevel().equals(Level.WARN);
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
