package io.github.kodido.process.orchestrator.api;

import io.github.kodido.process.orchestrator.model.ProcessTask;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
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
import java.util.stream.Collectors;

@RequestMapping(value = "/api/instances", produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
public class ProcessController {

    @Autowired
    private TaskService taskService;

    @RequestMapping(value="/{id}/tasks", method= RequestMethod.GET)
    public HttpEntity<List<ProcessTask>> getProcessTasks(@PathVariable String id) {
        final List<Task> taskList = taskService.createTaskQuery().processInstanceId(id).list();

        final List<ProcessTask> taskResources = taskList.stream().map(this::taskResource).collect(Collectors.toList());

        return new ResponseEntity<>(taskResources, HttpStatus.OK);
    }

    private ProcessTask taskResource(Task task) {
        ProcessTask processTask = new ProcessTask();
        processTask.setTaskId(task.getId());
        processTask.setName(task.getName());
        processTask.setDescription(task.getDescription());
        processTask.setPriority(task.getPriority());
        processTask.setAssignee(task.getAssignee());
        return processTask;
    }

}
