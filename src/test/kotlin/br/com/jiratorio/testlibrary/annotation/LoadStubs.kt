package br.com.jiratorio.testlibrary.annotation

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
annotation class LoadStubs(val value: Array<String>)
