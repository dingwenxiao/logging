package com.genband.util.log;

import java.io.Serializable;
import java.util.List;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.ParameterizedMessage;
import org.apache.logging.log4j.message.SimpleMessage;
import org.apache.logging.log4j.spi.ExtendedLogger;
import org.apache.logging.slf4j.EventDataConverter;
import org.apache.logging.slf4j.Log4jLogger;
import org.apache.logging.slf4j.Log4jMarker;
import org.apache.logging.slf4j.Log4jMarkerFactory;
import org.slf4j.Logger;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.slf4j.helpers.MarkerIgnoringBase;
import org.slf4j.impl.StaticMarkerBinder;
import org.slf4j.spi.LocationAwareLogger;

import com.genband.util.k8s.KubernetesNetworkService;
import com.genband.util.k8s.KubernetesNetworkServiceClass;
import com.genband.util.k8s.config.ConfigManager;
import com.genband.util.k8s.config.KafkaConfigManager;

/**
 * Users can customize their logger by implementing following functions
 * 
 * @author dixiao
 *
 */
public class GbLoggerAdapter implements LocationAwareLogger, Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  org.apache.logging.log4j.Logger logger4j;
  org.apache.log4j.Logger root = org.apache.log4j.LogManager.getLogger("sss");
  public static final String FQCN = Log4jLogger.class.getName();

  private static final Marker EVENT_MARKER = MarkerFactory.getMarker("EVENT");
  //private final boolean eventLogger;
  private transient ExtendedLogger logger;
  private final String name;
  private transient EventDataConverter converter;
  
  public GbLoggerAdapter(org.apache.logging.log4j.Logger logger4j) {
    // System.out.println("Adapter");
    this.logger4j = logger4j;
    this.name = logger4j.getName();
  }

  public void debug(String arg0) {
    logger4j.debug(arg0);
  }

  public void debug(String arg0, Object arg1) {
    logger4j.debug(arg0, arg1);
  }

  public void debug(String arg0, Object... arg1) {
    logger4j.debug(arg0, arg1);
  }

  public void debug(String arg0, Throwable arg1) {
    logger4j.debug(arg0, arg1);
  }

  public void debug(Marker arg0, String arg1) {
    logger4j.debug(arg0);
    logger4j.debug(arg1);
  }

  public void debug(String arg0, Object arg1, Object arg2) {
    logger4j.debug(arg0, arg1, arg2);
  }

  public void debug(Marker arg0, String arg1, Object arg2) {
    logger4j.debug(arg0);
    logger4j.debug(arg1, arg2);
  }

  public void debug(Marker arg0, String arg1, Object... arg2) {
    logger4j.debug(arg0);
    logger4j.debug(arg1, arg2);
  }

  public void debug(Marker arg0, String arg1, Throwable arg2) {
    logger4j.debug(arg0);
    logger4j.debug(arg1, arg2);
  }

  public void debug(Marker arg0, String arg1, Object arg2, Object arg3) {
    logger4j.debug(arg0);
    logger4j.debug(arg1, arg2, arg3);
  }

  public void error(String arg0) {
    logger4j.error(arg0);
  }

  public void error(String arg0, Object arg1) {
    logger4j.error(arg0, arg1);
  }

  public void error(String arg0, Object... arg1) {
    logger4j.error(arg0, arg1);
  }

  public void error(String arg0, Throwable arg1) {
    logger4j.error(arg0, arg1);
  }

  public void error(Marker arg0, String arg1) {
    logger4j.error(arg0);
    logger4j.error(arg1);
  }

  public void error(String arg0, Object arg1, Object arg2) {
    logger4j.error(arg0, arg1, arg2);
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
    logger4j.error(arg0);
    logger4j.error(arg1, arg2, arg3);
  }

  @Override
  public String getName() {
    return this.name;
  }

  public void info(String arg0) {
    // logger4j.info("This is customzed log " + arg0);
    root.info("asdfsd");
  }

  public void info(String arg0, Object arg1) {
    logger4j.info(arg0, arg1);
  }

  public void info(String arg0, Object... arg1) {
    logger4j.info(arg0, arg1);
  }

  public void info(String arg0, Throwable arg1) {
    logger4j.info(arg0, arg1);
  }

  public void info(Marker arg0, String arg1) {
    logger4j.info(arg0);
    logger4j.info(arg1);
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

  @Override
  public void log(Marker marker, String fqcn, int level, String message, Object[] params,
      Throwable throwable) {
    final Level log4jLevel = getLevel(level);
    final org.apache.logging.log4j.Marker log4jMarker = getMarker(marker);

    if (!logger.isEnabled(log4jLevel, log4jMarker, message, params)) {
        return;
    }
    final Message msg;
    if (marker != null && marker.contains(EVENT_MARKER) && converter != null) {
        msg = converter.convertEvent(message, params, throwable);
    } else if (params == null) {
        msg = new SimpleMessage(message);
    } else {
        msg = new ParameterizedMessage(message, params, throwable);
        if (throwable != null) {
            throwable = msg.getThrowable();
        }
    }
    //logger4j.logMessage(fqcn, log4jLevel, log4jMarker, msg, throwable);
    logger4j.log(log4jLevel, message, params[0]);
  }
  
  private static org.apache.logging.log4j.Marker getMarker(final Marker marker) {
    if (marker == null) {
        return null;
    } else if (marker instanceof Log4jMarker) {
        return ((Log4jMarker) marker).getLog4jMarker();
    } else {
        final Log4jMarkerFactory factory = (Log4jMarkerFactory) StaticMarkerBinder.SINGLETON.getMarkerFactory();
        return ((Log4jMarker) factory.getMarker(marker)).getLog4jMarker();
    }
}
  
  private static Level getLevel(final int i) {
    switch (i) {
    case TRACE_INT:
        return Level.TRACE;
    case DEBUG_INT:
        return Level.DEBUG;
    case INFO_INT:
        return Level.INFO;
    case WARN_INT:
        return Level.WARN;
    case ERROR_INT:
        return Level.ERROR;
    }
    return Level.ERROR;
}
}
