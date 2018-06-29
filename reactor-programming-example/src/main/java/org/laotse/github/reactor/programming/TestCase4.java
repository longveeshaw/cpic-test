package org.laotse.github.reactor.programming;

import reactor.core.publisher.Flux;

public class TestCase4 {
	
	public static void main(String[] args) {
		Flux.just("event1", "event2", "event3").subscribe(event -> {
			System.out.println(event);
			if ("event3".equals(event)) {
				System.out.println("Done.");
			}
		});
		
	}

}
