package org.laotse.github.gateway.test.rx;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * switchIfEmpty
 * 
 * @author ZhongWen
 *
 */
public class ObservableTest6 {

	public static void main(String[] args) throws InterruptedException {

		Observable<String> ob = ((int)(Math.random() * 100) % 2 == 0) ? Observable.just("switchIfEmpty1") : Observable.empty();
		ob.switchIfEmpty(Observable.just("switchIfEmpty2")).subscribeOn(Schedulers.io())
				.observeOn(Schedulers.newThread()).subscribe(next -> {
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
