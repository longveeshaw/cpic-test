package org.laotse.github.gateway.test;

import java.net.URI;

import org.springframework.cloud.gateway.handler.FilteringWebHandler;
import org.springframework.cloud.gateway.handler.RoutePredicateHandlerMapping;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

@Primary
@Component
public class RoutePredicateHandlerMappingX extends RoutePredicateHandlerMapping {

	public RoutePredicateHandlerMappingX(FilteringWebHandler webHandler, RouteLocator routeLocator) {
		super(webHandler, routeLocator);
	}
	
	@Override
	protected void validateRoute(Route route, ServerWebExchange exchange) {
		
		System.out.println(route.getId() + ", " + route.getUri() + ", " + route);
		System.out.println(exchange.getRequest());
		
		// 校验API订阅生成的AccessKey是否合法
		// 从HEAD中获取AccessKey
		
		String accessKey = exchange.getRequest().getHeaders().getFirst("AccessKey");
		URI requestUri = exchange.getRequest().getURI();
		if (null == accessKey) {
			throw new RuntimeException("Missing AccessKey in http request HEAD. [" + requestUri + "]");
		} else {
			// 调用AccessKey校验微服务API校验合法性
			// validateAccessKey(requestUri, accessKey);
		}
		System.out.println(accessKey);
		
		super.validateRoute(route, exchange);
	}

}
