package com.genband.util.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * So far it's useless. The wrap won't work since the code location is not correct.
 * need to find a way to wrap our own log4j.
 * should be removed after Dingwen checked
 * @author hakuang
 *
 */
public class MessageLoggerFactory {
  public static MessageLogger getLogger(Class<?> clazz) {
    Logger logger = LoggerFactory.getLogger(clazz);

    return new MessageLogger(logger);
  }
}
