package kr.legossol.ldap.api.user.dto;

import kr.legossol.ldap.api.user.domain.entity.Account;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountInfoDto {
    private String name;
    private String grade;
    private String title;
    private String uid;
    private String status;
    private String cellphone;
    private String email;
    private String delNy;

    public static AccountInfoDto of(Account account) {
        return AccountInfoDto.builder()
                .name(ObjectUtils.defaultIfNull(account.getName(),""))
                .grade(ObjectUtils.defaultIfNull(account.getGrade(),""))
                .title(ObjectUtils.defaultIfNull(account.getTitle(),""))
                .uid(ObjectUtils.defaultIfNull(account.getUid(),""))
                .cellphone(ObjectUtils.defaultIfNull(account.getCellphone(),""))
                .status(ObjectUtils.defaultIfNull(account.getCellphone(),""))
                .email(ObjectUtils.defaultIfNull(account.getEmail(),""))
                .delNy(ObjectUtils.defaultIfNull(account.getDelNy(),""))
                .build();
    }
}
