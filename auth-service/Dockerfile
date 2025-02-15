# 1. Java 17 환경을 사용
FROM openjdk:17-jdk

# 2. 작업 디렉토리 설정
WORKDIR /app

# 3. JAR 파일을 복사
COPY target/auth-service.jar app.jar

# 4. 컨테이너 시작 시 실행할 명령어
CMD ["java", "-jar", "app.jar"]