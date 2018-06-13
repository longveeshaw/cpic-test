package org.laotse.github.reactive.streams;

import static org.testng.Assert.assertEquals;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.laotse.github.reactive.streams.ReactiveStreamsTest.AsyncSubscriber;
import org.reactivestreams.Subscriber;
import org.reactivestreams.example.unicast.NumberIterablePublisher;
import org.reactivestreams.tck.SubscriberBlackboxVerification;
import org.reactivestreams.tck.TestEnvironment;

// Must be here for TestNG to find and run this, do not remove
public class AsyncSubscriberTestCase extends SubscriberBlackboxVerification<Integer> {

	private static ExecutorService e;

	@BeforeClass
	public static void before() {
		e = Executors.newFixedThreadPool(4);
	}

	@AfterClass
	public static void after() {
		if (e != null)
			e.shutdown();
	}

	public AsyncSubscriberTestCase() {
		super(new TestEnvironment());
	}

	@Override
	public Subscriber<Integer> createSubscriber() {
		return new AsyncSubscriber<Integer>(e) {
			@Override
			protected boolean whenNext(final Integer element) {
				return true;
			}
		};
	}

	@Test
	public void testAccumulation() throws InterruptedException {

		final AtomicLong i = new AtomicLong(Long.MIN_VALUE);
		final CountDownLatch latch = new CountDownLatch(1);
		final Subscriber<Integer> sub = new AsyncSubscriber<Integer>(e) {
			private long acc;

			@Override
			protected boolean whenNext(final Integer element) {
				acc += element;
				return true;
			}

			@Override
			protected void whenComplete() {
				i.set(acc);
				latch.countDown();
				System.out.println(acc);
				System.out.println("Complete");
			}
		};

		new NumberIterablePublisher(0, 10, e).subscribe(sub);
		latch.await(env.defaultTimeoutMillis() * 10, TimeUnit.MILLISECONDS);
		assertEquals(i.get(), 45);
	}

	@Override
	public Integer createElement(int element) {
		return element;
	}

}
