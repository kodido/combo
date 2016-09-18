package io.github.kodido.process.app;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
@ComponentScan( { "io.github.kodido.process.orchestrator.api", "org.activiti.rest" } )
public class WebConfig {

}
