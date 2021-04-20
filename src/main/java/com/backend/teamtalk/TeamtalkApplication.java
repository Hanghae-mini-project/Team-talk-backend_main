package com.backend.teamtalk;

import com.backend.teamtalk.domain.User;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.ArrayList;

@EnableJpaAuditing
@SpringBootApplication
public class TeamtalkApplication {

	public static void main(String[] args) {
		SpringApplication.run(TeamtalkApplication.class, args);

	}

}
