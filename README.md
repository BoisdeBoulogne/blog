# 项目名称

本项目是基于郑州轻工业大学工程实验室Java组考核要求构建的后端服务。

## 技术栈

- MyBatis
- Redis
- JWT
- Spring Boot
- 远程数据库
- 阿里云 OSS（对象存储服务）

## 项目特点

1. **用户校验：**  
   使用 JWT（JSON Web Token）进行用户认证和校验，确保 API 访问的安全性。解析后的用户 ID 存储在 `ThreadLocal` 中，方便后续业务逻辑的使用。

2. **多对多关系处理：**  
   对于多对多的关系，如用户收藏文章、点赞等，使用双方的 ID 作为联合主键，独立出一张表来存储关系数据，方便 SQL 查询。

3. **MyBatis Mapper 配置：**  
   项目使用 `.xml` 文件映射 MyBatis 的 Mapper 类，通过动态 SQL 实现灵活查询操作。

4. **Redis 缓存：**  
   项目使用远程 Redis 服务器缓存文章详情数据，防止缓存击穿和缓存穿透问题。在经过 JMeter 压测的条件下，加入缓存后的平均响应时间不到未使用缓存时的 1/10。

5. **阿里云 OSS：**  
   使用阿里云 OSS 来存储和管理图片文件，简化文件的管理和展示流程。

## 注意事项

- 本项目依赖于远程数据库和 Redis 服务，在使用时，请确保网络连接正常。
- 为了确保阿里云的安全，请在 `application.yml` 文件中添加您自己的阿里云 OSS 配置属性。

## 配置文件示例（`application.yml`）


  oss:
    accessKeyId: your_aliyun_access_key
    accessKeySecret: your_aliyun_access_secret
    endpoint: your_aliyun_oss_endpoint
    bucketName: your_bucket_name