package kr.legossol.ldap.api.group.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupBasicInfo {
    private String groupName;
    private String groupBase;
    private String groupBaseId;
    private String recogKey;
}
