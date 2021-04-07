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


</br>


</br>

JWT를 활용한 로그인 기능
------------------------
