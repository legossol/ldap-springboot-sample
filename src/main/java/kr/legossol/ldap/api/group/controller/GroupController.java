package kr.legossol.ldap.api.group.controller;

import kr.legossol.ldap.api.group.domain.dto.GroupDepartmentDto;
import kr.legossol.ldap.api.group.domain.dto.response.GroupResponseDto;
import kr.legossol.ldap.api.group.domain.service.GroupService;
import kr.legossol.ldap.api.group.facade.GroupFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequestMapping
@RestController
@RequiredArgsConstructor
public class GroupController {

    private final GroupFacade groupFacade;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "")
    public ResponseEntity<GroupResponseDto> createGroup(@RequestBody GroupDepartmentDto groupDepartmentDto) {
        GroupResponseDto groupFacade = this.groupFacade.createGroupFacade(groupDepartmentDto);
        return new ResponseEntity<>(groupFacade,HttpStatus.CREATED);
    }

}
