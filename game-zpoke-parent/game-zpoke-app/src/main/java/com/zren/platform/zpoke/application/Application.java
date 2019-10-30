package com.zren.platform.zpoke.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@ComponentScan(basePackages = {"com.zren.platform","com.assembly","com.jxinternet.platform.services.balance"})
@EnableJpaRepositories(basePackages = {"com.zren.platform.common.dal.repository"})
@EntityScan(basePackages = {"com.zren.platform.common.dal.po"})
@EnableFeignClients(basePackages = {"com.zren.platform"})
@SpringBootApplication
@EnableScheduling
public class Application
{
    public static void main(String[] args)
    {
        SpringApplication.run(Application.class, args);
    }
}
