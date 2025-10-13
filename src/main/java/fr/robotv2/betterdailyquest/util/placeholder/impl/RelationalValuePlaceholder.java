package fr.robotv2.betterdailyquest.util.placeholder.impl;

@FunctionalInterface
public interface RelationalValuePlaceholder<A, B> {
    String apply(String text, A fst, B snd);
}
