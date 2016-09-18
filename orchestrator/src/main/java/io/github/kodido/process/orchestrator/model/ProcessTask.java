package io.github.kodido.process.orchestrator.model;

import org.springframework.hateoas.ResourceSupport;

public class ProcessTask extends ResourceSupport{
    private String taskId;

    public String getTaskId() {
        return taskId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getPriority() {
        return priority;
    }

    public String getAssignee() {
        return assignee;
    }

    private String name;
    private String description;
    private int priority;
    private String assignee;

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }
}
