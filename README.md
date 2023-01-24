# SpringBoot-Project-BookStore

- **도서 쇼핑몰 사이트 API 개발**

<br>

### 🔥 프로젝트 목적
  
- 영풍문고의 나우드림 서비스를 이용할 때, 재고보다 (실시간으로) 구매하려는 사람이 더 많을 경우 문제가 발생할 수 있습니다.
- **프로젝트를 진행하며 동시성 문제를 해결하고, 이 외에 다른 API도 구현하며 다양한 기술을 학습하는 목적으로 기획했습니다.**

> **나우드림**  **:** 온라인 할인이 적용된 도서를 주문하고, 매장에서 직접 수령하는 서비스입니다.

<br>

##  개발 기간 
* 22.12.19 - 23.01.23

<br>

##  기술 스택 

- **Backend:**
  	<img src="https://img.shields.io/badge/Java 11-E06A03">
  	<img src="https://img.shields.io/badge/Spring Boot 2.7.7-77A546">

- **DataBase:**
	<img src="https://img.shields.io/badge/AWS RDS MariaDB 10.6.11-002D40">
	<img src="https://img.shields.io/badge/Redis 7.0.7-9F1E10">
	<img src="https://img.shields.io/badge/Elasticsearch 7.17.8-3DB7AA">

- **DevOps:**
	<img src="https://img.shields.io/badge/Docker-026CB0">
	<img src="https://img.shields.io/badge/AWS EC2 Ubuntu-ED7025">
	<img src="https://img.shields.io/badge/GitHub-121319">

- **Library:**
	<img src="https://img.shields.io/badge/Jasypt-838383">
	<img src="https://img.shields.io/badge/Spring Security-838383">
	<img src="https://img.shields.io/badge/JWT-838383">
	<img src="https://img.shields.io/badge/Redisson-838383">
	<img src="https://img.shields.io/badge/JUnit 4-838383">
	<img src="https://img.shields.io/badge/Mockito-838383">


- **Data:**
	<img src="https://img.shields.io/badge/Kibana 7.17.8-E55388">




- **IDE:**
	<img src="https://img.shields.io/badge/IntelliJ-8354A0">

- **Tool:**
	<img src="https://img.shields.io/badge/ERDCloud-494B92">
	<img src="https://img.shields.io/badge/Postman-F76936">




<br>

## 프로젝트 구조
<img width="1307" alt="image" src="https://user-images.githubusercontent.com/111635735/214366894-5abcef37-5d6f-4cb0-b1a7-db770753f75c.png">


##  모델링

