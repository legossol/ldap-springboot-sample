package kr.legossol.ldap.api.organization.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentBasicInfo {
    private String Name;
    private String depth;
    private String level;
    private String gidNumber;
}
