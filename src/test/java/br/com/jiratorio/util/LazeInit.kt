package br.com.jiratorio.util

inline fun <reified T> lazyInitBy(noinline initializer: () -> T): T {
    val aux by lazy(initializer)
    return aux
}
