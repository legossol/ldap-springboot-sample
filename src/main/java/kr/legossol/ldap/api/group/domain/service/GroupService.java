package kr.legossol.ldap.api.group.domain.service;

import kr.legossol.ldap.api.group.domain.dto.GroupDepartmentDto;
import kr.legossol.ldap.api.group.domain.dto.response.GroupResponseDto;
import kr.legossol.ldap.api.group.domain.entity.GroupBasicInfo;

public interface GroupService {
    GroupResponseDto createGroup(GroupDepartmentDto groupDepartmentDto);
}
