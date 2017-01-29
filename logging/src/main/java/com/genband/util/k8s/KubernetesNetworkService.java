package com.genband.util.k8s;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.genband.util.k8s.config.ConfigManager;
import com.genband.util.k8s.config.KafkaConfigManager;

import io.fabric8.kubernetes.api.model.EndpointAddress;
import io.fabric8.kubernetes.api.model.EndpointPort;
import io.fabric8.kubernetes.api.model.EndpointSubset;
import io.fabric8.kubernetes.api.model.Endpoints;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientException;
import io.fabric8.kubernetes.client.Watcher;

public enum KubernetesNetworkService {

	SERVICE_INSTANCE;
	private static String KUBERNETES_MASTER_URL_KEY = "kubernetesMasterUrl";
	private final static String kubernetesMasterTestUrl = "http://172.28.250.4:8080/";

	private Logger logger = LoggerFactory.getLogger(KubernetesNetworkService.class);

	private String kubernetesMasterUrl;
	private Config config;
	private KubernetesClient client;
	private List<String> kafkaAddressList;
	private EndPointsWatcherCallback endPointsWatcherCallback;
	private ConfigManager configManager = null;

	public static KubernetesNetworkService getInstance(EndPointsWatcherCallback callback) {
		return SERVICE_INSTANCE;
	}

	private void build(SingletonBuilder builder) {
		this.kubernetesMasterUrl = builder.kubernetesMasterUrl;
		this.endPointsWatcherCallback = builder.endPointsWatcherCallback;
		this.configManager = builder.configManager;
		
		//kafkaLabelConfigManager = new KafkaConfigManager(builder.configManager.getProp());
		if (fetchKubernetesMasterAddressFromSystem() || kubernetesMasterUrl != null) {
			config = new ConfigBuilder().withMasterUrl(kubernetesMasterUrl).build();
			client = new DefaultKubernetesClient(this.config);
			kafkaAddressList = new ArrayList<>();
		}
	}

	public List<String> fetchKafkaAddress() {
		// set kafka labels map
		/**
		 * currently we don't have kafka server ready, so we use another label
		 * which has return value to test it, which should be replaced by kafka
		 * label like "service":"kafka"
		 * 
		 * @author hakuang
		 * @date 2017/1/25
		 */
		// currently only have one kafka entry address.
		return fetchKubernetesEndpointsAddress(configManager.getKafakLabels());
	}

	public void startKafkaAddressWatcher() {
		initKubernetesLabelsWatcher(kafkaLabelConfigManager.getKafakLabels());
	}

	protected void setEndPointsWatcherCallback(EndPointsWatcherCallback endPointsWatcherCallback) {
		this.endPointsWatcherCallback = endPointsWatcherCallback;
	}
	
	protected void close() {
		client.close();
	}
	
	/*public void init() {
		kafkaLabelConfigManager = new KafkaLabelConfigManager(configManager.getProp());
		if (fetchKubernetesMasterAddressFromSystem() && kubernetesMasterUrl != null) {
			config = new ConfigBuilder().withMasterUrl(kubernetesMasterUrl).build();
			client = new DefaultKubernetesClient(this.config);
			kafkaAddressList = new ArrayList<>();
		}
	}*/

	/*
	 * private KubernetesNetworkService() {
	 *//**
		 * under windows, get system variable sometimes doesn't work
		 * 
		 * @author hakuang
		 * @data 2017/01/25
		 *//*
		 * kafkaLabelConfigManager = new KafkaLabelConfigManager(new
		 * ConfigManager().getProp()); if
		 * (fetchKubernetesMasterAddressFromSystem() && kubernetesMasterUrl !=
		 * null) { config = new
		 * ConfigBuilder().withMasterUrl(kubernetesMasterUrl).build(); client =
		 * new DefaultKubernetesClient(this.config); kafkaAddressList = new
		 * ArrayList<>(); } }
		 */

	/*
	 * private KubernetesNetworkService(EndPointsWatcherCallback callback) {
	 * this(); this.endPointsWatcherCallback = callback; if
	 * (endPointsWatcherCallback != null) { startKafkaAddressWatcher(); } }
	 */

