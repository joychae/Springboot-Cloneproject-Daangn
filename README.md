Springboot-Cloneproject-Daangn-당근마켓
=====================

참여인원 : 백앤드 2명 (김동현, 채수연) / 프론트 2명 (김광수, 박민경)  


</br>

프로젝트 백앤드 url: http://54.180.112.53:8080/api/hot_articles ( 메인 데이터 목록 url 입니다.)  
- 코드 관리 차원에서 클론 코딩 DB 구축을 위한 크롤러 코드는 다른 프로젝트를 만들어 관리하였습니다.  
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


</br>

기능 및 API 설계
---------

<img width="492" alt="clone_api_structure" src="https://user-images.githubusercontent.com/79817873/113748415-6c450e00-9743-11eb-9bfb-ee9faf7db8ce.PNG">


</br>

매물(Product) 관련 기능
---------

### Product

```java
@Entity
@Getter
@Table(name = "product")
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "ID")
    private Long id;

    @Column(length = 3000000)
    private String imgs;
    @Column
    private String nickname;
    @Column
    private String region;
    @Column
    private String title;
    @Column
    private String category;
    @Column
    private String createdAt;
    @Column
    private String price;
    @Column(length = 20000)
    private String contents;
    @Column
    private String chat;
    @Column
    private String likeNum;
    @Column
    private String viewNum;
    @Column
    private String daangnProductId;


    public Product(String imgs ,String contents,String nickname, String region, String title, String category, String createdAt, String price, String chat, String like, String view, String daangnProductId) {
        this.imgs = imgs;
        this.contents= contents;
        this.nickname = nickname;
        this.region = region;
        this.title = title;
        this.category = category;
        this.createdAt = createdAt;
        this.price = price;
        this.chat = chat;
        this.likeNum = like;
        this.viewNum = view;
        this.daangnProductId = daangnProductId;

    }

    public Product(ProductRequestDto requestDto){
        this.imgs = requestDto.getImg();
        this.title = requestDto.getTitle();
        this.category = requestDto.getCategory();
        this.price = requestDto.getPrice();
        this.contents = requestDto.getContents();
        this.createdAt =requestDto.getCreatedAt();
    }
}
```

- 당근 마켓에 업로드된 매물이 가지고 있는 항목 전부를 멤버 변수로 생성하였습니다.  
- 기본 생성자와 생성자 두 개를 만들었습니다.

### ProductRepository

```java
public interface ProductRepository extends JpaRepository<Product, Long> {

    Product findByDaangnProductId(String id);
    List<Product> findByNicknameContaining(String keyword);
    List<Product> findByTitleContaining(String keyword);
    List<Product> findByContentsContaining(String keyword);
    List<Product> findByRegionContaining(String keyword);
}
```

- Spring Data JPA 를 사용하기 위해 Product Entity와 JpaRepository를 연결해줍니다.  
- 기본적으로 사용할 수 있는 명령어 뿐만 아니라 검색 기능 구현을 위해 추가로 만들어서 사용해야 하는 명령어를 입력해주었습니다.  

### ProductService

```java

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    // 인기 매물 목록 조회
    public List<Product> findAllProduct() {
        return productRepository.findAll();
    }

    // 인기 매물 상세 조회
    public Product findProductDetail(String id) {
        return productRepository.findByDaangnProductId(id);
    }

    // 지역 매물 목록 조회
    public List<Product> find_hot(String want){
        System.out.println(want);
        return productRepository.findByRegionContaining(want);
    }

    // 매물 업로드
    public void createProduct(ProductRequestDto requestDto){
        Product product = new Product(requestDto);
        productRepository.save(product);
    }

    // 검색 매물 목록 조회
    public List<Product> searchProduct(String keyword) {
        List<Product> searchlist = new ArrayList<>();
        searchlist.addAll(productRepository.findByNicknameContaining(keyword));
        searchlist.addAll(productRepository.findByTitleContaining(keyword));
        searchlist.addAll(productRepository.findByContentsContaining(keyword));
        searchlist.addAll(productRepository.findByRegionContaining(keyword));

        // 제목과 내용에 동일한 키워드가 존재한다면, serchlist에 중복값이 생기기 때문에 controller로 searchlist를 반환하기 전에 중복값을 제거하는 코드입니다.
        searchlist = searchlist.parallelStream().distinct().collect(Collectors.toList());
        return searchlist;
    }

}

```

