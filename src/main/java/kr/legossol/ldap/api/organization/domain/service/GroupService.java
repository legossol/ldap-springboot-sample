package kr.legossol.ldap.api.organization.domain.service;

import kr.legossol.ldap.api.organization.domain.dto.DepartmentRequestV1;
import kr.legossol.ldap.api.organization.domain.dto.GroupDepartmentDto;
import kr.legossol.ldap.api.organization.domain.dto.response.DepartmentResponseV1;
import kr.legossol.ldap.api.organization.domain.dto.response.GroupResponseDto;

public interface GroupService {
    GroupResponseDto createGroup(GroupDepartmentDto groupDepartmentDto);

    void create(DepartmentRequestV1 departmentRequestV1);
}
