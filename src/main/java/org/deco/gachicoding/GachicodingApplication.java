package org.deco.gachicoding;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class GachicodingApplication {

    public static void main(String[] args) {
        SpringApplication.run(GachicodingApplication.class, args);
    }
}