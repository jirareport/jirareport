package br.com.jiratorio.testlibrary.junit.testtype

import br.com.jiratorio.testlibrary.junit.extension.ArchUnitJunitExtension
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.extension.ExtendWith

@Tag("arch")
@ExtendWith(ArchUnitJunitExtension::class)
annotation class ArchTest
