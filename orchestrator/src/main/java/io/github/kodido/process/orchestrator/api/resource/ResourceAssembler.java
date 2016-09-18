package io.github.kodido.process.orchestrator.api.resource;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;

import java.util.stream.Collector;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public interface ResourceAssembler<T> extends org.springframework.hateoas.ResourceAssembler<T, Resource<T>> {

    static <T> Collector<T, ?, Resources<T>> toResources() {
        return Collectors.collectingAndThen(toList(), list -> new Resources<>(list));
    }

}