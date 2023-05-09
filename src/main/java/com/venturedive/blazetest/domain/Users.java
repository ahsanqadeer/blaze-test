package com.venturedive.blazetest.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Users.
 */
@Entity
@Table(name = "users")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Users implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Size(max = 255)
    @Column(name = "first_name", length = 255)
    private String firstName;

    @Size(max = 255)
    @Column(name = "last_name", length = 255)
    private String lastName;

    @Size(max = 255)
    @Column(name = "password", length = 255)
    private String password;

    @Column(name = "last_active")
    private Instant lastActive;

    @Size(max = 255)
    @Column(name = "status", length = 255)
    private String status;

    @Column(name = "created_by")
    private Integer createdBy;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_by")
    private Integer updatedBy;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @Size(max = 255)
    @Column(name = "email", length = 255, unique = true)
    private String email;

    @NotNull
    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;

    @NotNull
    @Column(name = "email_verified", nullable = false)
    private Boolean emailVerified;

    @NotNull
    @Size(max = 100)
    @Column(name = "provider", length = 100, nullable = false)
    private String provider;

    @Lob
    @Column(name = "email_verification_token")
    private String emailVerificationToken;

    @Lob
    @Column(name = "forgot_password_token")
    private String forgotPasswordToken;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "projectsCompanies", "templatefieldsCompanies", "templatesCompanies", "usersCompanies" },
        allowSetters = true
    )
    private Companies company;

    @ManyToMany
    @JoinTable(
        name = "rel_users__project",
        joinColumns = @JoinColumn(name = "users_id"),
        inverseJoinColumns = @JoinColumn(name = "project_id")
    )
    @JsonIgnoreProperties(
        value = { "defaultTemplate", "company", "milestonesProjects", "testplansProjects", "testsuitesProjects", "users" },
        allowSetters = true
    )
    private Set<Projects> projects = new HashSet<>();

    @ManyToMany(mappedBy = "users")
    @JsonIgnoreProperties(value = { "permissions", "users" }, allowSetters = true)
    private Set<Roles> roles = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Users id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public Users firstName(String firstName) {
        this.setFirstName(firstName);
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public Users lastName(String lastName) {
        this.setLastName(lastName);
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return this.password;
    }

    public Users password(String password) {
        this.setPassword(password);
        return this;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Instant getLastActive() {
        return this.lastActive;
    }

    public Users lastActive(Instant lastActive) {
        this.setLastActive(lastActive);
        return this;
    }

    public void setLastActive(Instant lastActive) {
        this.lastActive = lastActive;
    }

    public String getStatus() {
        return this.status;
    }

    public Users status(String status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getCreatedBy() {
        return this.createdBy;
    }

    public Users createdBy(Integer createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public Users createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getUpdatedBy() {
        return this.updatedBy;
    }

    public Users updatedBy(Integer updatedBy) {
        this.setUpdatedBy(updatedBy);
        return this;
    }

    public void setUpdatedBy(Integer updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public Users updatedAt(Instant updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getEmail() {
        return this.email;
    }

    public Users email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getIsDeleted() {
        return this.isDeleted;
    }

    public Users isDeleted(Boolean isDeleted) {
        this.setIsDeleted(isDeleted);
        return this;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Boolean getEmailVerified() {
        return this.emailVerified;
    }

    public Users emailVerified(Boolean emailVerified) {
        this.setEmailVerified(emailVerified);
        return this;
    }

    public void setEmailVerified(Boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public String getProvider() {
        return this.provider;
    }

    public Users provider(String provider) {
        this.setProvider(provider);
        return this;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getEmailVerificationToken() {
        return this.emailVerificationToken;
    }

    public Users emailVerificationToken(String emailVerificationToken) {
        this.setEmailVerificationToken(emailVerificationToken);
        return this;
    }

    public void setEmailVerificationToken(String emailVerificationToken) {
        this.emailVerificationToken = emailVerificationToken;
    }

    public String getForgotPasswordToken() {
        return this.forgotPasswordToken;
    }

    public Users forgotPasswordToken(String forgotPasswordToken) {
        this.setForgotPasswordToken(forgotPasswordToken);
        return this;
    }

    public void setForgotPasswordToken(String forgotPasswordToken) {
        this.forgotPasswordToken = forgotPasswordToken;
    }

    public Companies getCompany() {
        return this.company;
    }

    public void setCompany(Companies companies) {
        this.company = companies;
    }

    public Users company(Companies companies) {
        this.setCompany(companies);
        return this;
    }

    public Set<Projects> getProjects() {
        return this.projects;
    }

    public void setProjects(Set<Projects> projects) {
        this.projects = projects;
    }

    public Users projects(Set<Projects> projects) {
        this.setProjects(projects);
        return this;
    }

    public Users addProject(Projects projects) {
        this.projects.add(projects);
        projects.getUsers().add(this);
        return this;
    }

    public Users removeProject(Projects projects) {
        this.projects.remove(projects);
        projects.getUsers().remove(this);
        return this;
    }

    public Set<Roles> getRoles() {
        return this.roles;
    }

    public void setRoles(Set<Roles> roles) {
        if (this.roles != null) {
            this.roles.forEach(i -> i.removeUser(this));
        }
        if (roles != null) {
            roles.forEach(i -> i.addUser(this));
        }
        this.roles = roles;
    }

    public Users roles(Set<Roles> roles) {
        this.setRoles(roles);
        return this;
    }

    public Users addRole(Roles roles) {
        this.roles.add(roles);
        roles.getUsers().add(this);
        return this;
    }

    public Users removeRole(Roles roles) {
        this.roles.remove(roles);
        roles.getUsers().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Users)) {
            return false;
        }
        return id != null && id.equals(((Users) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Users{" +
            "id=" + getId() +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", password='" + getPassword() + "'" +
            ", lastActive='" + getLastActive() + "'" +
            ", status='" + getStatus() + "'" +
            ", createdBy=" + getCreatedBy() +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedBy=" + getUpdatedBy() +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", email='" + getEmail() + "'" +
            ", isDeleted='" + getIsDeleted() + "'" +
            ", emailVerified='" + getEmailVerified() + "'" +
            ", provider='" + getProvider() + "'" +
            ", emailVerificationToken='" + getEmailVerificationToken() + "'" +
            ", forgotPasswordToken='" + getForgotPasswordToken() + "'" +
            "}";
    }
}
