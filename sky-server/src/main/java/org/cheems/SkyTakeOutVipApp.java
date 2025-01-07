package org.cheems;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.transaction.annotation.EnableTransactionManagement;



@SpringBootApplication
@EnableTransactionManagement //开启注解方式的事务管理
@EnableCaching //开始缓存注解
@Slf4j
public class SkyTakeOutVipApp {
    public static void main(String[] args) {

        SpringApplication.run(SkyTakeOutVipApp.class, args);
        log.info("sky take out vip server started");
    }
}
