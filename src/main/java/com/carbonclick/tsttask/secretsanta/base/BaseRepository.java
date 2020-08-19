package com.carbonclick.tsttask.secretsanta.base;

import com.carbonclick.tsttask.secretsanta.base.page.Page;
import com.carbonclick.tsttask.secretsanta.base.page.PageRequest;
import lombok.Getter;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;

@Getter
public class BaseRepository<E> {

    private final Class<E> entityClass;
    private final EntityManager entityManager;
    private final CriteriaBuilder criteriaBuilder;

    public BaseRepository(Class<E> entityClass, EntityManager entityManager, CriteriaBuilder criteriaBuilder) {
        this.entityClass = entityClass;
        this.entityManager = entityManager;
        this.criteriaBuilder = criteriaBuilder;
    }

    protected <R> Page<R> selectAsPage(
            Class<R> responseClass,
            PageRequest pageable,
            BiFunction<CriteriaBuilder, Root<E>, Selection<R>> getSelection) {
        return selectAsPage(responseClass, pageable, getSelection, null);
    }

    protected <R> Page<R> selectAsPage(
            Class<R> responseClass,
            PageRequest pageable,
            BiFunction<CriteriaBuilder, Root<E>, Selection<R>> getSelection,
            BiFunction<CriteriaBuilder, Root<E>, List<Order>> getOrderBy) {
        return new Page<>(
                getContent(responseClass, pageable, getSelection, getOrderBy),
                pageable.getPageSize(),
                count());
    }

    private <R> List<R> getContent(
            Class<R> responseClass,
            PageRequest pageable,
            BiFunction<CriteriaBuilder, Root<E>, Selection<R>> getSelection,
            BiFunction<CriteriaBuilder, Root<E>, List<Order>> getOrderBy) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<R> cq = criteriaBuilder.createQuery(responseClass);
        Root<E> from = cq.from(entityClass);

        cq.select(getSelection.apply(cb, from));
        TypedQuery<R> q = entityManager.createQuery(cq)
                .setFirstResult(pageable.getOffset())
                .setMaxResults(pageable.getPageSize());

        if(getOrderBy != null) {
            cq.orderBy(getOrderBy.apply(cb, from));
        }

        return q.getResultList();
    }

    public int count( ) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<E> from = cq.from(entityClass);
        cq.select(cb.count(from));

        return Optional.ofNullable(entityManager.createQuery(cq).getSingleResult())
                .map(Long::intValue)
                .orElse(0);
    }
}
