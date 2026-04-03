package com.akshita.jad.spring.endpoints;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;

import com.akshita.jad.agent.attach.JADAgent;

/**
 * 
 * @author hengyunabc 2020-06-24
 *
 */
@Endpoint(id = "jad")
public class JADEndPoint {

	@Autowired(required = false)
	private JADAgent jadAgent;

	@Autowired(required = false)
	private HashMap<String, String> jadConfigMap;

	@ReadOperation
	public Map<String, Object> invoke() {
		Map<String, Object> result = new HashMap<String, Object>();

		if (jadConfigMap != null) {
			result.put("jadConfigMap", jadConfigMap);
		}

		String errorMessage = jadAgent.getErrorMessage();
		if (errorMessage != null) {
			result.put("errorMessage", errorMessage);
		}

		return result;
	}

}