- ProductService 코드를 보면, Product 관련한 모든 기능들을 파악할 수 있도록 코드를 구성하였습니다. ProductController에 바로 ProductRepository를 주입받아 사용하지 않고 비즈니스 로직은 꼭 Service Layer를 거치도록 코드를 구성했기에 Service Layer만 보고도 Product 관련한 기능이 어떤 것들이 있는지 전부 파악이 가능합니다.  

### ProductController

```java

@RestController
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    // 인기 매물 목록 조회
    @GetMapping("/api/hot_articles")
    public List<Product> getPopularProducts() {
        List<Product> popluarlist =  productService.findAllProduct();
        Collections.shuffle(popluarlist);
        return popluarlist;
    }

    // 인기 매물 상세 조회
    @GetMapping("/api/hot_articles/{id}")
    public Product getProductDetail(@PathVariable String id) {
        return productService.findProductDetail(id);
    }

    // 지역별 매물 목록 조회
    @GetMapping(value = {"/api/region/{val2}/{val3}", "/api/region/{val2}"})
    public List<Product> find_hot( @PathVariable String val2, @PathVariable(required = false) String val3){
        if (val3 == null){
            return productService.find_hot(val2);
        }
        String want = val2 +" "+val3;
        return productService.find_hot(want);
    }

    // 매물 업로드
    @PostMapping("/api/upload")
    public void PostProduct(@RequestBody ProductRequestDto requestDto){
        productService.createProduct(requestDto);
    }

    // 매물 검색 목록 조회
    @GetMapping("api/search/{query}")
    public List<Product> getSearchResults(@PathVariable String query) {
        return productService.searchProduct(query);
    }
}

```

- 비즈니스 로직은 최대한 Service Layer에서 처리하여 Controller 코드를 최대한 간결하게 구성하였습니다. 덕분에 어느 url에 어떤 기능이 매핑되어 있는지 Controller Layer에서 한 눈에 확인하기 수월해졌습니다.


</br>


</br>

회원가입 기능
------------------------

### UserController
```java
    // 회원가입 정보를 받아오는 메소드 이다.
    // UserDto 를 이용해서 username, password, nickname 값을 받아옵니다.
    @PostMapping("/signup")
    public ResponseEntity<User> signup(
            @Valid @RequestBody UserDto userDto
    ) {
        return ResponseEntity.ok(userService.signup(userDto));
    }
```

- UserDto를 활용하여 username, password, nickname 값을 POST 형태로 클라이언트에서 받아옵니다.

### UserService
```java
    // 회원가입 로직을 처리하는 메소드이다.
    // username 을 이용해 이미 db에 가입되어 있는 유저인지 확인한 후, db 에 없는 username 이라면 회원가입 로직을 진행합니다.
    // userDto 에서 username, password(인코딩 수행), nickname 값을 받아 User table 에 저장합니다.
    @Transactional
    public User signup(UserDto userDto) {
        if (userRepository.findOneWithAuthoritiesByUsername(userDto.getUsername()).orElse(null) != null) {
            throw new RuntimeException("이미 가입되어 있는 유저입니다.");
        }

        //빌더 패턴의 장점
        Authority authority = Authority.builder()
                .authorityName("ROLE_USER")
                .build();

        User user = User.builder()
                .username(userDto.getUsername())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .nickname(userDto.getNickname())
                .authorities(Collections.singleton(authority))
                .activated(true)
                .build();

        return userRepository.save(user);
    }
```
- 받아온 username으로 User DB에 겹치는 값이 있는지 확인하고 있다면 에러 메시지를, 없다면 패스워드 인코딩 과정을 거쳐 User Table에 저장합니다.  
- 이로써 회원가입이 완료됩니다.

### User
```java
@Entity
@Table(name = "user")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @JsonIgnore
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(name = "username", length = 50, unique = true)
    private String username;

    @JsonIgnore
    @Column(name = "password", length = 100)
    private String password;

    @Column(name = "nickname", length = 50)
    private String nickname;

    @JsonIgnore
    @Column(name = "activated")
    private boolean activated;

    @ManyToMany
    @JoinTable(
            name = "user_authority",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "authority_name", referencedColumnName = "authority_name")})
    private Set<Authority> authorities;
}
```
- 회원 정보를 관리하는 User 테이블입니다.


