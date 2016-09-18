package io.github.kodido.process.orchestrator.api;

import org.springframework.hateoas.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class RootController {

    @RequestMapping( value = "/", method = GET, produces = MediaType.APPLICATION_JSON_VALUE )
    public Resource<String> index() {
        Resource<String> resource = new Resource<>("CoRE API v 1.0");
        resource.add(linkTo(methodOn(ProcessDefinitionController.class).getProcessDefinitions()).withRel("processes"));
        return resource;
    }

}
