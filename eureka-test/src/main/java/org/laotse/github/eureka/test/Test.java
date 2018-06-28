package org.laotse.github.eureka.test;

import com.netflix.appinfo.CloudInstanceConfig;
import com.netflix.appinfo.EurekaInstanceConfig;
import com.netflix.discovery.DefaultEurekaClientConfig;
import com.netflix.discovery.EurekaClientConfig;

public class Test {

	public static void main(String[] args) {
		EurekaClientConfig config = new DefaultEurekaClientConfig();
		
		EurekaInstanceConfig eurekaInstanceConfig = new CloudInstanceConfig();
	}
	
}
