# QIMS - 质量检测管理系统

## 项目简介

QIMS (Quality Inspection Management System) 是一个基于 Spring Boot 3.2 构建的质量检测管理系统后端服务，提供检测任务管理、数据采集、告警通知等核心功能。

## 技术栈

| 分类 | 技术 | 版本 |
| :--- | :--- | :--- |
| 语言 | Java | 17 |
| 框架 | Spring Boot | 3.2.5 |
| ORM | MyBatis-Plus | 3.5.6 |
| 认证 | JWT | 0.12.5 |
| 定时任务 | Quartz | Spring Boot 集成 |
| Excel处理 | Apache POI | 5.2.5 |
| 工具类 | Hutool | 5.8.26 |
| 对象映射 | MapStruct | 1.5.5.Final |
| 数据库 | MySQL | 8.0+ |

## 模块结构

```
QIMS-backend/
├── quality-common/          # 通用模块
│   ├── src/main/java/com/qims/common/
│   │   ├── base/            # 基础实体类
│   │   ├── config/          # 配置类
│   │   ├── datascope/       # 数据权限
│   │   ├── enums/           # 枚举类
│   │   ├── exception/       # 异常处理
│   │   ├── handler/         # 处理器
│   │   ├── result/          # 统一响应
│   │   └── security/        # JWT安全
├── quality-domain/          # 领域模块
│   ├── src/main/java/com/qims/domain/
│   │   ├── config/          # MyBatis配置
│   │   ├── entity/          # 数据库实体
│   │   └── mapper/          # Mapper接口
├── quality-service/         # 服务模块
│   ├── src/main/java/com/qims/service/
│   │   ├── dto/             # 数据传输对象
│   │   └── service/         # 业务服务
├── quality-web/             # Web模块
│   ├── src/main/java/com/qims/web/
│   │   ├── controller/      # REST控制器
│   │   ├── dto/             # 表单DTO
│   │   ├── security/        # Spring Security配置
│   │   └── QualityApplication.java  # 启动类
└── quality-quartz/          # 定时任务模块
    ├── src/main/java/com/qims/quartz/
    │   ├── config/          # Quartz配置
    │   ├── job/             # Job实现
    │   └── service/         # 调度服务
```

## 核心功能

### 1. 用户认证与授权

- **登录/注册**: 支持用户名密码登录和注册
- **JWT认证**: 无状态Token认证
- **权限管理**: 基于角色的权限控制(RBAC)
- **菜单树**: 动态获取用户菜单

### 2. 检测任务管理

- **任务创建**: 创建检测任务，关联节点和检测项
- **任务状态**: 待检测、检测中、已完成、已取消
- **进度跟踪**: 实时跟踪检测进度和合格率

### 3. 检测记录管理

- **手动录入**: 支持手动录入检测数据
- **API采集**: 自动从外部API采集数据
- **公式计算**: 支持Aviator表达式计算
- **定时采集**: Quartz定时任务自动采集

### 4. 告警通知

- **邮件推送**: 检测不达标时自动发送邮件告警
- **条件推送**: 支持自定义推送条件

### 5. 报表统计

- **日报/周报/月报**: 多维度统计报表
- **Excel导出**: 支持导出报表数据

## 数据库表结构

### 用户相关表

| 表名 | 说明 |
| :--- | :--- |
| sys_user | 用户表 |
| sys_role | 角色表 |
| sys_permission | 权限表 |
| sys_user_role | 用户角色关联 |
| sys_role_permission | 角色权限关联 |

### 检测相关表

| 表名 | 说明 |
| :--- | :--- |
| qims_node | 节点表 |
| qims_inspection_item | 检测项表 |
| qims_inspection_task | 检测任务表 |
| qims_inspection_record | 检测记录表 |
| qims_inspection_default_value | 检测默认值表 |

### 实施相关表

| 表名 | 说明 |
| :--- | :--- |
| qims_implementation_plan | 整改方案表 |
| qims_implementation_feedback | 整改反馈表 |

### 日志表

| 表名 | 说明 |
| :--- | :--- |
| sys_operation_log | 操作日志表 |

## 快速开始

### 环境要求

- JDK 17+
- MySQL 8.0+
- Maven 3.8+

### 配置数据库

修改 `quality-web/src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/qims?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai
    username: your_username
    password: your_password
```

### 启动服务

```bash
cd QIMS-backend
mvn clean package -DskipTests
java -jar quality-web/target/quality-web-1.0.0-SNAPSHOT.jar
```

服务启动后访问: `http://localhost:8888`

## API接口

### 认证接口

| 方法 | 路径 | 描述 |
| :--- | :--- | :--- |
| POST | `/api/auth/login` | 用户登录 |
| POST | `/api/auth/register` | 用户注册 |
| POST | `/api/auth/logout` | 用户登出 |
| GET | `/api/auth/menus` | 获取用户菜单 |
| GET | `/api/auth/permissions` | 获取用户权限 |

### 用户管理

| 方法 | 路径 | 描述 |
| :--- | :--- | :--- |
| GET | `/api/users` | 获取用户列表 |
| GET | `/api/users/{id}` | 获取用户详情 |
| POST | `/api/users` | 创建用户 |
| PUT | `/api/users/{id}` | 更新用户 |
| DELETE | `/api/users/{id}` | 删除用户 |

### 检测项管理

