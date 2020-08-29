package br.com.jiratorio.arch

import br.com.jiratorio.junit.testtype.ArchTest
import br.com.jiratorio.extension.areNotInnerClass
import com.tngtech.archunit.core.domain.JavaClasses
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition
import org.junit.jupiter.api.Test
import javax.persistence.Entity
import javax.persistence.MappedSuperclass

@ArchTest
class EntityArchTest {

    @Test
    fun `should be a entity`(classes: JavaClasses) =
        ArchRuleDefinition.classes()
            .that().areNotInnerClass().and().resideInAPackage("..domain.entity")
            .should().beAnnotatedWith(Entity::class.java)
            .orShould().beAnnotatedWith(MappedSuperclass::class.java)
            .check(classes)

    @Test
    fun `should not be accessed by usecase mapper repository or domain`(classes: JavaClasses) =
        ArchRuleDefinition.classes()
            .that().resideInAPackage("..entity..")
            .should().onlyBeAccessed().byAnyPackage("..usecase..", "..mapper..", "..repository..", "..domain..")
            .check(classes)
}
