package com.carbonclick.tsttask.secretsanta.user.repository;

import com.carbonclick.tsttask.secretsanta.assignment.repository.QueryService;
import com.carbonclick.tsttask.secretsanta.user.repository.dto.UserDto;
import com.carbonclick.tsttask.secretsanta.user.repository.entity.UserEntity;
import com.carbonclick.tsttask.secretsanta.user.repository.entity.UserEntity_;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserRepository {

    private final QueryService queryService;

    public UserDto getUserByName(String userName) {
        return queryService.querybuilder(UserEntity.class, UserDto.class)
                .select((cb, from) -> cb.construct(UserDto.class,
                        from.get(UserEntity_.USERNAME),
                        from.get(UserEntity_.PASSWORD)
                ))
                .where((cb, from) -> cb.equal(from.get(UserEntity_.USERNAME), userName))
                .build()
                .getSingleResult();
    }
}
