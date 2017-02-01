package com.genband.util.log;
/*
 * ModeShape (http://www.modeshape.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.util.List;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modeshape.common.i18n.I18nResource;
import org.modeshape.common.util.StringUtil;

import com.genband.util.k8s.KubernetesNetworkService;
import com.genband.util.k8s.config.ConfigManager;
import com.genband.util.k8s.config.KafkaConfigManager;

/**
 * A very crude but simple example of a custom {@link Logger} that simply writes all log messages (except trace) out to
 * System.out. This should never be used in production, but is an example of how one can create
 * 
 * @see GbLoggerFactory
 */
class GbLogger extends org.modeshape.common.logging.Logger {

    private final Logger logger;
    KubernetesNetworkService kubernetesNetworkService;
    
    public GbLogger( String name ) {
        logger = LogManager.getLogger(name);;
    }
    
    public void loggerAppenderInit() {
      ConfigManager configManager = new KafkaConfigManager(CONSTANT.LOG_CONIG_PATH);
      new KubernetesNetworkService.SingletonBuilder(new KafkaConfigManager("config.properties"))
          .build();
      kubernetesNetworkService = KubernetesNetworkService.getInstance();
      LogConfigurationUtil.addKafkaAppender(configManager, getKakfaAddress(), "kafka-info", "info",
          org.apache.logging.log4j.Level.INFO, org.apache.logging.log4j.Level.WARN);
      LogConfigurationUtil.addKafkaAppender(configManager, getKakfaAddress(), "kafka-debug", "debug",
          org.apache.logging.log4j.Level.DEBUG, org.apache.logging.log4j.Level.INFO);
      LogConfigurationUtil.addKafkaAppender(configManager, getKakfaAddress(), "kafka-trace", "trace",
          org.apache.logging.log4j.Level.TRACE, org.apache.logging.log4j.Level.DEBUG);
      LogConfigurationUtil.addKafkaAppender(configManager, getKakfaAddress(), "kafka-error", "error",
          org.apache.logging.log4j.Level.ERROR, org.apache.logging.log4j.Level.FATAL);
      LogConfigurationUtil.addKafkaAppender(configManager, getKakfaAddress(), "kafka-warn", "warn",
          org.apache.logging.log4j.Level.WARN, org.apache.logging.log4j.Level.ERROR);
    }

    private String getKakfaAddress() {

      List<String> fetchKafkaAddress = kubernetesNetworkService.getEndPointsAddressFromConfigMap();

      if (fetchKafkaAddress != null) {
        logger.info(fetchKafkaAddress.toString());
      } else {
        logger.info("Kafka adderss is empty");
      }

      // the kafka address is not ready yet.
      return "172.28.247.239:9092";
    }

    @Override
    public String getName() {
        return logger.getName();
    }

    @Override
    public boolean isTraceEnabled() {
        return logger.isTraceEnabled();
    }

    @Override
    public boolean isDebugEnabled() {
        return logger.isDebugEnabled();
    }

    @Override
    public boolean isInfoEnabled() {
        return logger.isInfoEnabled();
    }

    @Override
    public boolean isWarnEnabled() {
        return logger.isWarnEnabled();
    }

    @Override
    public boolean isErrorEnabled() {
        return logger.isErrorEnabled();
    }

    @Override
    public void warn( Throwable t,
                      I18nResource message,
                      Object... params ) {
        if (!isWarnEnabled()) return;
       
    }

    @Override
    public void warn( I18nResource message,
                      Object... params ) {
        if (!isWarnEnabled()) return;
        
    }

    /**
     * Log a message at the DEBUG level according to the specified format and (optional) parameters. The message should contain a
     * pair of empty curly braces for each of the parameter, which should be passed in the correct order. This method is efficient
     * and avoids superfluous object creation when the logger is disabled for the DEBUG level.
     * 
     * @param message the message string
     * @param params the parameter values that are to replace the variables in the format string
     */
    @Override
    public void debug( String message,
                       Object... params ) {
        if (!isDebugEnabled()) return;
        logger.debug(message, params);
    }

    /**
     * Log an exception (throwable) at the DEBUG level with an accompanying message. If the exception is null, then this method
     * calls {@link #debug(String, Object...)}.
     * 
     * @param t the exception (throwable) to log
     * @param message the message accompanying the exception
     * @param params the parameter values that are to replace the variables in the format string
     */
    @Override
    public void debug( Throwable t,
                       String message,
                       Object... params ) {
        if (!isDebugEnabled()) return;
        logger.debug(message, params, t);
    }

