package org.laotse.github.gateway.test.rx.hystrix;

import java.util.concurrent.Future;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;

import rx.Observable;

public class CommandHelloWorld extends HystrixCommand<String> {

	private final String name;

	protected CommandHelloWorld(String name) {
		super(HystrixCommandGroupKey.Factory.asKey("ExampleGroup"));
		this.name = name;
	}

	@Override
	protected String run() throws Exception {
		return "Hello " + name + " !";
	}

	public static void main(String[] args) throws Exception {
		CommandHelloWorld cmd = new CommandHelloWorld("Bob");
		Future<String> s = cmd.queue();
		System.out.println(s.get());
		
		Observable<String> x = new CommandHelloWorld("Bob").observe();
		System.out.println(x.subscribe(t -> {
			System.out.println(t);
		}));
	}

}
