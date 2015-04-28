package com.darylmathison.multimap;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceAware;
import com.hazelcast.core.MultiMap;

import java.io.Serializable;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Abstract class to access MultiMap.
 */
public abstract class MultiMapAccessThread implements Serializable, Runnable, HazelcastInstanceAware {
    protected com.hazelcast.core.HazelcastInstance instance;
    protected MultiMap<Long, Long> map;
    protected String mapName;
    protected Lock l = new ReentrantLock();

    public void setHazelcastInstance(HazelcastInstance instance) {
        l.lock();
        try {
            this.instance = instance;
            if (mapName != null && !mapName.isEmpty()) {
                map = instance.getMultiMap(mapName);
            }
        } finally {
            l.unlock();
        }
    }

    public String getMapName() {
        return mapName;
    }

    public void setMapName(String mapName) {
        l.lock();
        try {
            this.mapName = mapName;
        } finally {
            l.unlock();
        }
    }
}
