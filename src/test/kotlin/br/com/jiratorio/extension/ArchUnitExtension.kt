package br.com.jiratorio.extension

import com.tngtech.archunit.base.DescribedPredicate
import com.tngtech.archunit.core.domain.JavaClass
import com.tngtech.archunit.lang.syntax.elements.ClassesThat

fun <CONJUNCTION> ClassesThat<CONJUNCTION>.areNotInnerClass(): CONJUNCTION =
    areNotAssignableTo(AreInnerClass)

private object AreInnerClass : DescribedPredicate<JavaClass>("inner class") {

    override fun apply(input: JavaClass): Boolean =
        input.isInnerClass || input.name.contains("$")

}
