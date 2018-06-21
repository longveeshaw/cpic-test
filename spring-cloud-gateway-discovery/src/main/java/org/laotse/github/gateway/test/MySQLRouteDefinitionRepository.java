package org.laotse.github.gateway.test;

import org.springframework.cloud.gateway.route.InMemoryRouteDefinitionRepository;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.stereotype.Component;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class MySQLRouteDefinitionRepository extends InMemoryRouteDefinitionRepository {

	@Override
	public Mono<Void> save(Mono<RouteDefinition> route) {
		// TODO 保存到数据库
		return super.save(route); // 缓存
	}

	@Override
	public Mono<Void> delete(Mono<String> routeId) {
		// TODO 数据库删除
		return super.delete(routeId); // 缓存
	}

	@Override
	public Flux<RouteDefinition> getRouteDefinitions() {
		// TODO 本地缓存同步数据库
		return super.getRouteDefinitions(); // 缓存
	}

}
