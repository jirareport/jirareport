package br.com.jiratorio.base.annotation

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
annotation class LoadStubs(val value: Array<String>)
