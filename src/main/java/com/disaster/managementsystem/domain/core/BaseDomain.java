package com.disaster.managementsystem.domain.core;

import com.disaster.managementsystem.entity.BaseEntity;
import com.disaster.managementsystem.repository.core.CustomRepository;
import com.disaster.managementsystem.util.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


public abstract class BaseDomain<T, ID extends Serializable> {
    private final CustomRepository<T, ID> repository;

    @Autowired
    private Environment environment;

    @Autowired
    protected BaseDomain(CustomRepository<T, ID> repository) {
        this.repository = repository;
    }

    @Transactional
    public T createByEntity(T inputPO) throws Exception {
        return repository.save(inputPO);
    }

    @Transactional
    public T updateByEntity(T po) throws Exception {
        return repository.save(po);
    }

    @Transactional
    public T updateStatus(T po) throws Exception {
        return repository.save(po);
    }

    @Transactional
    public List<T> updateByEntities(Set<T> po) throws Exception {
        return repository.saveAll(po);
    }

    public Optional<T> findById(UUID id) {
        return repository.findById((ID) id);
    }

    public List<T> getAllByIds(List<ID> ids) throws Exception {
        return ids.stream()
                .map(id -> repository.findById(id).orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
    public void deleteById(UUID id) throws Exception {
        T entity = findById(id).orElseThrow(() -> new IllegalArgumentException("Entity not found"));
        delete(entity);
    }

    public void delete(T entity) {
        ((BaseEntity) entity).setDeletedBy(CommonUtils.getCurrentUserId());
        ((BaseEntity) entity).setDeletedAt(LocalDateTime.now());
        ((BaseEntity) entity).setDeleted(Boolean.TRUE);
        repository.save(entity);
    }

    @SuppressWarnings("unchecked")
    protected Class<T> getClassT() throws Exception {
        Type type = getClass().getGenericSuperclass();
        return (Class) ((ParameterizedType) type).getActualTypeArguments()[0];
    }

    public String getMessage(String errorCode){
        return Optional.ofNullable(environment.getProperty(errorCode))
                .orElse("Error code is not found or not defined properly!");
    }
}