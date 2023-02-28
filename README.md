## AWS EC2에 Spring Boot 프로젝트 배포하기

```text
ec2 인스턴스 사양은 프리티어 t2.micro 사양을 사용 했으며 운영체제는 ubuntu 22.04를 사용했다.
```

### # EC2 인스턴스에서 스프링 부트 프로젝트 실행

#### ▶︎ java 설치

```text
# 프로젝트에서 사용하는 버전에 맞게 설치하면 된다.
$ sudo apt-get openjdk-11-jdk

# 자바 버전 확인
$ java --version
```

#### ▶︎ git을 이용해 프로젝트 clone

ubuntu의 경우 git이 기본으로 설치되어 있었다.

```text
$ git clone 경로
```
#### ▶︎ 빌드 후 프로젝트 실행

```text
# maven
$ maven package

# gradle
$ ./gradlew build

$ cd build/libs
$ java -jar 이름.jar
```
<p align="center">
<img width="458" alt="스크린샷 2023-02-28 오후 5 42 38" src="https://user-images.githubusercontent.com/102194801/221799866-a3aa2044-f745-49c9-8650-3d7e82a6d4e2.png">
</p>

8080번 포트를 붙여줘야 프로젝트의 API를 호출할 수 있다. Nginx를 사용해 8080번 포트를 붙이지 않아도 호출할 수 있게 바꾸도록 하겠다.

#

### # Nginx 적용

`Nginx`를 적용해 작업하려는 구조는 다음과 같다.

<p align="center">
<img width="500" alt="스크린샷 2023-02-28 오후 3 53 54" src="https://user-images.githubusercontent.com/102194801/221776960-2f45040c-68e4-4bc9-b333-80117a772e15.png">
</p>

위 처럼 Nginx가 외부의 요청을 받아 서버로 요청을 전달하는 행위를 `리버스 프록시`라고 한다. 리버스 프록시 서버(Nginx)는 요청을 전달하고, 실제 요청에 대한 처리는 뒷단의 웹서버들이 처리한다. 대신 외부 요청을 뒷단 서버들에게 골고루 분배하거나, 한 번 요청왔던 js, image, css같은 정적 파일은 캐시해 리버스 프록시 서버에서 바로 응답을 주거나 등의 여러 장점들을 가지고 있다

학습하는 것이 목적이므로 위 그림과 같이 80번 포트로 접속하면 ec2 인스턴스 내 8080번 포트로 동작하고 있는 웹 애플리케이션에 연결되도록 한다.

#### ▶︎ Nginx 설치

```text
$ sudo apt-get install nginx
```

#### ▶︎ Nginx 기본 명령어

```text
$ sudo service nginx start (시작)

$ sudo service nginx restart (서버 재시작)

$ sudo service nginx reload (설정파일 재적용)

$ sudo service nginx stop (정지)

$ sudo service nginx status (상태확인)
```

#### ▶︎ Nginx 실행

nginx를 실행하고 상태를 확인하면 아래과 같이 나온다.

<p align="center">
<img width="1138" alt="스크린샷 2023-02-28 오후 4 12 07" src="https://user-images.githubusercontent.com/102194801/221784539-6b956c0a-bfe1-4cf7-9c10-0cd7d7bd6e8d.png">
</p>

이제 ec2 인스턴스에 설정한 IP로 접속해보면 아래와 같은 화면이 나오면 성공이다.

<p align="center">
<img width="543" alt="스크린샷 2023-02-28 오후 4 17 02" src="https://user-images.githubusercontent.com/102194801/221781511-65d4be14-8b93-4b49-b98e-8e19893a81fc.png">
</p>

만약 아무런 페이지도 나오지 않으면 EC2 관리자  보안그룹 항목에 가서 인바운드 규칙에 80번 포트에 대한 규칙을 추가해야 한다.
  
#### ▶︎ Nginx와 스프링 부트 프로젝트 연결

```text
# 사용하는 운영체제에 따라 경로를 지정하는 것이 다른 것 같다
$ sudo vi /etc/nginx/sites-available/default
```

위 명령어를 입력해 location 부분을 다음과 같이 수정해 준다.

<p align="center">
<img width="632" alt="스크린샷 2023-02-28 오후 4 47 53" src="https://user-images.githubusercontent.com/102194801/221787637-763159a2-1655-4877-8f76-547d3e863ead.png">
</p>

수정 후 빌드한 jar 파일을 실행 시켜준 다음 다시 접속해 보면 다음과 같이 8080번 포트가 없이 접근해 프로젝트의 API를 호출할 수 있다.

<p align="center">
<img width="455" alt="스크린샷 2023-02-28 오후 5 11 13" src="https://user-images.githubusercontent.com/102194801/221793570-6833caad-515b-4c34-a352-f72f7b4403e9.png">
</p>

Nginx를 사용해 80번 포트를 이용해 8080번 포트를 사용하는 웹 애플리케이션에 접근할 수 있으니 기존에 인바운드 규칙에 설정해 놓은 8080 포트에 대한 규칙을 제거해도 된다.

#

위 예제를 진행하는 아래 두 곳에서 많이 도움을 받았습니다. 감사합니다.

#### 참고

- https://jojoldu.tistory.com/267
- https://hue9010.github.io/aws/nginx%EB%A5%BC-%EC%A0%81%EC%9A%A9%ED%95%B4-%EB%B3%B4%EC%9E%90/
