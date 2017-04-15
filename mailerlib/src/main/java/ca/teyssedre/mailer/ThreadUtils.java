package ca.teyssedre.mailer;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadUtils {

    private static ExecutorService newThreadPoolNamed(String named) {
        NamedThreadFactory tf = new NamedThreadFactory(named);
        int i = Runtime.getRuntime().availableProcessors();
        return new ThreadPoolExecutor(i, i, 10L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(), tf);
    }

    private static Map<String, ExecutorService> executors = new HashMap<>();
    private static ExecutorService defaultExecutor = newThreadPoolNamed("MAIL-");
    private static Handler mainThread = new Handler(Looper.getMainLooper());

    public static void runOnBackground(Runnable runnable) {
        defaultExecutor.execute(runnable);
    }

    public static void runOnUI(Runnable runnable) {
        mainThread.post(runnable);
    }

    static void runOnPool(Runnable runnable, String pool) {
        getPoolNamed(pool).execute(runnable);
    }

    private static ExecutorService getPoolNamed(String pool) {
        ExecutorService executor = executors.get(pool);
        if (executor == null) {
            executor = newThreadPoolNamed(pool);
            executors.put(pool, executor);
        }
        return executor;
    }

    static <V> Future<V> submit(Callable<V> callable) {
        return defaultExecutor.submit(callable);
    }

    static <V> Future<V> submitOnPool(Callable<V> callable, String pool) {
        return getPoolNamed(pool).submit(callable);
    }

    private static class NamedThreadFactory implements ThreadFactory {
        private final ThreadGroup group;
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String namePrefix;

        NamedThreadFactory(String namePrefix) {
            SecurityManager s = System.getSecurityManager();
            group = (s != null) ? s.getThreadGroup() :
                    Thread.currentThread().getThreadGroup();
            this.namePrefix = namePrefix;
        }

        public Thread newThread(@NonNull Runnable r) {
            Thread t = new Thread(group, r,
                    namePrefix + threadNumber.getAndIncrement(), 0);
            if (t.isDaemon()) t.setDaemon(false);
            if (t.getPriority() != Thread.NORM_PRIORITY) {
                t.setPriority(Thread.NORM_PRIORITY);
            }
            return t;
        }
    }


}
