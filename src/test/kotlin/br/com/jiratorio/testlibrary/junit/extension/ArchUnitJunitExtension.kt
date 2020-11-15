package br.com.jiratorio.testlibrary.junit.extension

import com.tngtech.archunit.core.domain.JavaClasses
import com.tngtech.archunit.core.importer.ClassFileImporter
import com.tngtech.archunit.core.importer.ImportOption
import com.tngtech.archunit.core.importer.ImportOptions
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.ParameterContext
import org.junit.jupiter.api.extension.ParameterResolver

class ArchUnitJunitExtension : ParameterResolver {

    override fun supportsParameter(parameterContext: ParameterContext, extensionContext: ExtensionContext): Boolean =
        parameterContext.parameter.type == JavaClasses::class.java

    override fun resolveParameter(parameterContext: ParameterContext, extensionContext: ExtensionContext): Any =
        ClassesToCheck.toJavaClasses()

    object ClassesToCheck {

        private val importOptions = ImportOptions()
            .with(ImportOption.DoNotIncludeArchives())
            .with(ImportOption.DoNotIncludeJars())
            .with(ImportOption.DoNotIncludeTests())

        private val javaClasses: JavaClasses = ClassFileImporter(importOptions)
            .importPackages("br.com.jiratorio")

        fun toJavaClasses() =
            javaClasses

    }

}
