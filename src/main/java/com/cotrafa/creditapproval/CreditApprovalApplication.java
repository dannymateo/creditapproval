package com.cotrafa.creditapproval;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableJpaRepositories
@EnableJpaAuditing(auditorAwareRef = "jpaAuditorAware")
@Configuration
@EnableAsync
public class CreditApprovalApplication {

	public static void main(String[] args) {
		SpringApplication.run(CreditApprovalApplication.class, args);
	}
}