package kr.legossol.ldap.api.organization.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MoveDepartmentDto {
    private String name;
    private String parentName;
}
