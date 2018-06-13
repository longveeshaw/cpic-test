package org.laotse.github.reactor.programming;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import reactor.core.publisher.Flux;

public class TestCase1 {

	public static void main(String[] args) throws Exception {
		List<String> words = Arrays.asList("the", "quick", "brown", "fox", "jumped", "over", "the", "lazy", "dog");
		
		Flux<String> flux1 = Flux.fromIterable(words).map(e -> e + ", ");
		Flux<String> flux2 = Flux.just("java", "reactor", "programming").map(e -> e + ", ");
		
		Flux<String> flux3 = Flux.fromArray(new String[] { "java", "reactor", "programming" });
		Flux<String> flux4 = Flux.fromStream(Stream.of("java", "reactor", "programming"));
		
		flux1.subscribe(System.out :: print);
		System.out.println();
		flux2.subscribe(System.out :: print);
		System.out.println();
		// flux3.subscribe(System.out :: println);
		// flux4.subscribe(System.out :: println);
		
		
		Flux<String> flux5 = flux3.map(e -> e + " ").concatWithValues("<End>");
		flux5.subscribe(System.out :: print);
		System.out.println();
		
		
		Flux<String> flux6 = flux4.map(e -> e + " ").concatWithValues("<OK>").delaySubscription(Duration.ofSeconds(1L));
		flux6.subscribe(System.out :: print);
		System.out.println();
		
		System.out.println("Last");
		
		int i = 0;
		while (i++ < 10) {
			TimeUnit.SECONDS.sleep(1L);
		}
	}

}
