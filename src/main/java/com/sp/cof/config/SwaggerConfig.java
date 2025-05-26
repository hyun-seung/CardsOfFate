package com.sp.cof.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Cards of Fate API")
                        .version("v 1.0.0")
                        .description("Cards of Fate 백엔드 API 명세서"));
//                        .contact(new Contact().name("임현승").email("ardimento22@naver.com"))
//                        .license(new License().name("MIT License")));
    }
}
