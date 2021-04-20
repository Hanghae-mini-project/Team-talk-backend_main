package com.backend.teamtalk.repository;

import com.backend.teamtalk.domain.Pin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PinRepository extends JpaRepository<Pin, Long> {

}
