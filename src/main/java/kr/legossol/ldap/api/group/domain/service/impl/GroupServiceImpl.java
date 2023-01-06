package kr.legossol.ldap.api.group.domain.service.impl;

import com.google.gson.Gson;
import kr.legossol.ldap.api.group.domain.dto.GroupDepartmentDto;
import kr.legossol.ldap.api.group.domain.dto.response.GroupResponseDto;
import kr.legossol.ldap.api.group.domain.entity.GroupBasicInfo;
import kr.legossol.ldap.api.group.domain.service.GroupService;
import kr.legossol.ldap.api.group.exception.AlreadyExistException;
import kr.legossol.ldap.code.ObjectClassCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.AbstractContextMapper;
import org.springframework.ldap.query.LdapQuery;
import org.springframework.ldap.query.LdapQueryBuilder;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.naming.Name;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class GroupServiceImpl implements GroupService {
    private final LdapTemplate ldapTemplate;


    @Override
    public GroupResponseDto createGroup(GroupDepartmentDto groupDepartmentDto) {
        if (isExistDepartment(toGroupEntry(groupDepartmentDto.getGroupName(), groupDepartmentDto.getGroupBase()))) {
            throw new AlreadyExistException();
        }
        Name ldapName = LdapNameBuilder.newInstance().add(
                toGroupEntry(groupDepartmentDto.getGroupName(),groupDepartmentDto.getGroupBase()))
                .build();
        DirContextAdapter dir = new DirContextAdapter();
        dir.setAttributeValue(ObjectClassCode.OBJECT_CLASS.getName(), ObjectClassCode.POSIX_GROUP.getName());
        ldapTemplate.bind(dir);

        return null;
    }
    private void modify(String fullEntryName) {
        ModificationItem[] toBeAttribute = new ModificationItem[]{
                new ModificationItem(DirContextAdapter.REMOVE_ATTRIBUTE,
                        new BasicAttribute("departmentCode",7585))};
        ldapTemplate.modifyAttributes(fullEntryName, toBeAttribute);
    }

    private boolean isExistDepartment(String groupName) {
        Gson gson = new Gson();
        LdapQuery query = LdapQueryBuilder.query()
                .where(ObjectClassCode.OBJECT_CLASS.getName()).is(ObjectClassCode.POSIX_GROUP.getName())
                .and(ObjectClassCode.ORGANIZAIONAL_UNIT.getName()).like("*"+groupName+"*");
        List<GroupBasicInfo> groupList = ldapTemplate.search(query, new AbstractContextMapper<GroupBasicInfo>() {
            @Override
            protected GroupBasicInfo doMapFromContext(DirContextOperations ctx) {
                return GroupBasicInfo.builder()
                        .groupBase(ctx.getDn().toString())
                        .groupBaseId(ctx.getStringAttribute("gid"))
                        .build();
            }
        });
        return groupList.size() > 1 ? Boolean.TRUE : Boolean.FALSE;
    }

    private String toGroupEntry(String groupName, String groupBase) {
        StringBuilder sb = new StringBuilder();
        if (groupBase.isEmpty()) {
            return sb.append("cn=").append(groupName).toString();
        }
        return sb.append("cn=").append(groupName).append(",").append(groupBase).toString();
    }
}
