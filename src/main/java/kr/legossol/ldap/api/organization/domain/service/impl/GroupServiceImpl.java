package kr.legossol.ldap.api.organization.domain.service.impl;

import com.google.gson.Gson;
import kr.legossol.ldap.api.organization.domain.dto.DepartmentRequestV1;
import kr.legossol.ldap.api.organization.domain.dto.GroupDepartmentDto;
import kr.legossol.ldap.api.organization.domain.dto.MoveDepartmentDto;
import kr.legossol.ldap.api.organization.domain.dto.response.DepartmentResponseV1;
import kr.legossol.ldap.api.organization.domain.dto.response.GroupResponseDto;
import kr.legossol.ldap.api.organization.domain.entity.DepartmentBasicInfo;
import kr.legossol.ldap.api.organization.domain.service.GroupService;
import kr.legossol.ldap.api.organization.exception.MultiDepartmentExistException;
import kr.legossol.ldap.api.organization.properties.LdapProperties;
import kr.legossol.ldap.api.user.domain.entity.Account;
import kr.legossol.ldap.code.AttributeClassCode;
import kr.legossol.ldap.code.ObjectClassCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.ldap.core.ContextMapper;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.AbstractContextMapper;
import org.springframework.ldap.query.LdapQuery;
import org.springframework.ldap.query.LdapQueryBuilder;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.naming.Name;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.ModificationItem;
import javax.naming.directory.SearchControls;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
@EnableConfigurationProperties(LdapProperties.class)
public class GroupServiceImpl implements GroupService {

    private final LdapTemplate ldapTemplate;
    private final LdapProperties ldapProperties;

    private final ContextMapper<Name> groupAttrModifyMapper = new AbstractContextMapper<Name>() {
        @Override
        protected Name doMapFromContext(DirContextOperations ctx) {
            List<String> memberAttrList = Arrays.stream(
                    ctx.getStringAttributes(AttributeClassCode.MEMBER.getName())).collect(Collectors.toList());
            List<ModificationItem> toBeAttributes = new LinkedList<>();

            ldapTemplate.modifyAttributes(ctx.getDn(), new ModificationItem[]{new ModificationItem(
                    DirContextAdapter.REMOVE_ATTRIBUTE, ctx.getAttributes().get(AttributeClassCode.MEMBER.getName())
            )});
            memberAttrList.forEach(value ->{
                if(StringUtils.startsWithIgnoreCase(value,"uid")){
                    toBeAttributes.add(new ModificationItem(DirContextAdapter.ADD_ATTRIBUTE,
                            new BasicAttribute(AttributeClassCode.MEMBER.getName(), value)));
                    return;
                }
                toBeAttributes.add(new ModificationItem(DirContextAdapter.ADD_ATTRIBUTE,
                        new BasicAttribute(AttributeClassCode.MEMBER.getName(),
                                value.split(",")[0] + "," + ctx.getDn().toString() + "," + ldapProperties.getBase())));
            });
            toBeAttributes.add(new ModificationItem(DirContextAdapter.REPLACE_ATTRIBUTE,
                    new BasicAttribute(AttributeClassCode.DEPTH_CODE.getName(),
                            String.valueOf(Arrays.asList(ctx.getDn().toString().split(",")).size() - 1))));
            ldapTemplate.modifyAttributes(ctx.getDn(), toBeAttributes.toArray(toBeAttributes.toArray(new ModificationItem[toBeAttributes.size()])));
            return ctx.getDn();
        }
    };


    @Override
    public GroupResponseDto createGroup(GroupDepartmentDto groupDepartmentDto) {
//        if (isExistDepartment(toGroupEntry(groupDepartmentDto.getGroupName(), groupDepartmentDto.getGroupBase()))) {
//            throw new AlreadyExistException();
//        }
        Name ldapName = LdapNameBuilder.newInstance().add(
                toGroupEntry(groupDepartmentDto.getGroupName(),groupDepartmentDto.getGroupBase()))
                .build();
        DirContextAdapter dir = new DirContextAdapter();
        dir.setAttributeValue(ObjectClassCode.OBJECT_CLASS.getName(), ObjectClassCode.POSIX_GROUP.getName());
        ldapTemplate.bind(dir);

        return null;
    }

