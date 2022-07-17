# 무신사 과제

## 요구 사항

1. 상위 카테고리를 이용해, 해당 카테고리의 하위의 모든 카테고리를 조회가 가능해야한다.
2. 상위 카테고리를 지정하지 않을 시, 전체 카테고리를 반환해야한다.

두 조건 모두 충족하여 구현하였습니다.   
추가적으로 프론트 페이지 개발과 전체 카테고리 조회시 [캐시처리](https://github.com/kyupid/musinsa/pull/1)를 하였습니다.

프론트엔드: http://localhost:8080   
백엔드: http://localhost:800/api/categories

![2022-07-17_22-07-27](https://user-images.githubusercontent.com/59721293/179399830-fa2fdc47-5f34-4a0f-bca3-c17f4031eb08.jpg)

## 기술 스택

- Java
- Spring
- MyBatis
- H2
- Redis

## 실행 방법
리눅스 기준

먼저 아래 명령어를 통해 JDK와 Redis를 설치합니다.
```bash
sudo apt update
sudo apt upgrade
sudo apt install openjdk-11-jdk
sudo apt install redis

sudo apt install git
git clone git@github.com:kyupid/musinsa.git
cd musinsa
./gradlew build jar
java -jar build/libs/musinsa-0.0.1-SNAPSHOT.jar
```
로컬에서 프로젝트를 빌드하여 도커나 클라우드 서버로 전송하여 실행하는 경우,   
각 플랫폼에 맞는 방법(e.g. docker cp, scp 등)을 이용하여 파일을 복사한 후에.  
jar파일만 아래 명령어를 통해 배포파일을 실행합니다.
```bash
java -jar 지정경로/musinsa-0.0.1-SNAPSHOT.jar
```


## 데이터 구조

flat 구조
```
{
    "categories": [
        {
            "id": 1,
            "branch": "A",
            "name": "카테고리_1",
            "code": "A",
            "level": 0
        },
        {
            "id": 6,
            "branch": "A",
            "name": "카테고리_1_1",
            "code": "A|A",
            "level": 1
        },
        {
            "id": 8,
            "branch": "A",
            "name": "카테고리_1_1_1",
            "code": "A|A|A",
            "level": 2
        },
        {
            "id": 9,
            "branch": "A",
            "name": "카테고리_1_1_1_1",
            "code": "A|A|A|A",
            "level": 3
        },
        {
            "id": 10,
            "branch": "A",
            "name": "카테고리_1_1_1_2",
            "code": "A|A|A|B",
            "level": 3
        },
        {
            "id": 7,
            "branch": "A",
            "name": "카테고리_1_2",
            "code": "A|B",
            "level": 1
        },
        {
            "id": 2,
            "branch": "B",
            "name": "카테고리_2",
            "code": "B",
            "level": 0
        },
        {
            "id": 3,
            "branch": "C",
            "name": "카테고리_3",
            "code": "C",
            "level": 0
        },
        {
            "id": 4,
            "branch": "D",
            "name": "카테고리_4",
            "code": "D",
            "level": 0
        },
        {
            "id": 5,
            "branch": "E",
            "name": "카테고리_5",
            "code": "E",
            "level": 0
        }
    ]
}
```
### 프로퍼티 설명
- id: 유니크 id값
- name: 카테고리 이름
- branch: 최상위 카테고리부터 해당 카테고리의 모든 자식을 묶음
- level: 카테고리의 계층을 의미
- **code**: 해당 code를 기준으로 자식 카테고리를 구분하고 생성/삭제시에 이용됨
    - 무한 카테고리 생성 가능



## API 명세
Base URL: http://localhost:8080/api/categories

URL: GET `/`   
설명: 모든 카테고리 조회   
Response   
```json
{
    "categories": [
        {
            "id": 1,
            "branch": "A",
            "name": "카테고리_1",
            "code": "A",
            "level": 0
        },
        {
            "id": 6,
            "branch": "A",
            "name": "카테고리_1_1",
            "code": "A|A",
            "level": 1
        }
    ]
}
```

URL: GET `/{selectedId}`   
설명: 선택 카테고리 포함 하위 카테고리 조회    
Response
```json
{
    "categories": [
        {
            "id": 1,
            "branch": "A",
            "name": "카테고리_1",
            "code": "A",
            "level": 0
        },
        {
            "id": 6,
            "branch": "A",
            "name": "카테고리_1_1",
            "code": "A|A",
            "level": 1
        }
    ]
}
```

URL: POST `/`   
설명: 루트 카테고리 생성   
Request
```json
{
	"name": "카테고리_1"
}
```
Response
```
201 Created
```

URL: POST `/`   
설명: 하위 카테고리 생성   
Request
```json
{
	"parentId": 1,
	"name": "카테고리_1_1"
}
```
Response
```
201 Created
```

URL: DELETE `/{selectedId}`   
설명: 해당 카테고리포함 하위카테고리 모두 삭제   
Response
```
204 No content
```

URL: PATCH `/`   
설명: 카테고리 이름 수정   
Response
```
204 No content
```

