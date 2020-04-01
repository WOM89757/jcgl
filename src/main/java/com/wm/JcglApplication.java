package com.wm;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages= {"com.wm.*.mapper"})
public class JcglApplication {

    public static void main(String[] args) {
        SpringApplication.run(JcglApplication.class, args);
    }

}
