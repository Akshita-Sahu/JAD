package com.akshita.jad.spring;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.ConfigurableEnvironment;

import com.akshita.jad.agent.attach.JADAgent;

/**
 * 
 * @author hengyunabc 2020-06-22
 *
 */
@ConditionalOnProperty(name = "spring.jad.enabled", matchIfMissing = true)
@EnableConfigurationProperties({ JADProperties.class })
public class JADConfiguration {
	private static final Logger logger = LoggerFactory.getLogger(JADConfiguration.class);

	@Autowired
	ConfigurableEnvironment environment;

	/**
	 * <pre>
	 * 1.  jad.* ，JAD
	 * 2. ，JADProperties。
	 * </pre>
	 */
	@ConfigurationProperties(prefix = "jad")
	@ConditionalOnMissingBean(name="jadConfigMap")
	@Bean
	public HashMap<String, String> jadConfigMap() {
		return new HashMap<String, String>();
	}

	@ConditionalOnMissingBean
	@Bean
	public JADAgent jadAgent(@Autowired @Qualifier("jadConfigMap") Map<String, String> jadConfigMap,
			@Autowired JADProperties jadProperties) throws Throwable {
        jadConfigMap = StringUtils.removeDashKey(jadConfigMap);
        JADProperties.updateJADConfigMapDefaultValue(jadConfigMap);
        /**
         * @see org.springframework.boot.context.ContextIdApplicationContextInitializer#getApplicationId(ConfigurableEnvironment)
         */
        String appName = environment.getProperty("spring.application.name");
        if (jadConfigMap.get("appName") == null && appName != null) {
            jadConfigMap.put("appName", appName);
        }

		// 
		Map<String, String> mapWithPrefix = new HashMap<String, String>(jadConfigMap.size());
		for (Entry<String, String> entry : jadConfigMap.entrySet()) {
			mapWithPrefix.put("jad." + entry.getKey(), entry.getValue());
		}

		final JADAgent jadAgent = new JADAgent(mapWithPrefix, jadProperties.getHome(),
				jadProperties.isSlientInit(), null);

		jadAgent.init();
		logger.info("JAD agent start success.");
		return jadAgent;

	}
}
