package io.github.EfficiencAI.Assert;

import java.util.Collection;

public class Assert {
    public static void notNull(Object object) {
        if (object == null) {
            throw new NullPointerException();
        }
    }

    public static void notNull(Object object, String message) {
        if (object == null) {
            throw new NullPointerException(message);
        }
    }

    public static void notNull(Object object, Runnable runnable) {
        if (object == null) {
            runnable.run();
        }
    }
}
