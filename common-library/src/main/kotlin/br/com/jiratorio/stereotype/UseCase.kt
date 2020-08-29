package br.com.jiratorio.stereotype

import org.springframework.stereotype.Service

@Service
@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.CLASS)
annotation class UseCase
