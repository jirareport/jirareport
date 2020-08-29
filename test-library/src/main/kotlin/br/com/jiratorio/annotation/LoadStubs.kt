package br.com.jiratorio.annotation

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
annotation class LoadStubs(val value: Array<String>)
