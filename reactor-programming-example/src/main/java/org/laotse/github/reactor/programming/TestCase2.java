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
		
		new Thread(new PublisherTask()).start();
		
		// lambda
	}
	
	public static class PublisherTask implements Runnable {
		
		@Override
		public void run() {
			while (Boolean.TRUE) {
				System.out.print(".");
				try {
					TimeUnit.SECONDS.sleep(3L);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
	}

}
