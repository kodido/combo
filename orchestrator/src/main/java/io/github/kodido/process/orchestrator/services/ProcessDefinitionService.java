package io.github.kodido.process.orchestrator.services;

import io.github.kodido.process.orchestrator.model.ProcessDefinition;
import io.github.kodido.process.processes.metadata.Processes;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProcessDefinitionService {

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private RepositoryService repositoryService;

    public Optional<ProcessDefinition> getProcessById(String processId) {
        final org.activiti.engine.repository.ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().
                processDefinitionKey(processId).latestVersion().singleResult();

        return processDefinition != null ?
                Optional.of(new ProcessDefinition(processId, processDefinition.getName(), processDefinition.getDescription())) : Optional.empty();
    }

    public List<ProcessDefinition> listProcesses() {
        List<String> processIds = Processes.getProcessDefinitionIds();
        return processIds.stream()
                .map(this::getProcessById)
                .filter(Optional::isPresent)
                .map(Optional::get).collect(Collectors.toList());
    }

}
