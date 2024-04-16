package im.tretyakov.java;

import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Container implements Consumer<Long>, Supplier<Collection<Long>> {

    private final AtomicReference<Collection<Long>> value = new AtomicReference<>(new LinkedList<>());

    @Override
    public void accept(Long value) {
        this.value.getAndUpdate(v -> {
            v.add(value);
            return v;
        });
    }

    @Override
    public Collection<Long> get() {
        return value.getAndSet(new LinkedList<>());
    }
}
