package br.com.leonardoferreira.jirareport.helper;

public interface Helper {

    String getName();

    default boolean isCacheable() {
        return true;
    }
}