    @Override
    public void create(DepartmentRequestV1 departmentRequestV1) {
        if (ObjectUtils.isEmpty(departmentRequestV1.getParentName()) && departmentRequestV1.getLevel().equals("001")) {
            Name name = toName(departmentRequestV1.getName(), "");
            DirContextAdapter context = new DirContextAdapter(name);
            context.setAttributeValues(ObjectClassCode.OBJECT_CLASS.getName(), ObjectClassCode.getDefaultPosixGroupObjectClass());
            context.setAttributeValue(AttributeClassCode.CN.getName(),departmentRequestV1.getName());
            context.setAttributeValue(AttributeClassCode.GID.getName(),departmentRequestV1.getGidNumber());
            context.setAttributeValue(AttributeClassCode.DOCUMENT_LOCATION.getName(),departmentRequestV1.getLevel());
            ldapTemplate.bind(context);
            return ;
        }
        LdapQuery query = LdapQueryBuilder.query()
                .where(ObjectClassCode.OBJECT_CLASS.getName()).is(ObjectClassCode.POSIX_GROUP.getName())
                .and(AttributeClassCode.CN.getName()).is(departmentRequestV1.getName())
                .and(AttributeClassCode.DOCUMENT_LOCATION.getName()).is(departmentRequestV1.getLevel())
                .and(AttributeClassCode.GID.getName()).is(departmentRequestV1.getParentGidnumber());
        List<DepartmentBasicInfo> departmentBasicInfos = ldapTemplate.search(query, new AbstractContextMapper<DepartmentBasicInfo>() {
            @Override
            protected DepartmentBasicInfo doMapFromContext(DirContextOperations ctx) {
                return DepartmentBasicInfo.builder()
                        .depth(ctx.getDn().toString())
                        .gidNumber(ctx.getStringAttribute("gid"))
                        .level(ctx.getStringAttribute(AttributeClassCode.DOCUMENT_LOCATION.getName()))
                        .build();
            }
        });
        if (departmentBasicInfos.size() > 1) {
            throw new MultiDepartmentExistException();
        }
        departmentBasicInfos.stream().map(departmentBasicInfo -> {
            Name subName = toName(departmentRequestV1.getName(), departmentBasicInfo.getDepth());
            DirContextAdapter context = new DirContextAdapter(subName);
            context.setAttributeValues(ObjectClassCode.OBJECT_CLASS.getName(), ObjectClassCode.getDefaultPosixGroupObjectClass());
            context.setAttributeValue(AttributeClassCode.CN.getName(), departmentRequestV1.getName());
            context.setAttributeValue(AttributeClassCode.GID.getName(), departmentRequestV1.getGidNumber());
            context.setAttributeValue(AttributeClassCode.DOCUMENT_LOCATION.getName(), departmentRequestV1.getLevel());
            ldapTemplate.bind(context);
            return null;
        });
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
        List<DepartmentBasicInfo> groupList = ldapTemplate.search(query, new AbstractContextMapper<DepartmentBasicInfo>() {
            @Override
            protected DepartmentBasicInfo doMapFromContext(DirContextOperations ctx) {
                return DepartmentBasicInfo.builder()
                        .depth(ctx.getDn().toString())
                        .gidNumber(ctx.getStringAttribute("gid"))
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
    private Name toName(String departmentName, String groupBase) {
        StringBuilder sb = new StringBuilder();
        if (groupBase.isEmpty()) {
            return LdapNameBuilder
                    .newInstance()
                    .add(sb.append("cn=").append(departmentName).toString())
                    .build();
        }
        LdapQuery query = LdapQueryBuilder.query().where(ObjectClassCode.OBJECT_CLASS.getName()).is(ObjectClassCode.POSIX_GROUP.getName())
                .and(AttributeClassCode.CN.getName()).like("*"+departmentName+"*");
        List<DepartmentBasicInfo> departmentBasicInfos = ldapTemplate.search(query, new AbstractContextMapper<DepartmentBasicInfo>() {
            @Override
            protected DepartmentBasicInfo doMapFromContext(DirContextOperations ctx) {
                return DepartmentBasicInfo.builder()
                        .depth(ctx.getDn().toString())
                        .gidNumber(ctx.getStringAttribute("gid"))
                        .build();
            }
        });

        return LdapNameBuilder
                .newInstance()
                .add(sb.append("cn=").append(departmentName).append(groupBase).toString())
                .build();
    }
    @Override
    public void moveDepartment(MoveDepartmentDto moveDepartmentDto) {
        LdapQuery query;
        query = LdapQueryBuilder.query()
                .where(ObjectClassCode.OBJECT_CLASS.getName()).is(ObjectClassCode.POSIX_GROUP.getName())
                .and(AttributeClassCode.CN.getName()).is(moveDepartmentDto.getName());
        //대상 부서 DirContext
        Name targetDn = ldapTemplate.searchForContext(query).getDn();
        log.info("search ::::{}",targetDn);
        //옮겨질 부모 DirContext
        LdapQuery parentQuery;
        parentQuery = LdapQueryBuilder.query()
                .where(ObjectClassCode.OBJECT_CLASS.getName()).is(ObjectClassCode.POSIX_GROUP.getName())
                .and(AttributeClassCode.CN.getName()).is(moveDepartmentDto.getParentName());
        Name parentDn = ldapTemplate.searchForContext(parentQuery).getDn();
        Name toBeLodationDn = toName(moveDepartmentDto.getName(), parentDn.toString());

        log.info("toBeLodationDn::{}",toBeLodationDn);
        ldapTemplate.rename(targetDn, toBeLodationDn);

        //하나일경우 modify  여러개일경우 다 지우고 add
        List<Name> search = ldapTemplate.search(toBeLodationDn.toString(), "(&(objectclass=posixGroup))", SearchControls.SUBTREE_SCOPE, groupAttrModifyMapper);
        
        log.info("subtree search:::::{}",search);
    }

}
