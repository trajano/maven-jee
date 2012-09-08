package net.trajano.servicebus.master.internal;

import java.util.concurrent.Callable;

import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.util.Duration;

/**
 * This wraps a {@link Future} and {@link Await} so that it can be used with the
 * standard Java concurrency libraries.
 * 
 * @author trajano
 * 
 * @param <T>
 */
public class CallableScalaFuture<T> implements Callable<T> {
	private final Future<T> scalaFuture;

	public CallableScalaFuture(final Future<T> scalaFuture) {
		this.scalaFuture = scalaFuture;
	}

	@Override
	public T call() throws Exception {
		return Await.result(scalaFuture, Duration.Inf());
	}

}