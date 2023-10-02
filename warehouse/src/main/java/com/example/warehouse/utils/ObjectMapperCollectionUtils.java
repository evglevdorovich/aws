package com.example.warehouse.utils;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ObjectMapperCollectionUtils {

    private final ModelMapper modelMapper;

    public <D, T> Set<D> mapAllToSet(final Collection<T> entityList, Class<D> outClass) {
        return entityList
                .stream()
                .map(entity -> map(entity, outClass))
                .collect(Collectors.toSet());
    }

    public <D, T> List<D> mapAllToList(final Collection<T> entityList, Class<D> outClass) {
        return entityList
                .stream()
                .map(entity -> map(entity, outClass))
                .toList();
    }

    private <D, T> D map(final T entity, Class<D> outClass) {
        return modelMapper.map(entity, outClass);
    }

}
