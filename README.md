# ldap-springboot-sample
LDAP에 대한 모든 테스트 내용 정린는 아래 노션 링크에 있습니다.
https://malachite-wall-179.notion.site/LDAP-e09bbbfdf00142049def7e431636c4be


## 시작해보자

brew install openldap

- 테스트로 하는거니까 일단 샘플로 만들어서 하는거

sudo cp /etc/openldap/slapd.conf.default /etc/openldap/slapd.conf

```
root 비밀번호를 수정해야 하며 string 으로 사용해도 되나 암호화된 
값을 이용하는것이 더 안전하기에 slappasswd 명령어로 원하는 비밀번호를
입력하고 단방향 알고리즘 sha로 암호화한 패스워드를 가져온다.
```

$ slappasswd

![스크린샷 2022-12-07 오후 3.44.15.png](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/e083875a-ffe0-4f16-b9be-21bdadfe4fbf/%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%85%B5%E1%86%AB%E1%84%89%E1%85%A3%E1%86%BA_2022-12-07_%E1%84%8B%E1%85%A9%E1%84%92%E1%85%AE_3.44.15.png)

sudo vi /etc/openldap/slapd.conf

dc와 rootpw 에 위의 {SSHA}로 시작하는 암호로 설정 (수정한 String 입력해도 무관 -보안때문에 하는것)

추가 디렉토리 위치도 바꿔주자 

![스크린샷 2022-12-07 오후 3.58.42.png](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/94677749-8c28-4802-b93a-a646631354ee/%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%85%B5%E1%86%AB%E1%84%89%E1%85%A3%E1%86%BA_2022-12-07_%E1%84%8B%E1%85%A9%E1%84%92%E1%85%AE_3.58.42.png)

sudo vi ldap.conf

Base, Binddn, Uri 수정

![스크린샷 2022-12-07 오후 3.49.39.png](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/69f724b1-3d7c-40c7-b439-a74d514bd029/%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%85%B5%E1%86%AB%E1%84%89%E1%85%A3%E1%86%BA_2022-12-07_%E1%84%8B%E1%85%A9%E1%84%92%E1%85%AE_3.49.39.png)

![스크린샷 2022-12-07 오후 3.51.21.png](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/245bbe6d-54ba-42c0-8d1e-f5b277483990/%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%85%B5%E1%86%AB%E1%84%89%E1%85%A3%E1%86%BA_2022-12-07_%E1%84%8B%E1%85%A9%E1%84%92%E1%85%AE_3.51.21.png)

설정 끝

시작

$sudo mkdir /private/var/db/openldap/ssolldap.com

$sudo /usr/libexec/slapd -h ldap://localhost -d 50

 확인 : 디렉토리에 DB관련 파일이 생성된다.
위치 : cd /private/var/db/openldap/ssolldap.com

<img width="336" alt="Screenshot 2023-01-08 at 8 36 24 PM" src="https://user-images.githubusercontent.com/75191069/211194014-cf600f33-12f3-4057-baf0-2ffceaf683c3.png">


ldap은 정상적으로 띄어졌지만 최상위 DN이 생성되야 한다 - 이게 무슨말일까

ldif 파일에 끝 부분에 공백, 탭이 있으면 syntex 오류 발생하니 주의한다.

[1단계: LDIF 파일 생성](https://docs.aws.amazon.com/ko_kr/directoryservice/latest/admin-guide/create.html)

$vi create-suffix-dn.ldif

아무 장소 에서 vi xxx.ldif(나의 경우 create-suffix-dn.ldif)를 생성

```
dn: dc=ssolldap,dc=com
ObjectClass: dcObject
ObjectClass: organization
dc: ssolldap
o: Example
```

![스크린샷 2022-12-08 오전 11.28.52.png](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/b2aa03f1-5a7f-4fb8-bbf0-2b6907acb1e7/%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%85%B5%E1%86%AB%E1%84%89%E1%85%A3%E1%86%BA_2022-12-08_%E1%84%8B%E1%85%A9%E1%84%8C%E1%85%A5%E1%86%AB_11.28.52.png)

$vi cn_apply.sh

```
$ vi cn_apply.sh
sudo ldapadd -x -D "cn=Manager,dc=ssolldap,dc=com" -W -f create-suffix-dn.ldif
ldapadd -x -D "cn=admin,dc=mycompany,dc=com" -w admin -H ldap://$LDAP_HOST -f ldap/ldap-mycompany-com.ldif
```


$ sh cn_apply.sh

상위에서 설정한 ldap 비밀번호(String)을 입력하면 아래와같이 뭐가 입력되며


아까 구동시킨 ldap서버 로그에 아래와같이 뜬다.

확인 : $ ldapsearch -x -b dc=ssolldap,dc=com


FROM NOW ON, 부서 생성요청 or 계정 생성 요청시 어떻게 처리하는지 확인(UI 사용 - apache-directory-studio) : 그냥 콘솔로도 가능(힘듦)

$ brew install apache-directory-studio
