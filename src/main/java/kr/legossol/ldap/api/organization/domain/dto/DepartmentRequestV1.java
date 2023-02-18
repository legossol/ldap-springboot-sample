package kr.legossol.ldap.api.organization.domain.dto;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class    DepartmentRequestV1 {
    @NotNull
    private String name; // 생성될 부서 이름
    @NotNull
    private String gidNumber;
    @NotNull
    private String level; // 생성될 부서 위치

    //하기 부서 이름 and gidnumber는 최상위일 경우 존재하지 않는다는 가정
    private String parentName; //부모의 이름
    private String parentGidnumber;
}
