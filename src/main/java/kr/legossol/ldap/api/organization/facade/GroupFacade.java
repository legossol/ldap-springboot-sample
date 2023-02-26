package kr.legossol.ldap.api.organization.facade;

import kr.legossol.ldap.api.organization.domain.dto.DepartmentRequestV1;
import kr.legossol.ldap.api.organization.domain.dto.GroupDepartmentDto;
import kr.legossol.ldap.api.organization.domain.dto.MoveDepartmentDto;
import kr.legossol.ldap.api.organization.domain.dto.response.DepartmentResponseV1;
import kr.legossol.ldap.api.organization.domain.dto.response.GroupResponseDto;
import kr.legossol.ldap.api.organization.domain.service.GroupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class GroupFacade {
    private final GroupService groupService;

    public GroupResponseDto createGroupFacade(GroupDepartmentDto groupDepartmentDto) {
        return groupService.createGroup(groupDepartmentDto);
    }

    public DepartmentResponseV1 createDepartment(DepartmentRequestV1 departmentRequestV1) {
         groupService.create(departmentRequestV1);
    }

    public void moveDepartment(MoveDepartmentDto moveDepartmentDto) {
        groupService.moveDepartment(moveDepartmentDto);
    }
}
