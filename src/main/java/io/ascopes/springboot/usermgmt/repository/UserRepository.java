package io.ascopes.springboot.usermgmt.repository;

import io.ascopes.springboot.usermgmt.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUserNameIgnoreCase(String name);
}
