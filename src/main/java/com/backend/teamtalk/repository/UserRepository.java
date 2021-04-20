package com.backend.teamtalk.repository;

import com.backend.teamtalk.domain.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    //쿼리 수행 될 때 lazy 가 아니라 eager 로 authorities 정보를 가져오게 설정
    @EntityGraph(attributePaths = "authorities")

    // username 을 기준으로 User 정보 가져올 때 권한 정보도 같이 가져오게 함
    Optional<User> findOneWithAuthoritiesByUsername(String username);
}
