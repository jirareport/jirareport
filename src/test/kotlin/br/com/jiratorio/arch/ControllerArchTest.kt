package br.com.jiratorio.arch

import br.com.jiratorio.config.junit.testtype.ArchTest
import com.tngtech.archunit.core.domain.JavaClasses
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition
import org.junit.jupiter.api.Test
import org.springframework.web.bind.annotation.RestController

@ArchTest
class ControllerArchTest {

    @Test
    fun `should end with controller`(classes: JavaClasses) =
        ArchRuleDefinition.classes()
            .that().resideInAPackage("..controller..")
            .should().haveSimpleNameEndingWith("Controller")
            .check(classes)

    @Test
    fun `should be annotated with controller`(classes: JavaClasses) =
        ArchRuleDefinition.classes()
            .that().resideInAPackage("..controller..")
            .should().beAnnotatedWith(RestController::class.java)
            .check(classes)

    @Test
    fun `should not access repository`(classes: JavaClasses) =
        ArchRuleDefinition.noClasses()
            .that().resideInAPackage("..controller..")
            .should().accessClassesThat().resideInAPackage("..repository..")
            .check(classes)

    @Test
    fun `should not access client`(classes: JavaClasses) =
        ArchRuleDefinition.noClasses()
            .that().resideInAPackage("..controller..")
            .should().accessClassesThat().resideInAPackage("..client..")
            .check(classes)

}
