## USER-SERVICE

<aside>
💡 해당 유저서비스는 다른 곳에서 모듈화 사용할 수 있도록 구성하였다.
</aside>

- ${PROJECT_ROOT}/.env 파일 생성 
```groovy
JWT_KEY={JWT_KEY}

MYSQL_URL={MYSQL_URL}
MYSQL_USER={MYSQL_USER}
MYSQL_USER_PASSWORD={MYSQL_PASSWORD}

GOOGLE_CLIENT_ID={GOOGLE_CLIENT_ID}
GOOGLE_CLIENT_SECRET={GOOGLE_CLIENT_SECRET}
GOOGLE_REDIRECT_URI=http://localhost:8080/login/oauth2/code/google

KAKAO_CLIENT_ID={KAKAO_CLIENT_ID}
KAKAO_CLIENT_SECRET={KAKAO_CLIENT_SECRET}
KAKAO_REDIRECT_URI=http://localhost:8080/login/oauth2/code/kakao

NAVER_CLIENT_ID={NAVER_CLIENT_ID}
NAVER_CLIENT_SECRET={NAVER_CLIENT_SECRET}
NAVER_REDIRECT_URI=http://localhost:8080/login/oauth2/code/naver
```

- 환경
    - Spring 3.3.2
    - JDK 21
- 실행방법
    - JDK 21 설치
    
    - Spring 실행
    - [localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html) 접속

- Docker Image Build

```bash
docker build -t my-spring-boot-app .
```

- Docker Image Container start

```bash
docker run -d -p 8080:8080 my-spring-boot-app
```

- Docker Image

```bash
docker build -t user-service:0.0.1 .
```

```bash
`docker-compse up` -d #mysql, user-service container
```