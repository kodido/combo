package io.github.kodido.process.orchestrator.api.resource;

import org.springframework.hateoas.Resource;
import org.springframework.stereotype.Component;

@Component
public class DefaultResourceAssembler<T> implements ResourceAssembler<T> {

    @Override
    public Resource<T> toResource(T t ) {
        return new Resource<>(t);
    }

}
