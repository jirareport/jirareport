package br.com.jiratorio.factory

inline fun <reified T> lazyInitBy(noinline initializer: () -> T): T {
    val aux by lazy(initializer)
    return aux
}
