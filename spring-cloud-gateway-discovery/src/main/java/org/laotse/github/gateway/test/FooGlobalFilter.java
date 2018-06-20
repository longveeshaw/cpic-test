package org.laotse.github.gateway.test;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Component
public class FooGlobalFilter implements GlobalFilter, Ordered {

	@Override
	public int getOrder() {
		return LOWEST_PRECEDENCE;
	}

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		
		ServerHttpRequest request = exchange.getRequest();
		String method = request.getMethodValue();
		
		
		System.out.println("[" + method + "] " + request.getPath().value() + " " + request.getPath().toString() + " " + request.getURI() + " " + request.getRemoteAddress().getHostString());
		
		chain.filter(exchange);
		return Mono.empty();
	}

}
