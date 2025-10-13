package fr.robotv2.betterdailyquest.util;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public class Futures {

    public static <T> CompletableFuture<Void> ofAll(Collection<CompletableFuture<T>> futures) {
        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
    }

}
