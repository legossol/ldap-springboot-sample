package kr.legossol.ldap.api.organization.domain.dto;

import kr.legossol.ldap.api.organization.domain.entity.DepartmentBasicInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupDepartmentDto {

    private String groupName;
    private String groupBase;
    private String groupBaseId;
    private String recogKey;

    public DepartmentBasicInfo of() {
        return DepartmentBasicInfo.bu
    }

}