</br>


</br>

JWT를 활용한 로그인 기능
------------------------

회원 인증을 위해 JWT (JSON Web Token)을 사용합니다.  
JWT는 URL 파라미터로 전달하는 등 여러 방법으로 전달될 수 있는데, 이번 프로젝트에서는 HTTP 헤더에 넣어서 전달하였습니다.  


## 1) Spring Security Filter Chain 을 사용한다는 것을 선언하는 단계
### SecurityConfig
```java

// Spring Security 를 사용하기 위해서는 Spring Security Filter Chain 을 사용한다는 것을 명시해야 한다.
// 이는 WebSecurityConfigurerAdapter 를 상속받은 클래스에 @EnableWebSecurity 어노테이션을 달아주면 해결된다.
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final TokenProvider tokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    public SecurityConfig(
            TokenProvider tokenProvider,
            JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
            JwtAccessDeniedHandler jwtAccessDeniedHandler
    ) {
        this.tokenProvider = tokenProvider;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.jwtAccessDeniedHandler = jwtAccessDeniedHandler;
    }

    // 필수 Config 설정, Springboot 는 passwordEncoder 를 설정해주지 않으며, 이부분은 개발자가 직접 등록해주어야 한다.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring()
                .antMatchers(
                        "/h2-console/**"
                        ,"/favicon.ico"
                        ,"/error"
                        ,"/api/**"
                );
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                // token을 사용하는 방식이기 때문에 csrf를 disable합니다.
                .csrf().disable()

                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)

                .and()
                .headers()
                .frameOptions()
                .sameOrigin()

                // 중요! 세션을 사용하지 않기 때문에 STATELESS로 설정
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .authorizeRequests()
                // 이하 세 줄의 요청에 대해서는 로그인을 요구하지 않는다.
                .antMatchers("/api/hello").permitAll()
                .antMatchers("/api/authenticate").permitAll()
                .antMatchers("/api/signup").permitAll()

                // 그 외 나머지 요청에 대해서는 로그인을 요구한다.
                .anyRequest().authenticated()

                .and()
                // 로그인 요청이 오면, AuthenticationFilter 에 해당하는 JwtFilter 로 요청이 간다.
                .apply(new JwtSecurityConfig(tokenProvider));

    }
}

```
- 클라이언트에서 Request 요청이 오면, Spring Security Filter Chain을 가장 먼저 거치게 됩니다.
- Spring Security Filter Chain은 WebSecurityConfigurerAdapter를 상속받은 클래스에 @EnableWebSecurity 어노테이션을 달아줌으로서 구현할 수 있습니다. (커스터 마이징도 가능합니다.)
- 위의 SecurityConfig 에서는 인증이 필요한 요청이 오면, 커스터 마이징한 JwtSecurityConfig를 실행합니다.
- JwtSecurityConfig는 다음과 같습니다.


</br>

## 2) 커스터마이징한 Filter가 실행되도록 선언하는 단계
### JwtSecurityConfig
```java
public class JwtSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    // tokenProvider 의존성 주입 받는다.
    private TokenProvider tokenProvider;

    public JwtSecurityConfig(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    // configure 메소드를 오버라이드 해서 JwtFilter 를 통해 Security 로직에 필터를 등록한다.
    // UsernamePasswordAuthenticationFilter 가 실행되기 전에 커스텀한 JwtFilter 가 실행되도록 지정한다.
    // 이를 SecurityConfig 흐름에도 적용시켜 주어야 한다.
    @Override
    public void configure(HttpSecurity http) {
        JwtFilter customFilter = new JwtFilter(tokenProvider);
        // username, password 를 검사하는 필터를 내가 만튼 커스텀 필터로 하겠다는 지시
        http.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
```
- Spring Security 디폴트 필터인 UsernamePasswordAuthenticationFilter 가 실행되기 전, 직접 만든 JwtFilter가 실행되도록 지정합니다.


</br>

