package com.hjh.hhyx;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author 韩
 * @version 1.0
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)//取消mysql数据源自动配置
@EnableDiscoveryClient//服务注册
@EnableFeignClients//服务发现
public class ServiceSearchApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceSearchApplication.class, args);
    }

}
