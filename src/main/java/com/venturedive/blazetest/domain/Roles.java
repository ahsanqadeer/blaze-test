package com.venturedive.blazetest.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Roles.
 */
@Entity
@Table(name = "roles")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Roles implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 255)
    @Column(name = "role_name", length = 255, nullable = false, unique = true)
    private String roleName;

    @Column(name = "isdefault")
    private Boolean isdefault;

    @ManyToMany
    @JoinTable(
        name = "rel_roles__permission",
        joinColumns = @JoinColumn(name = "roles_id"),
        inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    @JsonIgnoreProperties(value = { "roles" }, allowSetters = true)
    private Set<Permissions> permissions = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "rel_roles__user", joinColumns = @JoinColumn(name = "roles_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    @JsonIgnoreProperties(value = { "company", "projects", "roles" }, allowSetters = true)
    private Set<Users> users = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Roles id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoleName() {
        return this.roleName;
    }

    public Roles roleName(String roleName) {
        this.setRoleName(roleName);
        return this;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Boolean getIsdefault() {
        return this.isdefault;
    }

    public Roles isdefault(Boolean isdefault) {
        this.setIsdefault(isdefault);
        return this;
    }

    public void setIsdefault(Boolean isdefault) {
        this.isdefault = isdefault;
    }

    public Set<Permissions> getPermissions() {
        return this.permissions;
    }

    public void setPermissions(Set<Permissions> permissions) {
        this.permissions = permissions;
    }

    public Roles permissions(Set<Permissions> permissions) {
        this.setPermissions(permissions);
        return this;
    }

    public Roles addPermission(Permissions permissions) {
        this.permissions.add(permissions);
        permissions.getRoles().add(this);
        return this;
    }

    public Roles removePermission(Permissions permissions) {
        this.permissions.remove(permissions);
        permissions.getRoles().remove(this);
        return this;
    }

    public Set<Users> getUsers() {
        return this.users;
    }

    public void setUsers(Set<Users> users) {
        this.users = users;
    }

    public Roles users(Set<Users> users) {
        this.setUsers(users);
        return this;
    }

    public Roles addUser(Users users) {
        this.users.add(users);
        users.getRoles().add(this);
        return this;
    }

    public Roles removeUser(Users users) {
        this.users.remove(users);
        users.getRoles().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Roles)) {
            return false;
        }
        return id != null && id.equals(((Roles) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Roles{" +
            "id=" + getId() +
            ", roleName='" + getRoleName() + "'" +
            ", isdefault='" + getIsdefault() + "'" +
            "}";
    }
}
