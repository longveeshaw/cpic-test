package org.laotse.github.gateway.test.rx;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * map
 * 
 * @author ZhongWen
 *
 */
public class ObservableTest2 {

	public static void main(String[] args) throws InterruptedException {

		Observable.from(new String[]{"This", "is", "RxJava"})
			.map(e -> e.toUpperCase()).toList().map(s -> {
				System.out.println("[" + Thread.currentThread().getName() + "] reverse " + s);
				Collections.reverse(s);
				return s;
			}).observeOn(Schedulers.io()).subscribeOn(Schedulers.newThread()).subscribe(e -> {
				System.out.println("[" + Thread.currentThread().getName() + "] " + e);
			});

		for (int i = 0; i < 6000; i++) {
			TimeUnit.MILLISECONDS.sleep(1L);
		}
	}

}
