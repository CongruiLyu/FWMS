package com.fwms;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.fwms.mapper")
public class FwmsApplication {

    public static void main(String[] args) {
        SpringApplication.run(FwmsApplication.class, args);
    }
}
