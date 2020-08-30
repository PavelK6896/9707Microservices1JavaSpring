package app.web.pavelk.microservices1.сapture1.util;

import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

// from https://github.com/google/guava/blob/master/guava/src/com/google/common/util/concurrent/ThreadFactoryBuilder.java
public final class ThreadFactoryBuilder { //фабрика потоков
    private Boolean daemon = null;
    private ThreadFactory backingThreadFactory = null;
    private String nameFormat;


    public ThreadFactoryBuilder() {
    }

    public ThreadFactoryBuilder setNameFormat(String nameFormat) {
        this.nameFormat = nameFormat;
        return this;
    }

    public ThreadFactoryBuilder setDaemon(boolean daemon) {
        this.daemon = daemon;
        return this;
    }

    public ThreadFactory build() {
        return doBuild(this);
    }

    private static ThreadFactory doBuild(ThreadFactoryBuilder builder) {
        final String nameFormat = builder.nameFormat;
        final Boolean daemon = builder.daemon;
        final ThreadFactory backingThreadFactory =
                (builder.backingThreadFactory != null)
                        ? builder.backingThreadFactory
                        : Executors.defaultThreadFactory();
        final AtomicLong count = (nameFormat != null) ? new AtomicLong(0) : null;
        return new ThreadFactory() {
            @Override
            public Thread newThread(Runnable runnable) {
                Thread thread = backingThreadFactory.newThread(runnable);
                if (nameFormat != null) {
                    thread.setName(format(nameFormat, count.getAndIncrement()));
                }
                if (daemon != null) {
                    thread.setDaemon(daemon);
                }

                return thread;
            }
        };
    }

    private static String format(String format, Object... args) {
        return String.format(Locale.ROOT, format, args);
    }
}

