package com.apexifyconnect.Repository;

import com.apexifyconnect.Model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.role = :role")
    List<User> findByRole(@Param("role") String role);

    Page<User> findByRole(String role, Pageable pageable);

    @Query("SELECT u FROM User u JOIN u.tags t WHERE t.name IN :tagNames AND u.role = :role")
    List<User> findByRoleAndTags(@Param("role") String role, @Param("tagNames") List<String> tagNames);

    @Query("SELECT u FROM User u JOIN u.tags t WHERE t.name = :tagName AND u.role = 'CONTENT_CREATOR'")
    List<User> findContentCreatorsByTag(@Param("tagName") String tagName);

}
