package com.backend.teamtalk.repository;

import com.backend.teamtalk.domain.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    /*
     * 속성 경로를 통해 authorities 를 불러오게 지정.
     * 로딩 정책을 이 엔티티 그룹에 적용.
     * 기본적으로 Fetch 정책이고, 이를 설정한 엔티티 속성에는 Eager 패치, 그 외 것들은 Lazy 패치.
     * == 쿼리 수행 될 때 lazy 가 아니라 eager 로 authorities 정보를 가져오게 설정
     */

    @EntityGraph(attributePaths = "authorities")
    Optional<User> findOneWithAuthoritiesByUsername(String username);
}
