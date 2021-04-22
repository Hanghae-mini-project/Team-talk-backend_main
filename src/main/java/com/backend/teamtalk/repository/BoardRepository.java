package com.backend.teamtalk.repository;

import com.backend.teamtalk.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {

    List<Board> findByUserId(Long user_id);


}

