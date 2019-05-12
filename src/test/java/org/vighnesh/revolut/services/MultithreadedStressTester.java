package org.vighnesh.revolut.services;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class MultithreadedStressTester {

    private final ExecutorService executor;
    private final int threadCount;


    MultithreadedStressTester(int threadCount) {
        this.threadCount = threadCount;
        this.executor = Executors.newCachedThreadPool();
    }

    void stress(final Runnable action) throws InterruptedException {
        spawnThreads(action).await();
    }

    private CountDownLatch spawnThreads(final Runnable action) {
        final CountDownLatch finished = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executor.execute(() -> {
                try {
                    action.run();
                } finally {
                    finished.countDown();
                }
            });
        }
        return finished;
    }


    void shutdown() {
        executor.shutdown();
    }
}