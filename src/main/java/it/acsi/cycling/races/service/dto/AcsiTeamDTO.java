package it.acsi.cycling.races.service.dto;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link it.acsi.cycling.races.domain.AcsiTeam} entity.
 */
public class AcsiTeamDTO implements Serializable {

    private Long id;

    @NotNull
    private String code;

    @NotNull
    private String name;

    private String userId;

    @NotNull
    private String managerName;

    @NotNull
    private String managerSurname;

    @NotNull
    private String managerEmail;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public String getManagerSurname() {
        return managerSurname;
    }

    public void setManagerSurname(String managerSurname) {
        this.managerSurname = managerSurname;
    }

    public String getManagerEmail() {
        return managerEmail;
    }

    public void setManagerEmail(String managerEmail) {
        this.managerEmail = managerEmail;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AcsiTeamDTO acsiTeamDTO = (AcsiTeamDTO) o;
        if (acsiTeamDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), acsiTeamDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "AcsiTeamDTO{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", name='" + getName() + "'" +
            ", userId='" + getUserId() + "'" +
            ", managerName='" + getManagerName() + "'" +
            ", managersurname='" + getManagerSurname() + "'" +
            ", managerEmail='" + getManagerEmail() + "'" +
            "}";
    }
}