## 3) 직접 만든 JwtFilter 가 실행되는 단계
### JwtFilter
```java
// TokenProvider 로 토큰을 생성하고 검증하는 컴포넌트를 완성했지만, 실제로 이 컴포넌트를 이용해 인증 작업을 진행하는 것은 Filter 이다.
public class JwtFilter extends GenericFilterBean {

    private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);

    public static final String AUTHORIZATION_HEADER = "Authorization";

    // TokenProvider 를 의존성 주입받는다.
    private TokenProvider tokenProvider;

    public JwtFilter(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    // GenericFilterBean 클래스를 상속 받아서, doFilter 메소드를 오버라이드 한다.
    // 실제 필터링 로직은 doFilter 메소드에 작성된다.
    // 토큰의 인증정보를 SecurityContext 에 저장하는 역할을 수행한다.
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        // 1.resolveToken 을 통해 토큰을 받아와서
        String jwt = resolveToken(httpServletRequest);
        String requestURI = httpServletRequest.getRequestURI();

        // 2.유효성 검증을 하고
        if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
            // 3. 정상 토근이면 getAuthentication 메소드로 토큰값을 이용해 인증된 Authenticaiton 객체를 반환
            Authentication authentication = tokenProvider.getAuthentication(jwt);
            // 4.인증된 Authentication 객체를 SecurityContext 에 저장한다.
            SecurityContextHolder.getContext().setAuthentication(authentication);
            logger.debug("Security Context에 '{}' 인증 정보를 저장했습니다, uri: {}", authentication.getName(), requestURI);
        } else {
            logger.debug("유효한 JWT 토큰이 없습니다, uri: {}", requestURI);
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    // Token 값을 해석해주는 메소드이다.
    // 헤더를 통해 온 token 값을 복호화 한다.
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
```
### TokenProvider

```java
// JWT 를 생성하고 검증하는 컴포넌트이다.
// JWT 에는 토큰 만료 시간이나 회원 권한 정보 등을 저장할 수 있다.
// InitializingBean 을 implements 한다.
@Component
public class TokenProvider implements InitializingBean {

    private final Logger logger = LoggerFactory.getLogger(TokenProvider.class);

    private static final String AUTHORITIES_KEY = "auth";

    private final String secret;
    private final long tokenValidityInMilliseconds;
    private Key key;

    // 0.TokenProvider 생성자이다. secret 과 tokenValidityInMilliseconds, key 를 멤버변수로 가지고 있다.
    // 1.application.yml 에서 설정한 secret key 값과 만료시간을 @Value 를 이용해 받아온다.
    // 2.받아온 secret key 값과 tokenValidityInSeconds 를 각각 secret, tokenValidityMilliseconds 에 넣어준다.
    public TokenProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.token-validity-in-seconds}") long tokenValidityInSeconds) {
        this.secret = secret;
        this.tokenValidityInMilliseconds = tokenValidityInSeconds * 1000;
    }

    // 3.InitializingBean 이 생성이 되고 주입을 받은 후에, secret 값을 Base64 Decode 해서 key 변수에 할당한다.
    // 자, 이제 모든 멤버변수가 채워졌다.
    @Override
    public void afterPropertiesSet() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    // Authentication 객체의 권한 정보를 이용해서 토큰을 생성하는 createToken 메소드이다.
    // authentication 을 parameter 로 받아서 jwt 토큰을 생성해서 리턴한다.
    public String createToken(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = (new Date()).getTime();
        Date validity = new Date(now + this.tokenValidityInMilliseconds);

        // jwt 토큰을 생성해서 리턴한다.
        return Jwts.builder()
                .setSubject(authentication.getName())  // 정보 저장
                .claim(AUTHORITIES_KEY, authorities) // 정보 저장
                .signWith(key, SignatureAlgorithm.HS512) // signature 에 들어갈 키 값과 사용할 암호화 알고리즘 세팅
                .setExpiration(validity) // set Expire Time
                .compact();
    }

    // JWT 토큰에서 인증 정보 조회
    // 토큰에 담겨 있는 정보를 이용해 Authentication 객체를 리턴하는 메소드이다.
    // createToken 과 정확히 반대의 역할을 해주는 메소드이다.
    // 토큰을 parameter 로 받아서 토큰으로 claim 을 만들고, 최종적으로는 Authentication 객체를 리턴한다.
    public Authentication getAuthentication(String token) {
        Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        User principal = new User(claims.getSubject(), "", authorities);

        // 인증 완료 후의 authentication 객체를 생성한다.
        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    // 토큰의 유효성 검증을 수행하는 매소드이다.
    // token 을 parameter 로 받아 파싱을 해보고 나오는 exception 들을 캐치, 문제가 있으면 false, 정상이면 true 를 반환한다.
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            logger.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            logger.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            logger.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            logger.info("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }
}
```
- Http 헤더에 실린 토큰 값을 받아와서 토큰 값을 resolveToken 메소드로 복호화 합니다.
- 복호화된 토큰값을 tokenProvider의 validateToken 메소드를 이용해 유효성 검사를 실행합니다.
- 토큰 유효성 검사를 통과한다면, tokenProvider의 getAuthentication 메소드가 token 값을 이용해 인증된 Authentication 객체를 반환합니다.
- 인증 단계가 완료되었습니다!


