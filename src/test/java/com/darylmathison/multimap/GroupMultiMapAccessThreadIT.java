package com.darylmathison.multimap;

import com.darylmathison.multimap.test.rule.HazelcastInstanceResource;
import com.hazelcast.core.IExecutorService;
import com.hazelcast.core.IdGenerator;
import org.junit.ClassRule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * GroupMultiMapAccessThread Integration Test
 */
public class GroupMultiMapAccessThreadIT {
    public static final int NUM_THREADS = 10;
    public static final String GEN_NAME = "groupIdGenerator";
    public static final String MAP_NAME = "groupMap";

    @ClassRule
    public static HazelcastInstanceResource hazelcastInstanceResource = new HazelcastInstanceResource();

    @Test
    public void testGroupMultiMapAccessThread() {
        List<GroupMultiMapAccessThread> threads = createThreads();
        IExecutorService service = hazelcastInstanceResource.getService();
        List<Future<?>> futures = new ArrayList<>(NUM_THREADS);
        for(GroupMultiMapAccessThread thread: threads) {
            futures.add(service.submit(thread));
        }

        for(Future<?> future: futures) {
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    private List<GroupMultiMapAccessThread> createThreads() {
        List<GroupMultiMapAccessThread> ret = new ArrayList<>(NUM_THREADS);
        IdGenerator gen = hazelcastInstanceResource.getInstance().getIdGenerator(GEN_NAME);
        Long groupId = gen.newId();
        for(int i = 0; i < NUM_THREADS; i++) {
            GroupMultiMapAccessThread thread = new GroupMultiMapAccessThread();
            thread.setMapName(MAP_NAME);
            thread.setGroupId(groupId);
            ret.add(thread);
        }

        return ret;
    }
}
