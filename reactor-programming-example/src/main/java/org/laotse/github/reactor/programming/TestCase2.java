package org.laotse.github.reactor.programming;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import reactor.core.publisher.Mono;

public class TestCase2 {
	
	public static void main(String[] args) throws Exception {
		Mono<Integer> mono = Mono.just(Integer.MAX_VALUE);
		mono.subscribe(System.out :: println);
		
		Mono<Long> mono1 = Mono.just(Long.MAX_VALUE).delaySubscription(Duration.ofSeconds(1L));
		mono1.subscribe(System.out :: println);
		
		int i = 0;
		while (i++ < 10) {
			TimeUnit.SECONDS.sleep(1L);
		}
	}

}
