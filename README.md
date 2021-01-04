# kakaopay
\n
\n개발 프레임워크
\n  Java 8 + Spring Boot + JPA
    
테이블 설계
  payment, payment_cancel 테이블 구성.
  부분 취소인 경우를 고려해 OneToMany 구조를 잡았으나 시간 관계상 구현 못함.
  
  create table payment (
      `id` bigint AUTO_INCREMENT
    , `uuid` varchar(20) not null
    , `amount` bigint not null
    , `vat` bigint not null default 0
    , `installment_period` varchar(2) not null default '00'
    , `forwarding_data` varchar(450) 
    , `inserted_at` datetime default now()
    , `updated_at` datetime default now()
    , PRIMARY KEY (`id`)
    , UNIQUE KEY `uk_payment_uuid`(`uuid`)
  );

  create table payment_cancel (
    `id` bigint AUTO_INCREMENT
    , `uuid` varchar(20) not null
    , `payment_id` bigint not null
    , `amount` bigint not null
    , `vat` bigint not null default 0
    , `installment_period` varchar(2) not null default '00'
    , `forwarding_data` varchar(450) 
    , `inserted_at` datetime default now()
    , `updated_at` datetime default now()
    , PRIMARY KEY (`id`)
    , UNIQUE KEY `uk_cancel_uuid`(`uuid`)
  );

문제해결 전략
  1. 주어진 String Data 명세대로 결제, 결제취소 Data 구성

빌드 및 실행 방법
  빌드: gradle 이용해서 build
  실행 방법: 
    서버: build 후 나온 jar 파일 실행
    IDE: intellij
