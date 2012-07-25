package net.trajano.clustering.test;

import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import net.trajano.clustering.PiApproximation;
import net.trajano.clustering.internal.HazelcastMaster;

import org.junit.Assert;
import org.junit.Test;

import com.hazelcast.core.Hazelcast;

public class HazelcastTest {
	@Test
	public void test() throws Exception {
		ExecutorService executor = Hazelcast.getExecutorService(UUID
				.randomUUID().toString());

		final HazelcastMaster master = new HazelcastMaster(10000, 10000);

		Future<PiApproximation> result = executor.submit(master);
		Assert.assertEquals(Math.PI, result.get().getPi(), 0.0001);
	}
}
