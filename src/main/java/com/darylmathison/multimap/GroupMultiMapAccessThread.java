package com.darylmathison.multimap;

/**
 * Thread designed to share the same "slot" on a MultiMap.
 */
public class GroupMultiMapAccessThread extends MultiMapAccessThread {

    private static final long MAX = 10;

    private Long groupId;

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        l.lock();
        boolean shouldRun = (groupId != null && map != null);
        l.unlock();
        if(shouldRun) {
            map.lock(groupId);
            try {
                if (map.get(groupId).isEmpty()) {
                    System.out.println("adding to list");
                    for (long i = 0; i < MAX; i++) {
                        map.put(groupId, i);
                    }
                } else {
                    System.out.println("nothing to add");
                }
            } finally {
                map.unlock(groupId);
            }
        }
    }

    public void setGroupId(Long groupId) {
        l.lock();
        this.groupId = groupId;
        l.unlock();
    }
}
