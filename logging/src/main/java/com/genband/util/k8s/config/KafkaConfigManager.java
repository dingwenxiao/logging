package com.genband.util.k8s.config;

import java.util.HashMap;
import java.util.Properties;

public class KafkaConfigManager extends ConfigManager{

	private HashMap<String, String> kafkaLabelsMap = new HashMap<>();

	public KafkaConfigManager() {
		loadKafakLabelMap();
	}

	public HashMap<String, String> getKafakLabels() {
		if (kafkaLabelsMap.isEmpty()) {
			loadKafakLabelMap();
		}
		return kafkaLabelsMap;
	}

	private void loadKafakLabelMap() {
		for (String key : prop.stringPropertyNames()) {
			if (key.startsWith("label")) {
				String value = prop.getProperty(key);
				kafkaLabelsMap.put(key, value);
			}
		}
	}

	public Properties getProp() {
		return prop;
	}

	public void setProp(Properties prop) {
		this.prop = prop;
	}

	public void resetKafakLabelMap() {
		kafkaLabelsMap = new HashMap<>();
		loadKafakLabelMap();
	}
}
