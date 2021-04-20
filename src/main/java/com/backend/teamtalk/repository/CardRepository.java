package com.backend.teamtalk.repository;

import com.backend.teamtalk.domain.Card;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<Card, Long> {
}
