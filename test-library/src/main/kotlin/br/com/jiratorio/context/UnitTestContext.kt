package br.com.jiratorio.context

import br.com.jiratorio.FakerConfig
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
@Import(FakerConfig::class)
@ComponentScan("br.com.jiratorio.factory")
class UnitTestContext
