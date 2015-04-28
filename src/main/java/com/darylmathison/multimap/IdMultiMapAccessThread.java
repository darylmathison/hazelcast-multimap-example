package com.darylmathison.multimap;

/**
 * This thread accesses only one "slot" in a multimap.
 */
public class IdMultiMapAccessThread extends MultiMapAccessThread {
    private Long id;

    public void run() {
        l.lock();
        boolean shouldRun = (map != null && id != null);
        l.unlock();
        if(shouldRun) {
            map.lock(id);
            try {
                for (long i = 0; i < 10; i++) {
                    map.put(id, i);
                }
            } finally {
                map.unlock(id);
            }
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
