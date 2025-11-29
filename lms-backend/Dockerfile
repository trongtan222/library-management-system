# Stage 1: Build với Maven & Java 21
# Sử dụng image mới hơn để hỗ trợ Java 21 và cập nhật chứng chỉ SSL
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app

# Copy file cấu hình trước để tận dụng caching
COPY pom.xml .
# Tải dependencies trước (giúp build nhanh hơn ở các lần sau)
RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Chạy ứng dụng với Java 21
FROM eclipse-temurin:21-jdk-jammy
WORKDIR /app
COPY --from=build /app/target/lms-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]