| 方法 | 路径 | 描述 |
| :--- | :--- | :--- |
| GET | `/api/inspection-items` | 获取检测项列表 |
| GET | `/api/inspection-items/{id}` | 获取检测项详情 |
| POST | `/api/inspection-items` | 创建检测项 |
| PUT | `/api/inspection-items/{id}` | 更新检测项 |
| DELETE | `/api/inspection-items/{id}` | 删除检测项 |

### 检测任务管理

| 方法 | 路径 | 描述 |
| :--- | :--- | :--- |
| GET | `/api/inspection-tasks` | 获取任务列表 |
| GET | `/api/inspection-tasks/{id}` | 获取任务详情 |
| POST | `/api/inspection-tasks` | 创建任务 |
| PUT | `/api/inspection-tasks/{id}` | 更新任务 |
| DELETE | `/api/inspection-tasks/{id}` | 删除任务 |

### 检测记录管理

| 方法 | 路径 | 描述 |
| :--- | :--- | :--- |
| GET | `/api/inspection-records` | 获取记录列表 |
| GET | `/api/inspection-records/{id}` | 获取记录详情 |
| POST | `/api/inspection-records/manual` | 手动录入记录 |
| DELETE | `/api/inspection-records/{id}` | 删除记录 |

### 报表接口

| 方法 | 路径 | 描述 |
| :--- | :--- | :--- |
| GET | `/api/reports/daily` | 获取日报 |
| GET | `/api/reports/weekly` | 获取周报 |
| GET | `/api/reports/monthly` | 获取月报 |
| GET | `/api/reports/export` | 导出Excel报表 |

### 统计接口

| 方法 | 路径 | 描述 |
| :--- | :--- | :--- |
| GET | `/api/statistics/overview` | 获取统计概览 |
| GET | `/api/statistics/trend` | 获取趋势数据 |
| GET | `/api/statistics/qualified-rate` | 获取合格率统计 |

### 日志接口

| 方法 | 路径 | 描述 |
| :--- | :--- | :--- |
| GET | `/api/logs/operation` | 获取操作日志 |
| DELETE | `/api/logs/operation/{id}` | 删除操作日志 |

## 统一响应格式

### 成功响应

```json
{
  "code": 200,
  "message": "success",
  "data": {
    // 业务数据
  }
}
```

### 失败响应

```json
{
  "code": 500,
  "message": "错误描述",
  "data": null
}
```

## 状态码说明

| 状态码 | 说明 |
| :--- | :--- |
| 200 | 成功 |
| 400 | 请求参数错误 |
| 401 | 未授权 |
| 403 | 无权限 |
| 404 | 资源不存在 |
| 500 | 服务器内部错误 |

## 定时任务

系统使用 Quartz 实现定时任务，主要功能：

1. **检测项定时采集**: 根据检测项配置的cron表达式定时采集数据
2. **数据来源支持**:
   - API采集: 从配置的API地址获取数据
   - 公式计算: 使用Aviator表达式计算
3. **告警推送**: 检测不达标时自动发送邮件通知

## 安全特性

- **JWT认证**: 无状态Token认证，支持过期时间配置
- **密码加密**: 使用BCrypt加密存储密码
- **请求过滤**: JWT过滤器验证Token有效性
- **全局异常处理**: 统一异常处理，避免敏感信息泄露

## 配置说明

### JWT配置

```yaml
app:
  jwt:
    secret: QIMS-Quality-Inspection-Management-System-JWT-Secret-Key-2024!!
    expiration: 7200000  # 过期时间(毫秒)
    header: Authorization
    prefix: "Bearer "
```

### 邮件配置

```yaml
spring:
  mail:
    host: smtp.qq.com
    port: 587
    username: your-email@qq.com
    password: your-smtp-auth-code
```

### Quartz配置

```yaml
spring:
  quartz:
    job-store-type: jdbc
    jdbc:
      initialize-schema: always
    properties:
      org:
        quartz:
          jobStore:
            class: org.quartz.impl.jdbcjobstore.JobStoreTX
            tablePrefix: QRTZ_
            isClustered: true
          threadPool:
            threadCount: 10
```

## 开发规范

### 命名规范

- **类名**: PascalCase，如 `UserController`
- **方法名**: camelCase，如 `getUserById`
- **变量名**: camelCase，如 `userName`
- **常量名**: UPPER_SNAKE_CASE，如 `MAX_SIZE`

### 代码结构

- Controller层：处理HTTP请求，参数校验，调用Service
- Service层：业务逻辑处理，事务管理
- Mapper层：数据库访问，MyBatis-Plus接口
- Entity层：数据库实体，对应表结构
- DTO层：数据传输对象，用于请求和响应

### 异常处理

- 使用 `BizException` 处理业务异常
- 全局异常处理器 `GlobalExceptionHandler` 统一处理异常
- 避免直接抛出RuntimeException

## 部署说明

### 打包

```bash
mvn clean package -DskipTests
```

### 运行

```bash
# 开发环境
java -jar quality-web/target/quality-web-1.0.0-SNAPSHOT.jar

# 生产环境
java -jar quality-web/target/quality-web-1.0.0-SNAPSHOT.jar --spring.profiles.active=prod
```

### Docker部署

```dockerfile
FROM openjdk:17-jdk-slim
COPY quality-web/target/quality-web-1.0.0-SNAPSHOT.jar /app.jar
EXPOSE 8888
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

## 版本历史

| 版本 | 日期 | 描述 |
| :--- | :--- | :--- |
| 1.0.0-SNAPSHOT | 2026 | 1.0.0-SNAPSHOT版本 初始版本 |

## 许可证

Apache-2.0 license

## 联系信息

如有问题或建议，请联系开发团队。