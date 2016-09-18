package io.github.kodido.process.orchestrator.api;

import io.github.kodido.process.orchestrator.api.resource.ResourceAssembler;
import io.github.kodido.process.orchestrator.model.Process;
import io.github.kodido.process.orchestrator.model.ProcessDefinition;
import io.github.kodido.process.orchestrator.services.ProcessDefinitionService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

@RequestMapping(value = "/api/processes", produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
public class ProcessDefinitionController {

    @Autowired
    RuntimeService runtimeService;

    @Autowired
    ProcessDefinitionService processDefinitionService;

    @Autowired
    ResourceAssembler<ProcessDefinition> resourceAssembler;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @RequestMapping(value="/", method= RequestMethod.GET)
    public HttpEntity<List<ProcessDefinition>> getProcessDefinitions() {
        final List<ProcessDefinition> c4REProcessDefinitions = processDefinitionService.listProcesses();

        final List<ProcessDefinition> resources = c4REProcessDefinitions.stream()
                .map(this::createResource)
                .collect(Collectors.toList());
        return new ResponseEntity<>(resources, HttpStatus.OK);
    }

    private ProcessDefinitionController self() {
        return methodOn(this.getClass());
    }

    @RequestMapping(value="/{id}", method= RequestMethod.GET)
    public HttpEntity<ProcessDefinition> getProcessDefinition(@PathVariable String id) {
        ProcessDefinition definition = processDefinitionService.getProcessById(id)
                .orElseGet(() -> {throw new ResourceNotFoundException("No process definition with id" + id);});
        return new ResponseEntity<>(createResource(definition), HttpStatus.OK) ;
    }

    @RequestMapping(value="/{id}/start", method= RequestMethod.POST)
    public String startProcess(@PathVariable String id) {
        String procId = runtimeService.startProcessInstanceByKey(id).getId();
        logger.info("Started process " + procId);
        return procId;
    }

    @RequestMapping(value="/{id}/instances", method= RequestMethod.GET)
    public List<Process> getProcessInstancesByProcessId(@PathVariable String id) {
        final List<ProcessInstance> processInstances = runtimeService.createProcessInstanceQuery().processDefinitionKey(id).list();
        return processInstances.stream().map(mapProcess).collect(Collectors.toList());
    }

    Function<ProcessInstance, Process> mapProcess = processInstance -> {
        return new Process(processInstance.getId());
    };


    private ProcessDefinition createResource(ProcessDefinition resource) {
        String id = resource.getProcessId();
        resource.add(linkTo(self().getProcessDefinition(id)).withSelfRel());
        resource.add(linkTo(self().getProcessInstancesByProcessId(id)).withRel("instances"));
        return resource;
    }

}
