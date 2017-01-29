package com.genband.util.k8s.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigManager {

	Properties prop = null;
	private String configFilePath;
	private static final String DEFAULT_CONFIG_PATH = "config.properties";
	private String kubernetesMasterUrl;
	
	
	public String getKubernetesMasterUrl() {
		return kubernetesMasterUrl;
	}
	public ConfigManager() {
		
	}
	
	public ConfigManager(String configFilePath) {
		this.configFilePath = configFilePath;
	}

	public void loadProperties() {
		prop = new Properties();
		InputStream input = null;
		try {
			if(configFilePath==null || "".equals(configFilePath)) {
				configFilePath = DEFAULT_CONFIG_PATH;
			}
			input = new FileInputStream(configFilePath);
			// load a properties file
			prop.load(input);
			kubernetesMasterUrl = prop.getProperty("");
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

	public Properties getProp() {
		return prop;
	}	
	
}
