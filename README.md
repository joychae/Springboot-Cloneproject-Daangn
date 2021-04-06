SpringBoot CloneProject : Springboot-Cloneproject-Daangn 
=====================

참여인원 : 백앤드 2명 (김동현, 채수연) / 프론트 2명 (김광수, 박민경)  


</br>

프로젝트 백앤드 url: http://54.180.112.53:8080/api/hot_articles ( 메인 데이터 목록 url 입니다.)  
- 코드 관리 차원에서 클론 코딩 DB 구출을 위한 크롤러 코드는 다른 프로젝트를 만들어 관리하였습니다.  
- 크롤러 코드: https://github.com/joychae/Springboot-Cloneproject-Daangn-Crawler

</br>

개발 언어
---------
- Backend: Java 8
- Frontend: React


</br>

개발 환경
---------
- Java: JDK 1.8.0  
- IDE: IntelliJ IDEA 2020.3.3 x64  
- Build Management: Gradle  
- Framework: SpringBoot  
> - ORM: Spring Data JPA  
> - Sub-Framework: Spring security  


</br>

폴더 구조
---------
```
main
java
com.clone.daangnclone
│  │  
├─ config
│  └─ SecurityConfig.java
│  │  
├─ controller
│  └─ AuthController.java
│  └─ ProductController.java
│  └─ UserController.java
│  │  
├─ dto
│  └─ LoginDto.java
│  └─ ProductRequestDto.java
│  └─ TokenDto.java
│  └─ UserDto.java
│  │  
├─ entity
│  └─ Authority.java
│  └─ Product.java
│  └─ User.java
│  │  
├─ jwt
│  └─ JwtAccessDeniedHandler.java
│  └─ JwtAuthenticationEntryPoint.java
│  └─ JwtFilter.java
│  └─ JwtSecurityCongif.java
│  └─ TokenProvider.java
│  │  
├─ repository
│  └─ AuthorityRepository.java
│  └─ ProductRepository.java
│  └─ UserRepository.java
│  │ 
├─ service
│  └─ CustomUserDetailsService.java
│  └─ ProductService.java
│  └─ UserService.java
│  │ 
├─ util
│  └─ SecurityUtil.java
│  │ 
└─  DanngnCloneApplication
```



