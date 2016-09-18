package io.github.kodido.process.app.activiti;

import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.form.AbstractFormType;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.repository.Deployment;
import org.activiti.rest.form.MonthFormType;
import org.activiti.rest.form.ProcessDefinitionFormType;
import org.activiti.rest.form.UserFormType;
import org.activiti.spring.ProcessEngineFactoryBean;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.sql.Driver;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class ActivitiConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(ActivitiConfiguration.class);

    @Autowired
    protected Environment environment;

    @Bean
    public DataSource dataSource() {
        SimpleDriverDataSource ds = new SimpleDriverDataSource();
        try {
            @SuppressWarnings("unchecked")
            Class<? extends Driver> driverClass = (Class<? extends Driver>) Class.forName(System.getProperty("jdbc.driver", "org.h2.Driver"));
            ds.setDriverClass(driverClass);
        } catch (Exception e) {
            LOGGER.error("Error loading driver class", e);
        }
        ds.setUrl(System.getProperty("jdbc.url", "jdbc:h2:mem:activiti;DB_CLOSE_DELAY=1000"));
        ds.setUsername(environment.getProperty("jdbc.username", "sa"));
        ds.setPassword(environment.getProperty("jdbc.password", ""));
        return ds;
    }

    @Bean(name="processEngineFactoryBean")
    public ProcessEngineFactoryBean processEngineFactoryBean() {
        ProcessEngineFactoryBean factoryBean = new ProcessEngineFactoryBean();
        factoryBean.setProcessEngineConfiguration(processEngineConfiguration());
        return factoryBean;
    }

    @Bean(name="processEngine")
    public ProcessEngine processEngine() {
        // Safe to call the getObject() on the @Bean annotated processEngineFactoryBean(), will be
        // the fully initialized object instanced from the factory and will NOT be created more than once
        try {
            return processEngineFactoryBean().getObject();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Bean(name = "transactionManager")
    public PlatformTransactionManager transactionManager() {
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
        transactionManager.setDataSource(dataSource());
        return transactionManager;
    }

    @Bean(name="processEngineConfiguration")
    public ProcessEngineConfigurationImpl processEngineConfiguration() {
        SpringProcessEngineConfiguration processEngineConfiguration = new SpringProcessEngineConfiguration();
        processEngineConfiguration.setDataSource(dataSource());
        processEngineConfiguration.setDatabaseSchemaUpdate(environment.getProperty("engine.schema.update", "true"));
        processEngineConfiguration.setTransactionManager(transactionManager());
        processEngineConfiguration.setJobExecutorActivate(Boolean.valueOf(
                environment.getProperty("engine.activate.jobexecutor", "false")));
        processEngineConfiguration.setAsyncExecutorEnabled(Boolean.valueOf(
                environment.getProperty("engine.asyncexecutor.enabled", "true")));
        processEngineConfiguration.setAsyncExecutorActivate(Boolean.valueOf(
                environment.getProperty("engine.asyncexecutor.activate", "true")));
        processEngineConfiguration.setHistory(environment.getProperty("engine.history.level", "full"));

        List<AbstractFormType> formTypes = new ArrayList<AbstractFormType>();
        formTypes.add(new UserFormType());
        formTypes.add(new ProcessDefinitionFormType());
        formTypes.add(new MonthFormType());
        processEngineConfiguration.setCustomFormTypes(formTypes);

        return processEngineConfiguration;
    }

/*    @Bean
    public ProcessEngine processEngine() {
        return ProcessEngineConfiguration.createStandaloneProcessEngineConfiguration()
                .setJdbcDriver("org.h2.Driver")
                .setDatabaseType("h2")
                .setJdbcUrl("jdbc:h2:mem:activiti;DB_CLOSE_DELAY=1000")
                .setJdbcUsername("sa")
                .setJdbcPassword("")
                .setDatabaseSchemaUpdate("true")
                .buildProcessEngine();
    }*/

    @Bean
    public RuntimeService runtimeService(ProcessEngine processEngine) {
        return processEngine.getRuntimeService();
    }

    @Bean
    public TaskService taskService(ProcessEngine processEngine) {
        return processEngine.getTaskService();
    }

    @Bean
    public RepositoryService repositoryService(ProcessEngine processEngine) {
        return processEngine.getRepositoryService();
    }

    @Bean
    public IdentityService identityService() {
        return processEngine().getIdentityService();
    }

    @Bean
    public ManagementService managementService() {
        return processEngine().getManagementService();
    }

    @Bean
    public HistoryService historyService(ProcessEngine processEngine) {
        return processEngine.getHistoryService();
    }

    @Bean
    public FormService formService() {
        return processEngine().getFormService();
    }

    @Bean
    public Deployment deploy(RepositoryService repositoryService) {
        Deployment deployment = repositoryService.createDeployment().addClasspathResource("processes/Release.bpmn").deploy();
        return deployment;
    }

}