	/**
	 * The logic of fetching endpoints might be changed in the new k8s
	 * environment. Currently, it only consider one item.
	 * 
	 * @param labelsMap
	 * @return
	 * @author hakuang
	 * @date 2017/01/25
	 */
	private List<String> fetchKubernetesEndpointsAddress(HashMap<String, String> labelsMap) {
		if (client != null) {
			List<Endpoints> items = client.endpoints().withLabels(labelsMap).list().getItems();
			return processEndpointsAddress(items.get(0));
		} else {
			return null;
		}
		// so far the result size should be 1
	}

	private void initKubernetesLabelsWatcher(HashMap<String, String> labelsMap) {
		if (client != null) {
			client.endpoints().withLabels(labelsMap).watch(new EndpointWatcher());
		}
	}

	/**
	 * get kubernetes url from system variable which will be set in docker file.
	 * sometimes, this doesn't work under windows system. Dont't know the reason
	 * yet.
	 * 
	 * @return
	 */
	private boolean fetchKubernetesMasterAddressFromSystem() {
		Map<String, String> env = System.getenv();
		if (env.containsKey(KUBERNETES_MASTER_URL_KEY)) {
			kubernetesMasterUrl = env.get(KUBERNETES_MASTER_URL_KEY);
			return true;
		}
		return false;
	}

	/**
	 * This is only used to test which should be removed later
	 * 
	 * @return
	 */
/*	private boolean fetchKubernetesMasterAddress() {
		kubernetesMasterUrl = "172.28.250.4:8080";
		return true;
	}*/

	
	private List<,List<String>> processEndpointsAddress(Endpoints endpoints) {
		List<List<String>> endPointsAddress = new ArrayList<>();

//		for (EndpointSubset subset : endpoints.getSubsets()) {
//			for (EndpointAddress address : subset.getAddresses()) {
//				for (EndpointPort port : subset.getPorts()) {
//					endPointsAddress.add(address.getIp() + ":" + port.getPort());
//				}
//			}
//		}
		
		endpoints
		for (EndpointSubset subset : endpoints.getSubsets()) {
			endPointsAddress.add(subset.getAddresses());
		}
		
		return endPointsAddress;
	}

	private class EndpointWatcher implements Watcher<Endpoints> {

		@Override
		public void eventReceived(io.fabric8.kubernetes.client.Watcher.Action action, Endpoints endpoints) {
			// logger.debug("Get Endpoints Action: " + action.toString() + " \n
			// " + endpoints.toString());
			if (action == Action.MODIFIED) {
				processModifiedAction(endpoints);
			} else if (action == Action.ADDED) {
				processAddedAction(endpoints);
			} else if (action == Action.DELETED) {
				processDeletedFunction();
			}
			if (endPointsWatcherCallback != null && kafkaAddressList != null) {
				endPointsWatcherCallback.sendAddressList(kafkaAddressList);
			}
		}

		private void processDeletedFunction() {
			kafkaAddressList = null;
			// logger.info("Cleaning address related info...");
		}

		private void processAddedAction(Endpoints endpoints) {
			// logger.info("current kafka address list: " + kafkaAddressList);
			kafkaAddressList = processEndpointsAddress(endpoints);
			// logger.info(
			// "get Kubernetes Watch Add event \n" + "update kafka address list
			// to:" + kafkaAddressList);
		}

		private void processModifiedAction(Endpoints endpoints) {
			// logger.info("current kafka address list: " + kafkaAddressList);
			kafkaAddressList = processEndpointsAddress(endpoints);
			// logger.info("get Kubernetes Watch Modify event \n" + "update
			// kafka address list to:"
			// + kafkaAddressList);
		}

		@Override
		public void onClose(KubernetesClientException cause) {
			// logger.info("kafka watch closed");
		}
	}

	public static class SingletonBuilder {

		private final String kubernetesMasterUrl; // Mandatory
		private ConfigManager configManager = null; // Mandatory
		private EndPointsWatcherCallback endPointsWatcherCallback = null;

//		private SingletonBuilder() {
//			kubernetesMasterUrl = kubernetesMasterTestUrl;
//		}

		public SingletonBuilder(ConfigManager configManager) {
			this.kubernetesMasterUrl = configManager.getKubernetesMasterUrl();
			this.configManager = configManager;
		}

		public SingletonBuilder endPointsWatcherCallback(EndPointsWatcherCallback endPointsWatcherCallback) {
			this.endPointsWatcherCallback = endPointsWatcherCallback;
			return this;
		}

		public void build() {
			KubernetesNetworkService.SERVICE_INSTANCE.build(this);
		}
	}
}
