package br.com.leonardoferreira.jirareport.context;

import br.com.leonardoferreira.jirareport.TestConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(TestConfig.class)
@ComponentScan("br.com.leonardoferreira.jirareport.factory")
public class UnitTestContext {
}
