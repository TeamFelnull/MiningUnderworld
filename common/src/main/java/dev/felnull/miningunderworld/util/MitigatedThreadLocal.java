package dev.felnull.miningunderworld.util;

import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

/**
 * Mixinで変数を利用する場合に複数のスレッドがアクセスする可能性がある場合に使用</br>
 * JavaのThreadLocalがスレッドが生きてる間GCされない問題を回避</br>
 * GCされると思われない場所での利用はJavaのThreadLocalを利用することを推奨
 *
 * @param <T>
 * @author MORIMORI0317
 */
public class MitigatedThreadLocal<T> {
    private final Map<Thread, T> values = new ConcurrentHashMap<>();
    private final Supplier<T> initValueFactory;

    private MitigatedThreadLocal(Supplier<T> initValueFactory) {
        this.initValueFactory = initValueFactory;
    }

    public static <T> MitigatedThreadLocal<T> newMitigatedThreadLocal() {
        return new MitigatedThreadLocal<>(null);
    }

    public static <T> MitigatedThreadLocal<T> newMitigatedThreadLocal(@Nullable Supplier<T> initValueFactory) {
        return new MitigatedThreadLocal<>(initValueFactory);
    }

    public T get() {
        if (initValueFactory == null)
            return values.get(Thread.currentThread());

        return values.computeIfAbsent(Thread.currentThread(), key -> initValueFactory.get());
    }

    public void set(T newValue) {
        values.put(Thread.currentThread(), newValue);
    }
}
