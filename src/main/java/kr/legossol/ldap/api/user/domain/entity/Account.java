package kr.legossol.ldap.api.user.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    private String id;
    private String uid;
    private String password;
    private String status;
    private String title;
    private String grade;
    private String cellphone;
    private String email;
    private String name;
    private String delNy;


}
