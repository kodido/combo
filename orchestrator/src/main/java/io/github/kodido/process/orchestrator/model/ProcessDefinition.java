package io.github.kodido.process.orchestrator.model;

import org.springframework.hateoas.ResourceSupport;

public class ProcessDefinition extends ResourceSupport {
    private final String id;
    private final String name;
    private final String description;

    public String getProcessId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public ProcessDefinition(String id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }
}
