package com.carbonclick.tsttask.secretsanta.base;

import com.carbonclick.tsttask.secretsanta.base.page.Page;
import com.carbonclick.tsttask.secretsanta.base.page.PageRequest;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CompoundSelection;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;

@Service
public class QueryService {

    private final EntityManager entityManager;
    private final CriteriaBuilder cb;

    private QueryService(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.cb = entityManager.getCriteriaBuilder();
    }

    public <E, P> PageQueryBuilder<E, P> pageQueryBuilder(
            Class<E> entityClass,
            Class<P> projectionClass) {
        return new PageQueryBuilder<>(entityClass, projectionClass);
    }

    public <E, P> QueryBuilder<E, P> querybuilder(
            Class<E> entityClass,
            Class<P> projectionClass) {
        return new QueryBuilder<>(entityClass, projectionClass);
    }

    public static class PageQuery <P> {
        private final TypedQuery<Long> countTypedQuery;
        private final TypedQuery<P> listTypedQuery;

        private PageQuery(TypedQuery<Long> countTypedQuery, TypedQuery<P> listTypedQuery) {
            this.countTypedQuery = countTypedQuery;
            this.listTypedQuery = listTypedQuery;
        }

        public Page<P> getPage(PageRequest pageRequest) {
            int count = Optional.ofNullable(countTypedQuery.getSingleResult()).orElse(0L).intValue();
            List<P> list = listTypedQuery
                    .setFirstResult(pageRequest.getOffset())
                    .setMaxResults(pageRequest.getPageSize())
                    .getResultList();
            return new Page<>(list, pageRequest.getPageSize(), count);
        }
    }

    protected abstract class AbstractQueryBuilder<E, P, B extends AbstractQueryBuilder<E, P, B>> {
        protected final CriteriaQuery<P> query;
        protected final Root<E> from;

        private AbstractQueryBuilder(Class<E> entityClass, Class<P> projectionClass) {
            query = cb.createQuery(projectionClass);
            from = query.from(entityClass);
        }

        public B select(BiFunction<CriteriaBuilder, Root<E>, CompoundSelection<P>> selection) {
            query.select(selection.apply(cb, from));
            return (B)this;
        }

        public abstract B where(BiFunction<CriteriaBuilder, Root<E>, Expression<Boolean>> where);

        public B orderBy(BiFunction<CriteriaBuilder, Root<E>, List<Order>> orderBy) {
            query.orderBy(orderBy.apply(cb, from));
            return (B)this;
        }
    }

    public class QueryBuilder<E, P> extends AbstractQueryBuilder<E, P, QueryBuilder<E, P>> {
        private QueryBuilder(Class<E> entityClass, Class<P> projectionClass) {
            super(entityClass, projectionClass);
        }

        public QueryBuilder<E, P> where(BiFunction<CriteriaBuilder, Root<E>, Expression<Boolean>> where) {
            query.where(where.apply(cb, from));
            return this;
        }

        public TypedQuery<P> build() {
            return entityManager.createQuery(query);
        }
    }

    public class PageQueryBuilder<E, P> extends AbstractQueryBuilder<E, P, PageQueryBuilder<E, P>> {
        private final CriteriaQuery<Long> countQuery;
        private final Root<E> countFrom;

        private PageQueryBuilder(Class<E> entityClass, Class<P> projectionClass) {
            super(entityClass, projectionClass);

            countQuery = cb.createQuery(Long.class);
            countFrom = countQuery.from(entityClass);
            countQuery.select(cb.count(countFrom));
        }

        public PageQueryBuilder<E, P> where(BiFunction<CriteriaBuilder, Root<E>, Expression<Boolean>> where) {
            query.where(where.apply(cb, from));
            countQuery.where(where.apply(cb, countFrom));
            return this;
        }

        public PageQuery<P> build() {
            TypedQuery<Long> countTypedQuery = entityManager.createQuery(countQuery);
            TypedQuery<P> listTypedQuery = entityManager.createQuery(query);
            return new PageQuery<>(countTypedQuery, listTypedQuery);
        }
    }
}
