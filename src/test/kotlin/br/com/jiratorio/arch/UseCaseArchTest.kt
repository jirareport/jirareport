package br.com.jiratorio.arch

import br.com.jiratorio.config.junit.testtype.ArchTest
import br.com.jiratorio.config.stereotype.UseCase
import br.com.jiratorio.extension.areNotInnerClass
import com.tngtech.archunit.core.domain.JavaClasses
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition
import org.junit.jupiter.api.Test
import org.springframework.stereotype.Service

@ArchTest
class UseCaseArchTest {

    @Test
    fun `should not access controller`(classes: JavaClasses) =
        ArchRuleDefinition.noClasses()
            .that().resideInAPackage("..usecase..")
            .should().accessClassesThat().resideInAPackage("..controller..")
            .check(classes)

    @Test
    fun `should be annotated with @UseCase`(classes: JavaClasses) =
        ArchRuleDefinition.classes()
            .that().areNotInnerClass().and().resideInAPackage("..usecase..")
            .should().beAnnotatedWith(UseCase::class.java)
            .check(classes)

    @Test
    fun `should not be annotated with @Service`(classes: JavaClasses) =
        ArchRuleDefinition.noClasses()
            .that().resideInAPackage("..usecase..")
            .should().beAnnotatedWith(Service::class.java)
            .check(classes)

    @Test
    fun `should not have name ending with UseCase`(classes: JavaClasses) =
        ArchRuleDefinition.noClasses()
            .that().resideInAPackage("..usecase..")
            .should().haveSimpleNameEndingWith("UseCase")
            .check(classes)

    @Test
    fun `classes that are annotated with @UseCase should reside in usecase package`(classes: JavaClasses) =
        ArchRuleDefinition.classes()
            .that().areAnnotatedWith(UseCase::class.java)
            .should().resideInAPackage("..usecase..")
            .check(classes)

}
