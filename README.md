> 해당 repository는 private repository의 일부를 복사한 소개 목적의 repository로 실제 실행은 불가합니다.

<br>

# hello-world-auto-store

## Overview

 * [쿠팡(Coupang) Open API](https://travel-developers.coupang.com/) 를 사용하여 구매대행 상품 등록, 판매 자동화 프로젝트
 * 소싱 플랫폼으로 [아마존](https://www.amazon.com/) (미국), [알리바바](https://korean.alibaba.com/) (중국), [알리익스프레스](https://ko.aliexpress.com/?spm=a2g0o.home.1000002.1.3d424430V4f87P) (중국), [라쿠텐](https://www.rakuten.co.jp/) (일본) 등 확장 예정
 * 판매 플랫폼으로 오늘의집, 지마켓 등 API 를 제공하는 쇼핑몰에 확장 예정(네이버 스마트스토어의 경우 개인 개발자에게 API 제공이 되지 않는 것으로 확인됨)

### 구매대행?
 * 국내 정식 수입업체를 통해 들어오는 상품보다 해외에서 물건을 직접 살 때(해외직구) 더 저렴한 상품 
 * 국내에서 정식 수입되어 판매되지 않는 상품 등
 * 위와 같은 상품들을 해외에서 구매할 때, 한국으로 직배송 되지 않는 경우 배송 대행지(배대지) 를 사용해서 구매를 해야 함(미국 화장품 -> 미국 배대지 -> 한국 우리집)
 * 따라서 개인 구매자가 해외 상품을 구매하려고 할 때, 위와 같이 배대지를 거쳐 집으로 받는 절차를 대행 해줌으로써 수수료를 챙기는 방식
 * 개인 구매자가 중국 물건을 구매하고자 하는 경우, 알리바바 등의 사이트에 직접 들어가 중국어를 해석하고 물건을 배대지를 거쳐 받아봐야 함
 * 이 과정이 번거롭고 중국어를 모르면 구매가 어렵기 때문에 구매대행 업자들은 쿠팡, 네이버 스마트스토어 등에 소싱할 상품을 번역하여 등록하고, 구매가 이루어지면 배대지를 거치는 절차를 대신해 줌

## Tech-stack
 * Java 11
 * Gradle
 * Spring Boot
 * Kafka for event messaging

## System Architecture

### Version 4
![system-architecture](https://user-images.githubusercontent.com/35681772/159146403-1bcab3aa-02e4-49ae-9e88-829199832320.png)

#### Batch Processing
 * `상품 분석기` 는 팔릴 상품을 분석하여 결과를 특정 경로에 File 생성
 * `상품 관리자` 는 해당 File 을 읽어 상품 등록
   - 소싱할 대상이 되는 상품 상세 설명을 번역하고 관련 이미지, 리뷰, 유투브 영상을 제품 상세 정보에 추가
   - 판매 플랫폼에서 상품 등록에 필요한 벤더 정보를 추가하여 상품 등록

#### Stream Processing
 * `주문 관리자` 는 주문이 들어 왔을 때 상품 결제, 배대지 입력, 상품 배송 현황 추적
 * `CS 관리자` 는 고객 문의에 즉각 대응하기 위해 `주문 관리자` 와 Event-Driven 방식으로 통신

#### 참고
 * [인프라 다이어그램(#2)](https://github.com/kingwaggs/hello-world-auto-store/issues/2)
 * [Event-Driven Architectures with Kafka and Java Spring-Boot](https://itnext.io/event-driven-architectures-with-kafka-and-java-spring-boot-6ded048e86f3)

## Feature Introduction

 * [기능 목록 및 필요 기술 스택](https://github.com/kingwaggs/hello-world-auto-store/issues/1) 참고

### Product Analyzer
 * 상품 분석기
 * 어떤 상품을 판매할 지 결정하는 모듈이므로 수익을 결정하는 가장 중요한 기능 담당
 * __네이버 데이터랩__ 의 [통합 검색어 트렌드](https://developers.naver.com/docs/serviceapi/datalab/search/search.md#%ED%86%B5%ED%95%A9-%EA%B2%80%EC%83%89%EC%96%B4-%ED%8A%B8%EB%A0%8C%EB%93%9C-%EA%B0%9C%EC%9A%94), [쇼핑 인사이트](https://developers.naver.com/docs/serviceapi/datalab/shopping/shopping.md#%EC%87%BC%ED%95%91%EC%9D%B8%EC%82%AC%EC%9D%B4%ED%8A%B8-%EA%B0%9C%EC%9A%94) API 를 호출하여 팔릴 상품군을 파악
 * 해당 상품군의 상품들을 실제 물건을 소싱해올 사이트(아마존, 알리바바, 알리익스프레스 등) 에서 상품 단위 목록 선정
 * __네이버 스마트스토어__ , __쿠팡__ 에서 다른 판매자가 해당 상품을 판매중인 현황 및 가격 등을 파악해 __가중치__ 를 부여
    - 여러 판매자가 판매중인 상품군의 경우 내 상품이 팔릴 확률 낮아짐
    - 가격 경쟁력에서도 밀릴 가능성 높음
    - 상품군 단위의 비교도 유의미 할 것이라 생각됨
 * __보다 정교하게 팔릴 상품을 판단할 수 있도록 아이디어 논의 필요__

### Product Manager
 * 상품 등록
   - [파파고 번역 API](https://developers.naver.com/docs/papago/README.md) 를 이용해 소싱할 상품의 상세 정보를 번역하여 등록
   - 상품 관련 유투브 영상도 링크를 첨부하는 식으로 상세 설명에 보여질 수 있으면 좋을 것 같음
 * 상품 수정
   - 잘못된 정보 수정
   - 가격 수정 등
 * 상품 삭제
   - 쿠팡의 경우 한 셀러당 25000 개의 상품을 최대치로 제한하고 있음
   - 기본적으로 특정 주기별로 새로 갱신된 팔릴 상품 등록, 안 팔리는 상품 삭제(Scheduling)
 * API 로 제공하여 관리자가 직접 특정 상품을 제어할 수 있는 기능 필요
 * 소싱할 상품의 재고가 현재도 존재하는지 특정 주기로 체킹하여 반영하는 기능 필요

### Order Manager
* 주문 처리
* 구매가 들어온 상품에 대해 해당 상품을 실제 구매처리 하는 역할
* 소싱할 상품의 배송 주소 : 배송 대행지
* 배송 대행지 에서 물건을 전달받을 주소 : 구매한 고객 주소
* 따라서 API 를 제공해주는 배송 대행지가 있는지 리서치 필요
  - 없을 경우 구매 건에 대해 엑셀 파일 추출하여 해당 배송대행지에 자동 이메일 전송하여 주문하는 방법이 가능한 대안이라 보임
* CS 대응이 가능하도록 주문 처리된 건에 대해 현재 상품이 어디에 있는지 Tracking 하는 기능 API 로 제공 필요

### Customer-Service Manager
* 쿠팡 등 판매처의 Open API 를 호출하여 인입된 CS 문의에 즉시 답변하는 역할
* "현재 배송 상태 조회" 등의 기능이 있겠음
* 판매처(쿠팡, 네이버 스마트스토어, ..)상에 세부적으로 표시될 수 없는 배송 현황을 고객 문의 인입시 대응하는 역할

### Admin
* 소싱 상품 현황, batch 동작 현황, 월 판매 현황, 실제 매출액 추이 등
