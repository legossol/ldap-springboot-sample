package kr.legossol.ldap.api.organization.controller;

import kr.legossol.ldap.api.organization.domain.dto.DepartmentRequestV1;
import kr.legossol.ldap.api.organization.domain.dto.GroupDepartmentDto;
import kr.legossol.ldap.api.organization.domain.dto.response.DepartmentResponseV1;
import kr.legossol.ldap.api.organization.domain.dto.response.GroupResponseDto;
import kr.legossol.ldap.api.organization.facade.GroupFacade;
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
    public ResponseEntity<DepartmentResponseV1> createGroup(@RequestBody DepartmentRequestV1 departmentRequestV1) {
        DepartmentResponseV1 groupFacade = this.groupFacade.createDepartment(departmentRequestV1);
        return new ResponseEntity<>(groupFacade,HttpStatus.CREATED);
    }

//    @ResponseStatus(HttpStatus.CREATED)
//    @PostMapping(value = "")
//    public ResponseEntity<GroupResponseDto> createGroup(@RequestBody GroupDepartmentDto groupDepartmentDto) {
//        GroupResponseDto groupFacade = this.groupFacade.createGroupFacade(groupDepartmentDto);
//        return new ResponseEntity<>(groupFacade,HttpStatus.CREATED);
//    }

}
