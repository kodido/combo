package io.github.kodido.process.app;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
@ComponentScan( basePackages = { "io.github.kodido.process" }, excludeFilters = {
        @ComponentScan.Filter( type = FilterType.ANNOTATION, value = EnableWebMvc.class ),
        @ComponentScan.Filter( type = FilterType.REGEX, pattern = "github\\.kodido\\.process\\.orchestrator\\.api.*" ) } )
public class ServiceConfig {
}

