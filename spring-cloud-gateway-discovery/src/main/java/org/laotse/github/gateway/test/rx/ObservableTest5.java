package org.laotse.github.gateway.test.rx;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * concatMap
 * 
 * @author ZhongWen
 *
 */
public class ObservableTest5 {

	public static void main(String[] args) throws InterruptedException {

		Observable.from(new String[] { "This", "is", "RxJava" }).concatMap(e -> Observable.create(subscriber -> {
			subscriber.onNext(e.toUpperCase());
			subscriber.onCompleted();
			// subscriber.onError(new Exception("Sorry, ..."));
		})).subscribeOn(Schedulers.io()).observeOn(Schedulers.newThread()).subscribe(next -> {
			System.out.println("[" + Thread.currentThread().getName() + "] " + next);
		}, error -> {
			System.out.println("[" + Thread.currentThread().getName() + "] [ERROR] " + error);
		}, () -> {
			System.out.println("[" + Thread.currentThread().getName() + "] Done");
		});

		for (int i = 0; i < 6000; i++) {
			TimeUnit.MILLISECONDS.sleep(1L);
		}
	}

}
