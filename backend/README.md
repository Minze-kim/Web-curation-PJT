# 0720 Nuga 코딩 컨벤션

## 💡 Commit message


- 영어만을 이용해 작성, 총 70자를 넘기지 않는다
- 첫 글자는 반드시 대문자로 작성할 것,(고유명사를 제외한 나머지는 다 소문자로!!)
- 명령문 형식으로
- JIRA코드 번호는 `커밋 메세지` + `,(띄어쓰기)` + `코드번호`
- 띄어쓰기 잘하기!!!!!

``` bash
// example
$ Edit login function, SP0S03P12A304-12
$ Modify backend server code, S03P12A304-13
$ Create user profile page, S03P12A304-20

// 고유명사
$ Modify SNS login function, S03P12A304-23
```



## 🖥️ Front

- VS code 사용시

  - Indenting space 👉 2

  

- 축약형 사용

  - `v-on` 👉 `@`
  - `v-bind` 👉 `:`

  

- 함수

  - 화살표함수

```javascript
login(){
    axios.post(BACKEND_URL)
    	.then(res => {
        console.log(res)
    	})
    	.catch(err => {
        console.log(err.response.data)
    })

}
```



- 뷰 컴포넌트명

  - 합성어로!! 각 단어의 첫 글자는 반드시 대문자

    ```
    // 좋음
    TodoItem
    MovieList
    MovieListItem
    
    // 나쁨
    todo
    Todo
    movielist
    Movielist
    ```



- `route name` == `component name`

  - 경로 관련해서 쓸때는 route name으로만 사용하기!!(경로 하드코딩❌)

    ```html
    <!-- 좋음 -->
    <router-link v-if="!isLoggedIn" :to="{ name: 'Login' }">Login</router-link>
    <!-- script 에서도! -->
    router.push({name:'Home'})
    
    
    <!-- 나쁨 -->
    <router-link v-if="!isLoggedIn" to="/account/user/login">Login</router-link>
    <!-- script 에서도! -->
    router.push('/account/user/login')
    
    ```



- 컴포넌트에서 사용할 함수나 변수명

  - **`lowerCamelCase`**

  - 맨 앞글자를 소문자로 나머지 뒤에 따라붙는 단어들의 앞글자는 모두 대문자로 표기

    ```
    // 예시
    fetchArticles
    createArticle
    postAuthData
    ```



- ⚠️ **단,**  `Mutations.js` 에서는 주의!!! ⚠️

  - **`ALL_CAPITAL_SNAKE_CASE`**

  - 모든 글자는 대문자로, 각 단어의 사이를 언더바`_`로 구분해주는 표기

    ```
    SET_TOKEN
    LOGIN
    SIGN_UP
    FIND_PW
    ```



- 추가적으로 축약이 필요한 단어의 경우에는, 네이밍하기전에 상의하기!!!

  ```
  password 👉 pwd, pw
  button 👉 btn, button
  ```



- `template`에서 id, class, for 등....정의할 때

  - 일반적으로 소문자

  - 합성어의 경우, 소문자-소문자

    ```html
    <div id="find-pw">
    	<form class="find-pw-form">
            <label for="email"></label>
            <input id="email" />
    	</form>
    </div>
    ```
    
## BackEnd
    
 - JAVA 프로그래밍

```
 - 변수는 CamelCase를 기본으로 한다.

예) userEmail, userCellPhone ...

 - 패키지명은 소문자를 사용한다.

에) frontend, useremail 

 - ENUM이나 상수는 대문자로 네이밍하고 ( - )을 사용하지 않는다

예) NORMAL_STATUS ...

 - 함수명은 소문자로 시작하고 동사로 네이밍한다.

예) getUserId(), isNormar() ...

 - 클래스명은 명사로 작성하고 UpperCamelCase를 사용한다.

예) UserEmail, Address ...

 - 컬렉션은 복수형을 사용하거나 컬렉션을 명시해준다.

예) List ids, Map<User, Int> userToIdMap ...

 - JPA Repository에서는 findXXX형식의 네이밍 쿼리메소드를 사용하므로 개발자 정의 메소드는 getXXX를 정의해서 사용한다
```

 - Structure
 
```
 - MVC 패턴을 기본으로 한다. 
 
 - Controller는 로직을 처리하지 않고 어떤 Service의 어떤 함수를 호출할지 결정과 Exception처리만을 담당하도록한다.
 
 - 하나의 메소드와 클래스는 하나의 목적을 두게 만든다.
 
 예) 예) 로그인 기능을 위해 사용자 검증 목적인 userValid 함수를 따로 만든다.
 
 - RequestMapping보다는 GetMapping, PutMapping 등 명시적으로 알아보기 쉽게 만든다.
```

 - 그외
```
- 조건문에 부정 조건을 넣는 것을 피한다.

예) if(!status.isOK)  (X) ,   if(status.isOK) (O)

- entity 모델 그대로 request 파라미터, response 파라미터를 사용하지 않는다.

예) 예) 대신, SignUpReqeust를 만들어 사용한다.

- AutoWired 대신에 롬복의 RequiredArgsConstructor과 private final을 이용한다.
```
