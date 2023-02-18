package kr.legossol.ldap.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ObjectClassCode {
    OBJECT_CLASS("objectClass")
    ,TOP("top")
    ,POSIX_GROUP("posixGroup")
    ,POSIX_ACCOUNT("posixAccount")
    ,ORGANIZAIONAL_UNIT("organizaionalUnit")
    ,ORGANIZAIONAL_PERSON("organizaionalPerson")
    ,INET_ORG_PERSON("inetOrgPerson")
    ,EXTENSIBLE_OBJECT("extensibleObject")
    ,PERSON("person")
    ,
    ;
    private final String name;
    public static String[] getDefaultPosixGroupObjectClass() {
        return new String[]{
                TOP.getName()
                , POSIX_GROUP.getName()
                , EXTENSIBLE_OBJECT.getName()
        };
    }

    public static String[] getDefaultPosixAccountObjectClass() {
        return new String[]{
                TOP.getName(),
                PERSON.getName(),
                POSIX_ACCOUNT.getName(),
                INET_ORG_PERSON.getName(),
                ORGANIZAIONAL_PERSON.getName()
        };
    }
}
