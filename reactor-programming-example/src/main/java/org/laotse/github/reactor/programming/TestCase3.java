package org.laotse.github.reactor.programming;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import reactor.core.publisher.Mono;

public class TestCase3 {
	
	private static Map<String, Foo> fooes = Collections.synchronizedMap(new LinkedHashMap<String, Foo>());
	
	public static void main(String[] args) throws Exception {
		
		Mono.just(new Foo("100001", "Bar")).flatMap(f -> {
			System.out.println("Why not exec ?");
			fooes.put(f.getId(), f);
			return Mono.empty();
		}).subscribe(f -> {
			System.out.println(f);
		});
		
		Mono.just(new Foo("100002", "Bar")).map(f -> {
			System.out.println("Why not exec ?");
			fooes.put(f.getId(), f);
			return Boolean.TRUE;
		});
		
		Mono.just(new Foo("100002", "Bar")).map(f -> {
			System.out.println("Why not exec ?");
			fooes.put(f.getId(), f);
			return Boolean.TRUE;
		});
		
		System.out.println(fooes);
		System.out.println(fooes.size());
		
		int i = 0;
		while (i++ < 10) {
			TimeUnit.SECONDS.sleep(1L);
		}
	
		System.out.println(fooes);
		System.out.println(fooes.size());
	}

	public static class Foo {
		
		private String id;
		
		private String bar;

		public Foo() {
			super();
		}

		public Foo(String id, String bar) {
			super();
			this.id = id;
			this.bar = bar;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getBar() {
			return bar;
		}

		public void setBar(String bar) {
			this.bar = bar;
		}

		@Override
		public String toString() {
			return "Foo [id=" + id + ", bar=" + bar + "]";
		}
		
	}
	
}
