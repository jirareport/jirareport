package br.com.leonardoferreira.jirareport.helper;

/**
 * @author lferreira
 * @since 09/08/18 16:13
 */
public interface Helper {

    String getName();

    default boolean isCacheable() {
        return true;
    }
}