    /**
     * Log a message at the ERROR level according to the specified format and (optional) parameters. The message should contain a
     * pair of empty curly braces for each of the parameter, which should be passed in the correct order. This method is efficient
     * and avoids superfluous object creation when the logger is disabled for the ERROR level.
     * 
     * @param message the message string
     * @param params the parameter values that are to replace the variables in the format string
     */
    @Override
    public void error( I18nResource message,
                       Object... params ) {
        if (!isErrorEnabled()) return;
        logger.error(message);
    }

    /**
     * Log an exception (throwable) at the ERROR level with an accompanying message. If the exception is null, then this method
     * calls {@link org.modeshape.common.logging.Logger#error(org.modeshape.common.i18n.I18nResource, Object...)}.
     * 
     * @param t the exception (throwable) to log
     * @param message the message accompanying the exception
     * @param params the parameter values that are to replace the variables in the format string
     */
    @Override
    public void error( Throwable t,
                       I18nResource message,
                       Object... params ) {
        if (!isErrorEnabled()) return;
        logger.error(message.text(message), t);
    }

    /**
     * Log a message at the INFO level according to the specified format and (optional) parameters. The message should contain a
     * pair of empty curly braces for each of the parameter, which should be passed in the correct order. This method is efficient
     * and avoids superfluous object creation when the logger is disabled for the INFO level.
     * 
     * @param message the message string
     * @param params the parameter values that are to replace the variables in the format string
     */
    @Override
    public void info( I18nResource message,
                      Object... params ) {
        if (!isInfoEnabled()) return;
        logger.info(message.text(message));
        logger.info(params);
    }

    /**
     * Log an exception (throwable) at the INFO level with an accompanying message. If the exception is null, then this method
     * calls {@link org.modeshape.common.logging.Logger#info(org.modeshape.common.i18n.I18nResource, Object...)}.
     * 
     * @param t the exception (throwable) to log
     * @param message the message accompanying the exception
     * @param params the parameter values that are to replace the variables in the format string
     */
    @Override
    public void info( Throwable t,
                      I18nResource message,
                      Object... params ) {
        if (!isInfoEnabled()) return;
       // logger.write(MessageType.INFO, message, params, t);
    }

    /**
     * Log a message at the TRACE level according to the specified format and (optional) parameters. The message should contain a
     * pair of empty curly braces for each of the parameter, which should be passed in the correct order. This method is efficient
     * and avoids superfluous object creation when the logger is disabled for the TRACE level.
     * 
     * @param message the message string
     * @param params the parameter values that are to replace the variables in the format string
     */
    @Override
    public void trace( String message,
                       Object... params ) {
        if (!isTraceEnabled()) return;
      //  logger.write(MessageType.TRACE, message, params, null);
    }

    /**
     * Log an exception (throwable) at the TRACE level with an accompanying message. If the exception is null, then this method
     * calls {@link #trace(String, Object...)}.
     * 
     * @param t the exception (throwable) to log
     * @param message the message accompanying the exception
     * @param params the parameter values that are to replace the variables in the format string
     */
    @Override
    public void trace( Throwable t,
                       String message,
                       Object... params ) {
        if (!isTraceEnabled()) return;
      //  logger.write(MessageType.TRACE, message, params, t);
    }

    protected static enum MessageType {
        INFO,
        ERROR,
        WARNING,
        DEBUG,
        TRACE
    }

    /**
     * A crude example of a logging facility to which our logger can delegate. This should never be used in production; always use
     * a real logging framework or the JDK logger.
     */
    protected static class SystemLogger {
        private final String category;

        protected SystemLogger( String category ) {
            this.category = category;
        }

        public String getName() {
            return category;
        }

        public boolean isEnabled( MessageType msgType ) {
            switch (msgType) {
                case TRACE:
                    return false;
                default:
                    return true;
            }
        }

        public final void write( MessageType msgType,
                                 I18nResource msg,
                                 Object[] params,
                                 Throwable t ) {
            if (msg != null) {
                System.out.println(msgType + " [" + category + "]: " + msg.text(getLoggingLocale(), params));
            }
            if (t != null) {
                System.out.print(msgType + " [" + category + "]: ");
                t.printStackTrace();
                System.out.println();
            }
        }

        public final void write( MessageType msgType,
                                 String msg,
                                 Object[] params,
                                 Throwable t ) {
            if (msg != null) {
                System.out.println(msgType + " [" + category + "]: " + StringUtil.createString(msg, params));
            }
            if (t != null) {
                System.out.print(msgType + " [" + category + "]: ");
                t.printStackTrace();
                System.out.println();
            }
        }

    }
}
