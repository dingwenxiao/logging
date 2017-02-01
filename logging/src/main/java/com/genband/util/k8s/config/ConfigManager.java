package com.genband.util.k8s.config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;

import org.springframework.util.ResourceUtils;

/**
 * this class is configuration for kubernetes connection. Any configuration of services on k8s
 *  such as kafka, you can extend the abstract class.  
 * @author dixiao
 *
 */
public abstract class ConfigManager {

  Properties properties = null;
  private String configFilePath;
  private static final String DEFAULT_CONFIG_PATH = "config.properties";
  private String kubernetesMasterUrl;

  abstract public HashMap<String, String> getLabelMap();

  abstract public Properties getProperties();

  abstract public void setProperties(Properties properties);


  public ConfigManager() {
    loadProperties();
  }

  public ConfigManager(String configFilePath) {
    try {
      this.configFilePath = ResourceUtils.getFile("classpath:" + configFilePath).getAbsolutePath();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    loadProperties();
  }

  public String getKubernetesMasterUrl() {
    return kubernetesMasterUrl;
  }
  
/**load all properties from the config file 
 * 
 */
  private void loadProperties() {
    properties = new Properties();
    InputStream input = null;
    try {
      if (configFilePath == null || "".equals(configFilePath)) {
        configFilePath = DEFAULT_CONFIG_PATH;
      }
      input = new FileInputStream(configFilePath);
      // load a properties file
      properties.load(input);
      kubernetesMasterUrl = properties.getProperty("kubernetes.master.url");
    } catch (IOException ex) {
      ex.printStackTrace();
    } finally {
      if (input != null) {
        try {
          input.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }

}
