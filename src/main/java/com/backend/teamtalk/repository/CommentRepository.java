package com.backend.teamtalk.repository;

import com.backend.teamtalk.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
