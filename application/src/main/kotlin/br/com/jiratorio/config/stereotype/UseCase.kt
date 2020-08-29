package br.com.jiratorio.config.stereotype

import org.springframework.stereotype.Service

@Service
@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.CLASS)
annotation class UseCase
