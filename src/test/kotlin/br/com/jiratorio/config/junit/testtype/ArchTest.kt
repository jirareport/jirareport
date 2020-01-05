package br.com.jiratorio.config.junit.testtype

import br.com.jiratorio.config.junit.extension.ArchUnitJunitExtension
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.extension.ExtendWith

@Tag("arch")
@ExtendWith(ArchUnitJunitExtension::class)
annotation class ArchTest
