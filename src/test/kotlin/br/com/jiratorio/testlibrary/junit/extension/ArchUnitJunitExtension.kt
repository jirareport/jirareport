package br.com.jiratorio.testlibrary.junit.extension

import com.tngtech.archunit.core.domain.JavaClasses
import com.tngtech.archunit.core.importer.ClassFileImporter
import com.tngtech.archunit.core.importer.ImportOption
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.ParameterContext
import org.junit.jupiter.api.extension.ParameterResolver

class ArchUnitJunitExtension : ParameterResolver {

    override fun supportsParameter(parameterContext: ParameterContext, extensionContext: ExtensionContext): Boolean =
        parameterContext.parameter.type == JavaClasses::class.java

    override fun resolveParameter(parameterContext: ParameterContext, extensionContext: ExtensionContext): Any =
        ClassesToCheck.toJavaClasses()

    object ClassesToCheck {

        private val javaClasses: JavaClasses = ClassFileImporter(
            listOf(
                ImportOption.Predefined.DO_NOT_INCLUDE_ARCHIVES,
                ImportOption.Predefined.DO_NOT_INCLUDE_JARS,
                ImportOption.Predefined.DO_NOT_INCLUDE_TESTS
            )
        ).importPackages("br.com.jiratorio")

        fun toJavaClasses() =
            javaClasses

    }

}
