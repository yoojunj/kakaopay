# kakaopay
<br>
<br>개발 프레임워크
<br>  Java 8 + Spring Boot + JPA
<br>    
<br>테이블 설계
<br>  payment, payment_cancel 테이블 구성.
<br>  부분 취소인 경우를 고려해 OneToMany 구조를 잡았으나 시간 관계상 구현 못함.
<br>  
<br>  create table payment (
<br>      `id` bigint AUTO_INCREMENT
<br>    , `uuid` varchar(20) not null
<br>    , `amount` bigint not null
<br>    , `vat` bigint not null default 0
<br>    , `installment_period` varchar(2) not null default '00'
<br>    , `forwarding_data` varchar(450) 
<br>    , `inserted_at` datetime default now()
<br>    , `updated_at` datetime default now()
<br>    , PRIMARY KEY (`id`)
<br>    , UNIQUE KEY `uk_payment_uuid`(`uuid`)
<br>  );
<br>
<br>  create table payment_cancel (
<br>    `id` bigint AUTO_INCREMENT
<br>    , `uuid` varchar(20) not null
<br>    , `payment_id` bigint not null
<br>    , `amount` bigint not null
<br>    , `vat` bigint not null default 0
<br>    , `installment_period` varchar(2) not null default '00'
<br>    , `forwarding_data` varchar(450) 
<br>    , `inserted_at` datetime default now()
<br>    , `updated_at` datetime default now()
<br>    , PRIMARY KEY (`id`)
<br>    , UNIQUE KEY `uk_cancel_uuid`(`uuid`)
<br>  );
<br>
<br>문제해결 전략
<br>  1. 주어진 String Data 명세대로 결제, 결제취소 Data 구성
<br>  2. 명세 규격에 맞춰 substring 사용
<br>
<br>빌드 및 실행 방법
<br>  빌드: gradle 이용해서 build
<br>  실행 방법: 
<br>    서버: build 후 나온 jar 파일 실행
<br>    IDE: intellij
<br>
