package kr.legossol.ldap.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AttributeClassCode {
    DOCUMENT_LOCATION("documentLocaion")
    ,PASSWORD("userPassword")
    ,CN("cn")
    ,UID("uid")
    ,GRADE("personalTitle")//직급(선임, 매니저, 사원)
    ,TITLE("title")//직위(팀장, 사원)
    ,STATUS("ST")
    ,TELEPHONE("modile")
    ,EMAIL("email")
    ,UID_NUMBER("uidNumber")
    ,GID("gid")
    ,MEMBER("member")
    ,DEPTH_CODE("documentLocation")
    ,DEL_NY("shadowFlag")
    ,
    ;

    private final String name;


}
