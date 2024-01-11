package com.ditrit.letomodelizerapi.helper;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import org.mockito.Mockito;

import java.util.Collection;
import java.util.Date;

public abstract class MockHelper {

    public <T> EntityManager mockEntityManager(Class<T> entityClass) {
        return mockEntityManager(entityClass, Mockito.mock(EntityManager.class));
    }

    public <T> EntityManager mockEntityManager(Class<T> entityClass, EntityManager entityManager) {
        CriteriaBuilder builder = Mockito.mock(CriteriaBuilder.class);
        CriteriaQuery<T> query = Mockito.mock(CriteriaQuery.class);
        Root<T> root = Mockito.mock(Root.class);
        Path<Object> path = Mockito.mock(Path.class);
        Predicate predicate = Mockito.mock(Predicate.class);

        Mockito.when(entityManager.getCriteriaBuilder()).thenReturn(builder);
        Mockito.when(builder.createQuery(Mockito.any(entityClass.getClass()))).thenReturn(query);
        Mockito.when(query.from(Mockito.any(entityClass.getClass()))).thenReturn(root);
        Mockito.when(builder.and(Mockito.any())).thenReturn(predicate);
        Mockito.when(builder.or(Mockito.any())).thenReturn(predicate);
        Mockito.when(builder.not(Mockito.any())).thenReturn(predicate);
        Mockito.when(builder.lessThan(Mockito.any(), Mockito.any(Date.class))).thenReturn(predicate);
        Mockito.when(builder.greaterThan(Mockito.any(), Mockito.any(Date.class))).thenReturn(predicate);
        Mockito.when(builder.between(Mockito.any(), Mockito.any(Date.class), Mockito.any(Date.class)))
                .thenReturn(predicate);
        Mockito.when(builder.equal(Mockito.any(), Mockito.any())).thenReturn(predicate);
        Mockito.when(builder.notEqual(Mockito.any(), Mockito.any())).thenReturn(predicate);
        Mockito.when(root.get(Mockito.anyString())).thenReturn(path);
        Mockito.when(path.as(Mockito.any())).thenReturn(path);
        Mockito.when(path.in(Mockito.any(Collection.class))).thenReturn(predicate);

        return entityManager;
    }
}
