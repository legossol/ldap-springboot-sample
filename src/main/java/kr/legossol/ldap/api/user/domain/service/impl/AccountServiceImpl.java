package kr.legossol.ldap.api.user.domain.service.impl;

import kr.legossol.ldap.api.user.domain.entity.Account;
import kr.legossol.ldap.api.user.domain.service.AccountService;
import kr.legossol.ldap.api.user.dto.AccountInfoDto;
import kr.legossol.ldap.code.AttributeClassCode;
import kr.legossol.ldap.code.ObjectClassCode;
import lombok.RequiredArgsConstructor;
import org.springframework.ldap.core.ContextMapper;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.AbstractContextMapper;
import org.springframework.ldap.query.LdapQuery;
import org.springframework.ldap.query.LdapQueryBuilder;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.stereotype.Service;

import javax.naming.Name;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final LdapTemplate ldapTemplate;

    private final ContextMapper<Account> accountContextMapper = new AbstractContextMapper<Account>() {
        @Override
        protected Account doMapFromContext(DirContextOperations ctx) {
            return Account.builder()
                    .name(ctx.getStringAttribute(AttributeClassCode.CN.getName()))
                    .uid(ctx.getStringAttribute(AttributeClassCode.UID.getName()))
                    .grade(ctx.getStringAttribute(AttributeClassCode.GRADE.getName()))
                    .title(ctx.getStringAttribute(AttributeClassCode.TITLE.getName()))
                    .status(ctx.getStringAttribute(AttributeClassCode.STATUS.getName()))
                    .cellphone(ctx.getStringAttribute(AttributeClassCode.TELEPHONE.getName()))
                    .email(ctx.getStringAttribute(AttributeClassCode.EMAIL.getName()))
                    .id(ctx.getStringAttribute(AttributeClassCode.UID_NUMBER.getName()))
                    .build();
        }
    };

    @Override
    public void createAccount(Account account) {
        String userEntryDn = toEntry(account.getUid());
        Name accountName = LdapNameBuilder.newInstance()
                .add(userEntryDn)
                .build();

        DirContextAdapter dirContextAdapter = new DirContextAdapter(accountName);
        dirContextAdapter.setAttributeValues(ObjectClassCode.OBJECT_CLASS.getName(),
                ObjectClassCode.getDefaultPosixAccountObjectClass());
        dirContextAdapter.setAttributeValue(AttributeClassCode.CN.getName(),account.getName());
        dirContextAdapter.setAttributeValue(AttributeClassCode.UID.getName(),account.getUid());
        dirContextAdapter.setAttributeValue(AttributeClassCode.GRADE.getName(),account.getGrade());
        dirContextAdapter.setAttributeValue(AttributeClassCode.STATUS.getName(),account.getStatus());
        dirContextAdapter.setAttributeValue(AttributeClassCode.TITLE.getName(),account.getTitle());
        dirContextAdapter.setAttributeValue(AttributeClassCode.PASSWORD.getName(),account.getPassword());
        dirContextAdapter.setAttributeValue(AttributeClassCode.EMAIL.getName(),account.getEmail());
        dirContextAdapter.setAttributeValue(AttributeClassCode.UID_NUMBER.getName(),account.getId());
    }
    private String toEntry(String uid) {
        StringBuilder sb = new StringBuilder();
        return sb.append("uid=").append(uid).append(",ou=users").toString();
    }

    @Override
    public List<AccountInfoDto> findAccount(List<String> userList) {
        List<Account> accountList = new ArrayList<>();
        userList.forEach(name ->{
            LdapQuery query = LdapQueryBuilder.query()
                    .where(ObjectClassCode.OBJECT_CLASS.getName()).is(ObjectClassCode.POSIX_ACCOUNT.getName())
                    .and(AttributeClassCode.UID.getName()).is(name)
                    .and(AttributeClassCode.DEL_NY.getName()).is("0");
            accountList.addAll(ldapTemplate.search(query, accountContextMapper));
        });
        return accountList.stream().map(AccountInfoDto::of).collect(Collectors.toList());
    }
}
