package com.ibizabroker.lms.entity;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(
    name = "role",
    uniqueConstraints = @UniqueConstraint(name = "uk_role_name", columnNames = "role_name")
)
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Integer roleId;

    @Column(name = "role_name", nullable = false, length = 50)
    private String roleName;

    public Integer getRoleId() { return roleId; }
    public void setRoleId(Integer roleId) { this.roleId = roleId; }

    public String getRoleName() { return roleName; }
    public void setRoleName(String roleName) { this.roleName = roleName; }

    // alias nếu nơi khác dùng getName/setName
    public String getName() { return roleName; }
    public void setName(String name) { this.roleName = name; }

    // để Set<Role> hoạt động ổn định
    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Role)) return false;
        Role role = (Role) o;
        return Objects.equals(roleName, role.roleName);
    }
    @Override public int hashCode() { return Objects.hash(roleName); }
}
