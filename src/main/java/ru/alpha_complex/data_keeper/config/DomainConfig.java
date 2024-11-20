package ru.alpha_complex.data_keeper.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@Configuration
@EntityScan("ru.alpha_complex.data_keeper.domain")
@EnableJpaRepositories("ru.alpha_complex.data_keeper.repos")
@EnableTransactionManagement
public class DomainConfig {
}
