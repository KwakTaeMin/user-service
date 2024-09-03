package com.taemin.user.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .components(new Components()
                            .addSecuritySchemes("bearerAuth", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                            )
                            .addSecuritySchemes("Google OAuth2", new SecurityScheme()
                                .type(SecurityScheme.Type.OAUTH2)
                                .flows(new OAuthFlows()
                                           .authorizationCode(new OAuthFlow()
                                                                  .authorizationUrl("/oauth2/authorization/google")
                                                                  .tokenUrl("/login/oauth2/code/google")
                                           )
                                )
                            )
                            .addSecuritySchemes("Kakao OAuth2", new SecurityScheme()
                                .type(SecurityScheme.Type.OAUTH2)
                                .flows(new OAuthFlows()
                                           .authorizationCode(new OAuthFlow()
                                                                  .authorizationUrl("/oauth2/authorization/kakao")
                                                                  .tokenUrl("/login/oauth2/code/kakao")
                                           )
                                )
                            ).addSecuritySchemes("Naver OAuth2", new SecurityScheme()
                    .type(SecurityScheme.Type.OAUTH2)
                    .flows(new OAuthFlows()
                               .authorizationCode(new OAuthFlow()
                                                      .authorizationUrl("/oauth2/authorization/naver")
                                                      .tokenUrl("/login/oauth2/code/naver")
                               )
                    )
                )
            )
            .addSecurityItem(new SecurityRequirement().addList("bearerAuth").addList("OAuth2"))
            .servers(List.of(
                new Server().url("http://localhost:8080").description("Local environment"),
                new Server().url("http://ec2-3-38-210-40.ap-northeast-2.compute.amazonaws.com").description("EC2 environment")
            ))
            .info(new Info().title("User Service").version("1.0"));

    }

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
            .group("public")
            .pathsToMatch("/**")
            .build();
    }
}
