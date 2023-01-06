package kr.legossol.ldap.api.group.facade;

import kr.legossol.ldap.api.group.domain.dto.GroupDepartmentDto;
import kr.legossol.ldap.api.group.domain.dto.response.GroupResponseDto;
import kr.legossol.ldap.api.group.domain.entity.GroupBasicInfo;
import kr.legossol.ldap.api.group.domain.service.GroupService;
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
}
