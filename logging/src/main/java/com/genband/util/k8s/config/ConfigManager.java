package com.genband.util.k8s.config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;

import org.springframework.util.ResourceUtils;


public abstract class ConfigManager {

	Properties prop = null;
	private String configFilePath;
	private static final String DEFAULT_CONFIG_PATH = "config.properties";
	private String kubernetesMasterUrl;
	
	abstract public HashMap<String,String> getLabelMap();
	
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
	
	private void loadProperties() {
		prop = new Properties();
		InputStream input = null;
		try {
			if(configFilePath==null || "".equals(configFilePath)) {
				configFilePath = DEFAULT_CONFIG_PATH;
			}
			input = new FileInputStream(configFilePath);
			// load a properties file
			prop.load(input);
			kubernetesMasterUrl = prop.getProperty("kubernetes.master.url");
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