![bookstore](https://user-images.githubusercontent.com/111635735/214349480-424d970b-d790-4cab-a712-19c1ae4bc2ab.png)


[ERDCloud-bookstore Link](https://www.erdcloud.com/d/bRY643ayTHdwAZ557)


<br>





## 📌 주요 기능

#### (공통) 요청에 대한 응답, 예외 처리
- 요청 성공 시 dto.common.ApiResponse로 응답합니다.
- 예외 발생 시 ResponseEntity로 응답합니다.

<br>

#### 1. 회원가입
- 이메일 중복 시 예외를 발생시킵니다.
- 비밀번호는 bcrypt를 이용해 암호화한 뒤 DB에 저장합니다.

<br>

#### 2. 로그인
- 가입된 이메일인지 확인합니다.
- 입력된 비밀번호와 유저 비밀번호가 일치하는지 확인합니다.
- 위 조건을 다 통과하면 JWT 토큰과 함께 응답합니다.

<br>


#### 3. 카테고리별 도서 조회(정렬 가능)
- 쿼리스트링 방식으로 sub-category, order, page 값을 요청받습니다.
- 서브카테고리가 존재하지 않을 경우 예외를 발생시킵니다.
- order 값이 잘못된 경우 예외를 발생시킵니다.
- 정렬 기준은 type/Order에 enum으로 정의했으며, 속성으로 Sort 객체를 정의합니다.
- 조건에 맞는 책리스트를 반환합니다.

```text

order value 

- new : 최신순
- high : 높은 가격순
- row : 낮은 가격순
- bestseller : 판매순

```

<br>

#### 4 도서 & 작가 통합 검색
- **docker를 이용해 ElasticSearch, nori 설치 및 연동**
	- title, authors 두 필드에 nori를 설정해 주었습니다.


- 쿼리스트링 방식으로 keyword, page 값을 요청받습니다.
- keyword(도서 or 작가)를 포함하는 책 리스트를 반환합니다.(ES 조회)

- [Kibana 저장된 도서 데이터 조회](http://15.164.245.51:5601/app/discover#/?_g=(filters:!(),refreshInterval:(pause:!t,value:0),time:(from:now-15m,to:now))&_a=(columns:!(),filters:!(),index:'7f648ac0-9abf-11ed-9529-c3e5e3c53eb8',interval:auto,query:(language:kuery,query:''),sort:!()))

<br>


#### 5. 장바구니 - <a href="https://github.com/junga970/bookstore/wiki/%F0%9F%93%8C-%EC%9E%A5%EB%B0%94%EA%B5%AC%EB%8B%88-CRUD"> 상세보기 </a>
- **장바구니에 도서 추가, 수량 업데이트, 삭제 기능**
- **장바구니 조회, 전체 삭제 기능**
- 장바구니 기능은 로그인한 유저만 사용이 가능합니다.

<br>


#### 6. 나우드림 - <a href="https://github.com/junga970/bookstore/wiki/%F0%9F%93%8C-%EB%82%98%EC%9A%B0%EB%93%9C%EB%A6%BC"> 상세보기 </a>
- **나우드림 주문 기능**
- **매장별 재고 조회 기능**
- 나우드림 주문 기능은 로그인한 유저만 사용이 가능합니다.
- 로그인한 유저만 나우드림 이용이 가능합니다.

<br>


#### 7. 주문 내역 조회
- 주문 내역 조회 관련 기능은 로그인한 유저만 사용이 가능합니다.
- order_info 테이블의 주문일자(order_date)에 index를 설정했습니다.
- 쿼리스트링 방식으로 start-date, end-date, page 값을 요청받습니다.
	- 설정한 기간(start-date ~ end-date)의 주문내역을 조회할 수 있습니다.

- **주문 상세내역 조회**는 order_info_id를 요청받으며, 이에 해당하는 상세내역을 조회할 수 있습니다.


<br>

#### 8. 관리자 도서 등록
- **도서 등록 시 RDB와 ES에 저장되며, 트랜잭션을 적용했습니다.**
- 도서 등록 기능은 관리자만 사용이 가능합니다.


<br>


## Postman API 문서

**기능에 대한 요청 & 응답을 자세히 확인할 수 있습니다.**

[https://documenter.getpostman.com/view/17844607/2s8ZDbWLSy](https://documenter.getpostman.com/view/17844607/2s8ZDbWLSy)



<br>


## 폴더 구조

```
.
├── Dockerfile
├── docker-compose.yml
└── src
    ├── main
    │   ├── generated
    │   ├── java
    │   │   └── com
    │   │       └── example
    │   │           └── bookstore
    │   │               ├── BookstoreApplication.java
    │   │               ├── config
    │   │               │   ├── JasyptConfig.java
    │   │               │   ├── JpaAuditingConfig.java
    │   │               │   ├── RedissonConfig.java
    │   │               │   ├── SecurityConfig.java
    │   │               │   ├── elasticsearch
    │   │               │   │   ├── AbstractElasticsearchConfiguration.java
    │   │               │   │   └── ElasticSearchConfig.java
    │   │               │   └── jwt
    │   │               │       ├── CustomAuthenticationEntryPoint.java
    │   │               │       ├── JwtAuthenticationFilter.java
    │   │               │       └── JwtTokenProvider.java
    │   │               ├── controller
    │   │               │   ├── BookController.java
    │   │               │   ├── CartController.java
    │   │               │   ├── NowDreamController.java
    │   │               │   ├── OrderController.java
    │   │               │   └── UserController.java
    │   │               ├── dto
    │   │               │   ├── Token.java
    │   │               │   ├── common
    │   │               │   │   ├── ApiResponse.java
    │   │               │   │   └── ErrorResponse.java
    │   │               │   ├── condition
    │   │               │   │   ├── BookCondition.java
    │   │               │   │   ├── CartItemCondition.java
    │   │               │   │   ├── NowDreamStockCondition.java
    │   │               │   │   ├── OrderDetailCondition.java
    │   │               │   │   └── OrderInfoCondition.java
    │   │               │   └── request
    │   │               │       ├── BookRequest.java
    │   │               │       ├── CartUpdateRequest.java
    │   │               │       ├── LoginRequest.java
    │   │               │       ├── NowDreamOrderRequest.java
    │   │               │       └── RegisterRequest.java
    │   │               ├── entity
    │   │               │   ├── BaseEntity.java
    │   │               │   ├── Book.java
    │   │               │   ├── BookDocument.java
    │   │               │   ├── CartItem.java
    │   │               │   ├── Category.java
    │   │               │   ├── OrderDetail.java
    │   │               │   ├── OrderInfo.java
    │   │               │   ├── Store.java
    │   │               │   ├── SubCategory.java
    │   │               │   └── User.java
    │   │               ├── exception
    │   │               │   ├── CustomException.java
    │   │               │   └── GlobalExceptionHandler.java
    │   │               ├── repository
    │   │               │   ├── BookRepository.java
    │   │               │   ├── BookSearchRepository.java
    │   │               │   ├── CartItemRepository.java
    │   │               │   ├── OrderDetailRepository.java
    │   │               │   ├── OrderInfoRepository.java
    │   │               │   ├── StoreRepository.java
    │   │               │   ├── SubCategoryRepository.java
    │   │               │   └── UserRepository.java
    │   │               ├── service
    │   │               │   ├── BookService.java
    │   │               │   ├── CartService.java
    │   │               │   ├── NowDreamService.java
    │   │               │   ├── OrderService.java
    │   │               │   ├── UserDetailsServiceImpl.java
    │   │               │   └── UserService.java
    │   │               └── type
    │   │                   ├── ErrorCode.java
    │   │                   ├── Order.java
    │   │                   ├── ResponseCode.java
    │   │                   └── UserRole.java
    │   └── resources
    │       ├── application.yml
    │       ├── elastic
    │       │   ├── book-mapping.json
    │       │   └── book-setting.json
    │       ├── static
    │       └── templates
    └── test
        └── java
            └── com
                └── example
                    └── bookstore
                        └── service
                            ├── BookServiceTest.java
                            ├── CartItemServiceTest.java
                            ├── NowDreamServiceTest.java
                            ├── OrderServiceTest.java
                            ├── UserDetailsServiceImplTest.java
                            └── UserServiceTest.java

```





