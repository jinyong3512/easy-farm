
<h1 align="center" style='font-family: palatino Linotype'> Smart Notice Bot</h1>
<p align="center">
    <a href ="https://github.com/Smart-Notice-Bot/Smart.Notice.Bot/blob/main/LICENSE">
        <img src="https://img.shields.io/badge/license-Apache--2.0-blue?style=plastic&link=https://github.com/Smart-Notice-Bot/Smart.Notice.Bot/blob/main/LICENSE">
    </a>
</p>
<h4 align="center">
    <p>
        <b>한국어</b> |
        <a href="https://github.com/Smart-Notice-Bot/Smart.Notice.Bot/blob/main/README_en.md">English</a>
    <p>
</h4>
<br>
<p align='center' style='font-size:150%'>Smart Notice Bot은 이메일을 통해 대학교 공지사항을 알려주는 오픈소스 프로젝트입니다. </p>

<br>



## :star: 특징
1. (완료)홈페이지에 회원가입을 한 후 본인 학과, 이메일, 관심사를 선택한다.
2. (완료)공지사항 중에 사용자가 필요로 하는 것(취업, 창업, 대학원, 인턴 등)과 같은 글들이 갱신될 때마다 이메일로 보내준다.
3. (완료)자주하는 질문(FAQ)은 홈페이지에 제공한다.
4. (완료)누군가가 질문을 올리면 답변할 수 있는 게시판 서비스를 제공한다.

<br>

## :desktop_computer: 설치 환경
Python 3.7 & Jupyter notebook

<br>

## :white_check_mark: 실행방법
```
$ git clone https://github.com/Smart-Notice-Bot/Smart.Notice.Bot.git
$ cd notice
$ python manage.py makemigrations
$ python manage.py migrate
$ python manage.py inituser --username=admin --password=1234 --action=create_admin
$ python manage.py runserver
```

**<main.py사용법>**

1단계 

(chromedriver.exe가 설치 되어있는 경우는 2단계로 넘어가세요.)

본인 컴퓨터의 크롬 버전을 확인한 후 chromedriver.exe를 다운받습니다. 

크롬 버전 확인 방법: https://blog.naver.com/kiddwannabe/221539689821

chromedriver.exe 다운: https://chromedriver.chromium.org/downloads

2단계 

mian.py와 chromedriver.exe를 같은 위치에 둡니다.

PyCharm, Jupyter Notebook 등을 사용해 프로그램을 실행 합니다.

원하는 날짜(월과 일)을 입력합니다.

해당 날짜 공지글의 제목과 내용이 변수에 담겨 출력됩니다.

<br>

## :loudspeaker: Contributing 하는 방법 
Contribution과 issue는 언제든 환영합니다. 이 repository에 대해 PR을 제출할 수 있으며, 승인이 된다면 적용되어 배포될 것입니다.<br>
PR 제목을 포함한 Commit message는 아래와 같이 작성하면 됩니다.<br>
```
$ git commit -m "PR Title"
```


<br>

## :page_with_curl: 라이센스
APACHE LICENSE, VERSION 2.0



