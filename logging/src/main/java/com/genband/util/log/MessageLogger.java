package com.genband.util.log;

import org.slf4j.Logger;

/**
 * So far it's useless. The wrap won't work since the code location is not correct. need to find a
 * way to wrap our own log4j. should be removed after Dingwen checked
 * 
 * @author hakuang
 *
 */
public class MessageLogger {
  private Logger logger;

  public MessageLogger(Logger logger) {
    this.logger = logger;
  }

  public void messageInfo(String msg) {
    // TODO can parse the msg here
    logger.info(msg);
  }

  public void messageTrace(String msg) {
    // TODO can parse the msg here
    logger.trace(msg);
  }

  public void messageDebug(String msg) {
    // TODO can parse the msg here
    logger.debug(msg);
  }

  public void messageWarn(String msg) {
    // TODO can parse the msg here
    logger.warn(msg);
  }

  public void messageError(String msg) {
    // TODO can parse the msg here
    logger.error(msg);
  }

  // TODO add more decoration class if you want
  // ...
}
