/*
 * Copyright 2013-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.laotse.github.spc.test;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.factory.GatewayFilterFactory;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.core.Ordered;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Copy from GatewayControllerEndpoint
 * 
 */
@RestController
@RequestMapping("/gateway-apis")
public class APIs implements ApplicationEventPublisherAware {

	private static final Log log = LogFactory.getLog(APIs.class);

	private RouteDefinitionLocator routeDefinitionLocator;
	private List<GlobalFilter> globalFilters;
	@SuppressWarnings("rawtypes")
	private List<GatewayFilterFactory> gatewayFilters;
	private RouteDefinitionWriter routeDefinitionWriter;
	private RouteLocator routeLocator;
	private ApplicationEventPublisher publisher;

	@SuppressWarnings("rawtypes")
	public APIs(RouteDefinitionLocator routeDefinitionLocator, List<GlobalFilter> globalFilters,
									 List<GatewayFilterFactory> gatewayFilters, RouteDefinitionWriter routeDefinitionWriter,
									 RouteLocator routeLocator) {
		this.routeDefinitionLocator = routeDefinitionLocator;
		this.globalFilters = globalFilters;
		this.gatewayFilters = gatewayFilters;
		this.routeDefinitionWriter = routeDefinitionWriter; // InMemoryRouteDefinitionRepository
		this.routeLocator = routeLocator;
	}

	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher publisher) {
		this.publisher = publisher;
	}

	// TODO: Add uncommited or new but not active routes endpoint

	@GetMapping("/refresh")
	public Mono<Void> refresh() {
	    this.publisher.publishEvent(new RefreshRoutesEvent(this));
		return Mono.empty();
	}

	@GetMapping("/globalfilters")
	public Mono<HashMap<String, Object>> globalfilters() {
		return getNamesToOrders(this.globalFilters);
	}

	@GetMapping("/routefilters")
	public Mono<HashMap<String, Object>> routefilers() {
		return getNamesToOrders(this.gatewayFilters);
	}

	private <T> Mono<HashMap<String, Object>> getNamesToOrders(List<T> list) {
		return Flux.fromIterable(list).reduce(new HashMap<>(), this::putItem);
	}

	private HashMap<String, Object> putItem(HashMap<String, Object> map, Object o) {
		Integer order = null;
		if (o instanceof Ordered) {
			order = ((Ordered)o).getOrder();
		}
		//filters.put(o.getClass().getName(), order);
		map.put(o.toString(), order);
		return map;
	}

	// TODO: Flush out routes without a definition
	@GetMapping("/routes")
	public Mono<List<Map<String, Object>>> routes() {
		Mono<Map<String, RouteDefinition>> routeDefs = this.routeDefinitionLocator.getRouteDefinitions()
				.collectMap(RouteDefinition::getId);
		Mono<List<Route>> routes = this.routeLocator.getRoutes().collectList();
		return Mono.zip(routeDefs, routes).map(tuple -> {
			Map<String, RouteDefinition> defs = tuple.getT1();
			List<Route> routeList = tuple.getT2();
			List<Map<String, Object>> allRoutes = new ArrayList<>();

			routeList.forEach(route -> {
				HashMap<String, Object> r = new HashMap<>();
				r.put("route_id", route.getId());
				r.put("order", route.getOrder());

				if (defs.containsKey(route.getId())) {
					r.put("route_definition", defs.get(route.getId()));
				} else {
					HashMap<String, Object> obj = new HashMap<>();

					obj.put("predicate", route.getPredicate().toString());

					if (!route.getFilters().isEmpty()) {
						ArrayList<String> filters = new ArrayList<>();
						for (GatewayFilter filter : route.getFilters()) {
							filters.add(filter.toString());
						}

						obj.put("filters", filters);
					}

					if (!obj.isEmpty()) {
						r.put("route_object", obj);
					}
				}
				allRoutes.add(r);
			});

			return allRoutes;
		});
	}

/*
http POST :8080/admin/gateway/routes/apiaddreqhead uri=http://httpbin.org:80 predicates:='["Host=**.apiaddrequestheader.org", "Path=/headers"]' filters:='["AddRequestHeader=X-Request-ApiFoo, ApiBar"]'
*/
	@PostMapping("/routes/{id}")
	public Mono<ResponseEntity<Void>> save(@PathVariable String id, @RequestBody Mono<RouteDefinition> route) {
		//		return this.routeDefinitionWriter.save(route.map(r ->  {
		//			r.setId(id);
		//			log.info("Saving route: " + route);
		//			return r;
		//		})).then(Mono.defer(() ->
		//			Mono.just(ResponseEntity.created(URI.create("/routes/"+id)).build())
		//		));
		
		this.routeDefinitionWriter.save(route.map(r ->  {
			r.setId(id);
			log.info("Saving route: " + route);
			return r;
		})).subscribe(f -> {
			System.out.println(f);
		});
		return Mono.just(ResponseEntity.created(URI.create("/routes/"+id)).build());
	}

	@DeleteMapping("/routes/{id}")
	public Mono<ResponseEntity<Object>> delete(@PathVariable String id) {
		return this.routeDefinitionWriter.delete(Mono.just(id))
				.then(Mono.defer(() -> Mono.just(ResponseEntity.ok().build())))
				.onErrorResume(t -> t instanceof NotFoundException, t -> Mono.just(ResponseEntity.notFound().build()));
	}

	@GetMapping("/routes/{id}")
	public Mono<ResponseEntity<RouteDefinition>> route(@PathVariable String id) {
		//TODO: missing RouteLocator
		return this.routeDefinitionLocator.getRouteDefinitions()
				.filter(route -> route.getId().equals(id))
				.singleOrEmpty()
				.map(route -> ResponseEntity.ok(route))
				.switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
	}

	@GetMapping("/routes/{id}/combinedfilters")
	public Mono<HashMap<String, Object>> combinedfilters(@PathVariable String id) {
		//TODO: missing global filters
		return this.routeLocator.getRoutes()
				.filter(route -> route.getId().equals(id))
				.reduce(new HashMap<>(), this::putItem);
	}
	
	@Autowired
	RouteLocatorBuilder builder;
	
	@GetMapping("/test")
	public boolean test() {
		// uri=http://httpbin.org:80 predicates:='["Host=**.apiaddrequestheader.org", "Path=/headers"]' filters:='["AddRequestHeader=X-Request-ApiFoo, ApiBar"]'
		
		List<PredicateDefinition> predicates = new ArrayList<>();
		{
			PredicateDefinition predicate = new PredicateDefinition("Path=/s");
			predicates.add(predicate);
			// ...
		}	
		List<FilterDefinition> filters = new ArrayList<>();
			
		RouteDefinition definition = new RouteDefinition();
		//definition.setId("path_route_1"); // will be overwrite on save
		definition.setUri(URI.create("http://www.baidu.com"));
		definition.setOrder(Ordered.HIGHEST_PRECEDENCE);
		definition.setPredicates(predicates);
		definition.setFilters(filters);
		
		save("path_route_1", Mono.just(definition));
		
		this.publisher.publishEvent(new RefreshRoutesEvent(this));
		//this.publisher.publishEvent(new RefreshEvent(this, new RefreshRoutesEvent(this), "Refresh Route"));
		
		// routeLocator.getRoutes().then(route);
		// routeLocator.getRoutes().then(route).cache();
		
		// RouteLocator locator = builder.routes().route("path_route", r -> r.path("/dynaimc_route").uri("https://www.baidu.com")).build();
		
		// routeLocator.getRoutes().then(Mono.just(locator.getRoutes())).cache();
		
		
		return Boolean.TRUE;
	}
	
}
