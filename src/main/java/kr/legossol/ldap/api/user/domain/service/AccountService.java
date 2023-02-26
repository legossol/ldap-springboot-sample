package kr.legossol.ldap.api.user.domain.service;

import kr.legossol.ldap.api.user.domain.entity.Account;
import kr.legossol.ldap.api.user.dto.AccountInfoDto;

import java.util.List;

public interface AccountService {

    void createAccount(Account account);
    List<AccountInfoDto> findAccount(List<String> userList);
}