</br>

## 4) 인증된 사용자 정보를 가져오기 위한 단계
### CustomUserDetailsService
```java
// 토큰에 저장된 유저 정보를 활용해야 하기 때문에 CustomUserDetailService 라는 이름의 클래스를 만든다.
@Component("userDetailsService")
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(final String username) {
        return userRepository.findOneWithAuthoritiesByUsername(username)
                .map(user -> createUser(username, user))
                .orElseThrow(() -> new UsernameNotFoundException(username + " -> 데이터베이스에서 찾을 수 없습니다."));
    }

    private org.springframework.security.core.userdetails.User createUser(String username, User user) {
        if (!user.isActivated()) {
            throw new RuntimeException(username + " -> 활성화되어 있지 않습니다.");
        }
        List<GrantedAuthority> grantedAuthorities = user.getAuthorities().stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getAuthorityName()))
                .collect(Collectors.toList());
        return new org.springframework.security.core.userdetails.User(user.getUsername(),
                user.getPassword(),
                grantedAuthorities);
    }
}

```
- Spring Security의 기본 인터페이스인 UserDetailsService를 상속받는다.
- UserDetails 객체를 만들기 위한 클래스로 활용된다.
- USerDetails는 따로 implements받은 클래스를 생성하기도, 기존 커스텀 User Entity에 implements하기도 하며 아무 곳에도 implements 하지 않고 사용할 수 있다.
- UserDetails는 인증에 성공한 사용자의 정보를 가져 올 때 가장 많이 활용되며, Authentication 객체를 구현한 UsernamePasswordAuthenticationToken을 생성하기 위해서도 사용된다.


</br>

## 기타) 첫 로그인 시 토큰 생성 단계
### AuthController
```java
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api")
public class AuthController {

    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    public AuthController(TokenProvider tokenProvider, AuthenticationManagerBuilder authenticationManagerBuilder) {
        this.tokenProvider = tokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
    }

    // 로그인 정보를 받아오는 메소드이다.
    // LoginDto 를 통해 로그인 하고자 하는 유저의 username 과 password 정보를 받아온다.
    @PostMapping("/authenticate")
    public ResponseEntity<TokenDto> authorize(@Valid @RequestBody LoginDto loginDto) {

        // 중요! 받아온 username 과 password 값으로 인증 전의 Authentication 객체를 생성
        // Authentication 객체를 구현한 UsernamePasswordAuthenticaitonToken 객체를 구현하기 위해서 UserDetails 객체를 이용
        // 그 과정에서 UserDetailsService 에 접근하고, User Table 에 아이디가 없는 유저라면 loadUserByUsername 의 에러 메시지가 반환된다.
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        // 해당 값을 SecurityContext 에 저장한다.
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.createToken(authentication);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);

        return new ResponseEntity<>(new TokenDto(jwt), httpHeaders, HttpStatus.OK);
    }
}
```
- username과 password 값을 클라이언트로부터 받아온다.
- username과 password 값으로 인증 전 UsernamePasswordAuthentication 객체를 만든다. 해당 객체를 생성하는 과정에서 CustomUserDetailsService가 실행되며, User 테이블에 같은 아이디를 사용하는 유저가 없을 시 에러 문구가 반환된다. 매칭되는 아이디를 가진 User가 있다면, UsernamePasswordAuthentication 객체가 만들어진다.
- 해당 authentication 객체를 가지고 tokenProvider의 createToken 메소드가 token을 만들어 반환해준다.
- 로그인에 성공한 유저는 해당 token 값을 반환받으며, 이후 get 요청 시 해당 token 값을 헤더에 실어서 보내주면 위에 설명해둔 1~3 단계를 거쳐 필터를 통과하여 인증이 필요한 url 에 접근할 수 있게된다.
