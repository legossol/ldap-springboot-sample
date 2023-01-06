package kr.legossol.ldap.api.group.domain.dto;

import kr.legossol.ldap.api.group.domain.entity.GroupBasicInfo;
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

    public GroupBasicInfo of() {
        return GroupBasicInfo.bu
    }

}
