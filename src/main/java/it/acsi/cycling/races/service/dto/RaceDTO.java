package it.acsi.cycling.races.service.dto;
import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import it.acsi.cycling.races.domain.enumeration.RaceStatus;

/**
 * A DTO for the {@link it.acsi.cycling.races.domain.Race} entity.
 */
public class RaceDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private Instant date;

    @NotNull
    private String location;

    private String description;

    private String info;

    @NotNull
    private String address;

    private Double latitude;

    private Double longitude;

    private String rules;

    private Instant subscriptionExpirationDate;

    private String attributes;

    private RaceStatus status;


    private Long acsiTeamId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getRules() {
        return rules;
    }

    public void setRules(String rules) {
        this.rules = rules;
    }

    public Instant getSubscriptionExpirationDate() {
        return subscriptionExpirationDate;
    }

    public void setSubscriptionExpirationDate(Instant subscriptionExpirationDate) {
        this.subscriptionExpirationDate = subscriptionExpirationDate;
    }

    public String getAttributes() {
        return attributes;
    }

    public void setAttributes(String attributes) {
        this.attributes = attributes;
    }

    public RaceStatus getStatus() {
        return status;
    }

    public void setStatus(RaceStatus status) {
        this.status = status;
    }

    public Long getAcsiTeamId() {
        return acsiTeamId;
    }

    public void setAcsiTeamId(Long acsiTeamId) {
        this.acsiTeamId = acsiTeamId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        RaceDTO raceDTO = (RaceDTO) o;
        if (raceDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), raceDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "RaceDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", date='" + getDate() + "'" +
            ", location='" + getLocation() + "'" +
            ", description='" + getDescription() + "'" +
            ", info='" + getInfo() + "'" +
            ", address='" + getAddress() + "'" +
            ", latitude=" + getLatitude() +
            ", longitude=" + getLongitude() +
            ", rules='" + getRules() + "'" +
            ", subscriptionExpirationDate='" + getSubscriptionExpirationDate() + "'" +
            ", attributes='" + getAttributes() + "'" +
            ", status='" + getStatus() + "'" +
            ", acsiTeam=" + getAcsiTeamId() +
            "}";
    }
}
