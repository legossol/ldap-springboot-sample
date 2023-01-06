package kr.legossol.ldap.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ObjectClassCode {
    OBJECT_CLASS("objectClass")
    ,TOP("top")
    ,POSIX_GROUP("posixGroup")
    ,ORGANIZAIONAL_UNIT("organizaionalUnit")
    ;
    private final String name;
}
