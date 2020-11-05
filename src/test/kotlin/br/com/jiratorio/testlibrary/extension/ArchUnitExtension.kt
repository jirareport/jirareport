package br.com.jiratorio.testlibrary.extension

import com.tngtech.archunit.base.DescribedPredicate
import com.tngtech.archunit.core.domain.JavaClass
import com.tngtech.archunit.core.domain.JavaModifier
import com.tngtech.archunit.lang.syntax.elements.ClassesThat
import com.tngtech.archunit.lang.syntax.elements.GivenMethodsConjunction
import com.tngtech.archunit.lang.syntax.elements.MethodsThat

fun <CONJUNCTION> ClassesThat<CONJUNCTION>.areNotInnerClass(): CONJUNCTION =
    areNotAssignableTo(AreInnerClass)

private object AreInnerClass : DescribedPredicate<JavaClass>("inner class") {

    override fun apply(input: JavaClass): Boolean =
        input.isInnerClass || input.name.contains("$")

}

fun <CONJUNCTION : GivenMethodsConjunction> MethodsThat<CONJUNCTION>.areNotSynthetic(): CONJUNCTION =
    this.doNotHaveModifier(JavaModifier.SYNTHETIC)
