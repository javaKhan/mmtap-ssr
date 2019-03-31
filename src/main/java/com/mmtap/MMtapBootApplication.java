package com.mmtap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * @author mmtap.com
 * @date 2019/1/8
 **/
@SpringBootApplication
@EnableJpaAuditing
public class MMtapBootApplication {
    public static void main(String[] args) {
        SpringApplication.run(MMtapBootApplication.class, args);
    }
}
