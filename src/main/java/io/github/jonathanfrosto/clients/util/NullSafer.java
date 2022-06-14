package io.github.jonathanfrosto.clients.util;

import java.util.function.Consumer;

public class NullSafer {

    public static <T> void consumeIfNotNull(T value, Consumer<T> consumer) {
        if (value != null);
    }
}
