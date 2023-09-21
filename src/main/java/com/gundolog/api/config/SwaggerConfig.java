package com.gundolog.api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi notionApi() {
        return GroupedOpenApi.builder()
            .group("Gundolog API v1")
            .pathsToMatch("/v1/**")
            .build();
    }

    @Bean
    public OpenAPI springOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Gundolog API 명세서")
                .description("헥사고날 아키텍쳐 기반 블로그 API 명세서")
                .version("v1.0.0"));
    }
}
