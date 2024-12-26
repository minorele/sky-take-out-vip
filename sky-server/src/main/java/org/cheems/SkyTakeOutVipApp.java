package org.cheems;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;



@SpringBootApplication
@EnableTransactionManagement //开启注解方式的事务管理
@Slf4j
public class SkyTakeOutVipApp {
    public static void main(String[] args) {

        log.info("sky take out vip server started");

        SpringApplication.run(SkyTakeOutVipApp.class, args);
    }
}
