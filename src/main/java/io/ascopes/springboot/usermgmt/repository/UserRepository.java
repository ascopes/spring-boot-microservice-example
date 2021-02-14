package io.ascopes.springboot.usermgmt.repository;

import io.ascopes.springboot.usermgmt.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * User repository for the SQL/NoSQL backend.
 */
@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    /**
     * Find a user by their username, case insensitively.
     *
     * @param name the name to look up.
     * @return an optional containing the user if it is present, or empty if not found.
     */
    Optional<UserEntity> findByUserNameIgnoreCase(String name);

    /**
     * Parses an ID into a long and looks up the entity with that ID.
     *
     * @param id the ID to use.
     * @return the entity within an optional if found, otherwise an empty optional.
     */
    default Optional<UserEntity> findByStringId(String id) {
        return findById(Long.parseLong(id));
    }

    /**
     * Parses an ID into a long and deletes the entity with that ID.
     *
     * @param id the ID to use.
     */
    default void deleteByStringId(String id) {
        deleteById(Long.parseLong(id));
    }
}
