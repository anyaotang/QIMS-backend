package com.qims.web.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * SpringDoc OpenAPI 配置
 * 访问地址: http://localhost:8888/swagger-ui.html
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("QIMS 质量检测管理系统 API 文档")
                        .description("质量检测管理系统（Quality Inspection Management System）后端接口文档，包含认证、用户管理、部门管理、角色管理、权限管理、节点管理、检测项管理、检测记录、实施方案、报表、统计、操作日志等模块。")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("QIMS Team")))
                .addSecurityItem(new SecurityRequirement().addList("Bearer Token"))
                .components(new Components()
                        .addSecuritySchemes("Bearer Token",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("输入登录后获取的 JWT Token")));
    }
}
