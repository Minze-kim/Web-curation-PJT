## Nuga 서버배포자료

### 푸티젠으로 ppk 생성 

 1. 푸티젠 실행
 2. 메뉴바에 conversions 클릭 => import key 클릭
 3. ppk로 바꿀 pem 파일 선택(zip파일 말고 pem이어야함 당연)
 4. save public key 클릭



### 푸티옵션 설정

1. putty 실행
2. Connection -> SSH -> Auth 클릭
3. Auth에서 private key file for authentication에 ppk파일(위에서만든) 선택
4. Connection -> Data 클릭
5. Data에서 Auto-login username에 ubuntu(다른거 입력해도 되지만 일단통일) 입력
6. Session와서 saved sessions에 방금 설정한거 저장하기 위해서 이름 입력 아무거나
7. Save클릭 
8. 저장한 것 더블클릭



### winSCP 실행

1. 호스트이름에 i3a304.p.ssafy.io 입력
2. 사용자이름에 아까 저장한 ubuntu 입력
3. 로그인시도 해봄 -> 되면끝 안되면 4번으로
4. 1,2번 과정을 다시 하고 고급버튼 클릭 
5. 연결 -> 터널링에서 SSH 터널링을 통한 접속 체크
6. 이전과 같이 호스트이름,사용자이름 넣고 개인키파일(ppk)도 넣는다
7. SSH -> 인증을 가서도 ppk를 넣는다.
8. 확인 -> 로그인한다. 여기서 안되면 모른다



### 배포하기

1. vsCode가서 npm build하여 dist파일을 생성한다.
2. intellij 혹은 이클립스 가서 jar 파일을 생성한다.
 2-1 이클립스에 대해서는 모르니 검색하고 인텔리제이라면 오른쪽이나 아래에 maven 탭을
클릭하고 Lifecycle -> package 오른쪽마우스클릭 -> run maven build 클릭
3. 그러면 jar파일이 프로젝트 루트 밑에 target폴더 밑에 jar파일이 만들어짐
4. dist 파일과 jar파일을 서버에 올려야한다. 이제 이것을 위에서 연결한 WinSCP로 쉽게 해보자
5. jar파일과 dist폴더를 /home/ubuntu에 드래그앤 드롭으로 옮긴다(로컬인것 처럼 보이지만 winscp는 서버의 폴더를 보여준다)
6. 이제 putty로 간다. 처음 들어온 위치가 /home/ubuntu일 것이다. 그 밑에 jar파일을 실행해보자
7. sudo java -jar 파일이름을 실행한다.



### 백그라운드 실행

1. 현재 sudo java -jar 파일이름 명령어를 통해서 프로세스가 실행중인 상태다
2. ctrl + z를 입력한다.
3. jobs 명령어를 통해 실행중인 프로세스 번호를 확인한다. 1번일 것이다
4. bg %1을 입력하면 백그라운드 실행
5. 이걸 종료하고싶으면 그냥 하면 안된다. fg %1을 입력하고 돌아온다.
6. fg로 돌아왔으면 평소처럼 종료해도 된다. fg %1을 입력안하고
프로세스를 종료해도 되긴 하다 (kill %1)