
<h1 align="center" style='font-family: palatino Linotype'>EasyFarm</h1>
<h1 align="center" style='font-family: palatino Linotype'><img src="img/splashImage.png" width="200" height="400"></h1>

# 주제
안드로이드와 인공지능을 이용한 노지 작물 병해충 검출 및 분류 플랫폼 구축 Capstone Design 프로젝트

## 기술 스택
Kotlin Retrofit Node.js Flask Firebase

## 기간
2022.09 ~ 2022.12

# [시연영상](https://www.notion.so/jinyong3512/Easy-Farm-b782bb8e366b468d84dfd94d0c1dc9b2)

## 구현 내용
>1. [완료] 딥러닝을 이용한 병해충 판단 서비스
>2. [완료] 병해충 도감 서비스
>3. [완료] 병해충 기록 조회 서비스
>4. [완료] 예보를 통한 푸시 알림 서비스
>5. [완료] 메모장 서비스

## 프로젝트 진행
- 병해충을 판단해줄 전문 인력 부족으로 앱을 통한 간편한 진단 서비스
- 안드로이드 1명,서버 1명, 인공지능 2명 총 4명이 팀을 이루어 제작한 프로젝트
- 안드로이드 부분을 담당하여 UI/UX 디자인과 구현, 서버와 데이터 통신을 담당

## 맡은 구현
- 전체적인 UI/UX 구현

    - 홈 화면
    <br>
    <img src="img/home.jpg" width="200" height="400">
    
    - 메뉴 화면
    <br>
    <img src="img/menu.jpg" width="200" height="400">

    - 설정 화면
    <br>
      <img src="img/setting.jpg" width="200" height="400">

- 병해충 진단

    - 카메라 촬영 및 갤러리 이미지 서버로 업로드
    <br>
    <img src="img/picture.jpeg" width="200" height="400">

    - 인공지능 진단 결과 서버로부터 받아서 출력
    <br>
    <img src="img/result.jpeg" width="200" height="400">

- 도감
    - 진단하지 않고도 병해충 관련 정보를 NCPMS에서 확인 가능
    <br>
    <img src="img/dictionary.jpeg" width="200" height="400">

- 예보
    - 사용자의 GPS를 이용해 해당 지역 병해충 격상 단계 확인 가능
    <br>
    <img src="img/forecast.jpeg" width="200" height="400">

- 나의 기록
    - 로그인하는 번거로움 없이 안드로이드 디바이스 고유 ID를 이용하여 DB에서 기록을 가져와 보여 줌
    <br>
    <img src="img/myRecord.jpg" width="200" height="400">

- 메모장
    - 작물이나 병해충관련 메모 가능
    <br>
    <img src="img/memo.jpg" width="200" height="400">

- 푸시알림
    - 병해충 위험 관련 경보시 Firebase를 이용한 푸시 알림
    <br>
    <img src="img/alarm.jpg" width="200" height="400">

## 어려움
<aside>
💡 서버로 이미지와 식물 종류를 어떻게 전송하지?

</aside>

- ❗MultipartBody.Part로 보내면 된다
- ❗retrofit2 post image https://machine-woong.tistory.com/171
<aside>
💡 Splash에서 유저 정보(디바이스 ID, 위도, 경도 데이터)를 구하는데 가끔 안구한다

</aside>

- ❓구하는 작업이 비동기적으로 처리되어 MainActivity로 넘어가버리면 오류 발생
- ❗유저 정보를 먼저 확실히 구하고 그 뒤에 코드를 붙여 써서 통신을 하고 1초 강제로 기다리게 한뒤에 MainActivity로 전환한다
<aside>
💡 나의 기록 ListView에서 아이템 삭제 시 아이템의 정보가 밀려서 보인다

</aside>

- ❓원래의 정보가 남아 있다 Adapter를 갱신하지 않아서 그런걸로 판단
- ❗ListView https://recipes4dev.tistory.com/45

## 프로젝트 후기

- 좋은 점
    - 팀원들과의 원활한 협업을 위해 DB와 인공지능 모델은 없는 로컬 서버를 개인적으로 돌려보며 통신을 연습해가서 팀원들과 협업할때 쉽게 진행되고 서버도 조금이나마 경험해봤다
    - 사용자의 편의를 중점적으로  직접 UI/UX를 디자인하며 팀원들과 논의하여 수정해나가는 협업을 했다

- 부족한 점
    - 안드로이드 부분에 집중하느라 정확히 DB와 인공지능 학습에서는 어떤 일이 일어나는지 공부하지 못했다
