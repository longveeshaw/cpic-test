package org.laotse.github.gateway.test.rx;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * create
 * 
 * @author ZhongWen
 *
 */
public class ObservableTest {

	public static void main(String[] args) throws InterruptedException {

		Observable.create((subscriber) -> {
			for (int i = 0; i < 5; i++) {
				System.out.println("[" + Thread.currentThread().getName() + "] Emit " + i);
				subscriber.onNext(i);
				if (i == 2) {
					subscriber.onCompleted();
				}
			}
		}).observeOn(Schedulers.io()) // 设置生产者所在线程
				.subscribeOn(Schedulers.newThread()) // 设置消费者所在线程
				.subscribe((s) -> {
					System.out.println("[" + Thread.currentThread().getName() + "] Handle Action " + s);
				});

		for (int i = 0; i < 60000; i++) {
			TimeUnit.MILLISECONDS.sleep(1L);
		}
	}

}
