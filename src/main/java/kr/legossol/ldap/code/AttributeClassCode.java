package kr.legossol.ldap.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AttributeClassCode {
    DOCUMENT_LOCATION("documentLocaion")
    ,CN("cn")
    ,GID("gid")
    ,MEMBER("member")
    ,
    ;

    private final String name;


}
