package com.carbonclick.tsttask.secretsanta.user.repository;

import com.carbonclick.tsttask.secretsanta.base.BaseRepository;
import com.carbonclick.tsttask.secretsanta.user.repository.dto.UserDto;
import com.carbonclick.tsttask.secretsanta.user.repository.entity.UserEntity;
import com.carbonclick.tsttask.secretsanta.user.repository.entity.UserEntity_;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

@Component
public class UserRepository extends BaseRepository<UserEntity> {

    public UserRepository(EntityManager entityManager, CriteriaBuilder criteriaBuilder) {
        super(UserEntity.class, entityManager, criteriaBuilder);
    }

    public UserDto getUserByName(String userName) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<UserDto> cq = getCriteriaBuilder().createQuery(UserDto.class);
        Root<UserEntity> from = cq.from(UserEntity.class);

        cq.select(cb.construct(UserDto.class,
                from.get(UserEntity_.USERNAME),
                from.get(UserEntity_.PASSWORD)
        ));

        cq.where(cb.equal(from.get(UserEntity_.USERNAME), userName));

        return getEntityManager().createQuery(cq).getSingleResult();
    }
}
