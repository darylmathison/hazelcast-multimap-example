package com.darylmathison.multimap;

import com.darylmathison.multimap.test.rule.HazelcastInstanceResource;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IExecutorService;
import com.hazelcast.core.IdGenerator;
import org.junit.ClassRule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Integration test for IdMultiMapAccessThread
 */
public class IdMultiMapAccessThreadIT {

    public static final String MAP_NAME = "idAccessMap";
    public static final String GEN_NAME = "singleAccess";
    public static final int NUM_THREADS = 10;

    @ClassRule
    public static HazelcastInstanceResource hazelcastInstanceResource = new HazelcastInstanceResource();

    @Test
    public void testIdThreads() {
        List<IdMultiMapAccessThread> threads = generateThreads(hazelcastInstanceResource.getInstance());
        List<Future<?>> futures = new ArrayList<>(NUM_THREADS);
        IExecutorService spinner = hazelcastInstanceResource.getService();
        for(IdMultiMapAccessThread thread: threads) {
            futures.add(spinner.submit(thread));
        }

        for(Future<?> future: futures) {
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    private List<IdMultiMapAccessThread> generateThreads(HazelcastInstance instance) {
        IdGenerator gen = instance.getIdGenerator(GEN_NAME);
        List<IdMultiMapAccessThread> threads = new ArrayList<>(NUM_THREADS);
        for(int i = 0; i < NUM_THREADS; i++) {
            IdMultiMapAccessThread thread = new IdMultiMapAccessThread();
            thread.setMapName(MAP_NAME);
            thread.setId(gen.newId());
            threads.add(thread);
        }

        return threads;
    }
}
