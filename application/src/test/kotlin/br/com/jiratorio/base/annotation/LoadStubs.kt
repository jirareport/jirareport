package br.com.jiratorio.base.annotation

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
annotation class LoadStubs(val value: Array<String>)
