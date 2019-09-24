package it.acsi.cycling.races.service.dto;
import java.time.Instant;
import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import it.acsi.cycling.races.domain.enumeration.RaceStatus;

/**
 * A DTO for the {@link it.acsi.cycling.races.domain.Race} entity.
 */
public class RaceDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private LocalDate date;

    @NotNull
    private String location;

    private String description;

    private String info;

    @NotNull
    private String address;

    private Double latitude;

    private Double longitude;

    private String rules;

    private LocalDate subscriptionExpirationDate;

    private String attributes;

    private RaceStatus status;

    private Long typeId;
    private String typeName;

    private Long acsiTeamId;

    private String contactName;

    private String contactEmail;

    private String contactPhone;

    private byte[] binaryLogoImage;
    private String binaryLogoContentType;
    private String binaryLogoFileName;
    private String binaryLogoUrl;

    private byte[] binaryCoverImage;
    private String binaryCoverContentType;
    private String binaryCoverFileName;
    private String binaryCoverUrl;

    private byte[] binaryPathMapImage;
    private String binaryPathMapContentType;
    private String binaryPathMapFileName;
    private String binaryPathMapUrl;

    private List<SubscriptionTypeDTO> subscriptionTypes;
    private List<PathTypeDTO> pathTypes;

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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
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

    public LocalDate getSubscriptionExpirationDate() {
        return subscriptionExpirationDate;
    }

    public void setSubscriptionExpirationDate(LocalDate subscriptionExpirationDate) {
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

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long raceTypeId) {
        this.typeId = raceTypeId;
    }

    public Long getAcsiTeamId() {
        return acsiTeamId;
    }

    public void setAcsiTeamId(Long acsiTeamId) {
        this.acsiTeamId = acsiTeamId;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public byte[] getBinaryLogoImage() {
        return binaryLogoImage;
    }

    public void setBinaryLogoImage(byte[] binaryLogoImage) {
        this.binaryLogoImage = binaryLogoImage;
    }

    public byte[] getBinaryCoverImage() {
        return binaryCoverImage;
    }

    public void setBinaryCoverImage(byte[] binaryCoverImage) {
        this.binaryCoverImage = binaryCoverImage;
    }

    public byte[] getBinaryPathMapImage() {
        return binaryPathMapImage;
    }

    public void setBinaryPathMapImage(byte[] binaryPathMapImage) {
        this.binaryPathMapImage = binaryPathMapImage;
    }

    public String getBinaryLogoContentType() {
        return binaryLogoContentType;
    }

    public void setBinaryLogoContentType(String binaryLogoContentType) {
        this.binaryLogoContentType = binaryLogoContentType;
    }

    public String getBinaryLogoFileName() {
        return binaryLogoFileName;
    }

    public void setBinaryLogoFileName(String binaryLogoFileName) {
        this.binaryLogoFileName = binaryLogoFileName;
    }

    public String getBinaryCoverContentType() {
        return binaryCoverContentType;
    }

    public void setBinaryCoverContentType(String binaryCoverContentType) {
        this.binaryCoverContentType = binaryCoverContentType;
    }

    public String getBinaryCoverFileName() {
        return binaryCoverFileName;
    }

    public void setBinaryCoverFileName(String binaryCoverFileName) {
        this.binaryCoverFileName = binaryCoverFileName;
    }

    public String getBinaryPathMapContentType() {
        return binaryPathMapContentType;
    }

    public void setBinaryPathMapContentType(String binaryPathMapContentType) {
        this.binaryPathMapContentType = binaryPathMapContentType;
    }

    public String getBinaryPathMapFileName() {
        return binaryPathMapFileName;
    }

    public void setBinaryPathMapFileName(String binaryPathMapFileName) {
        this.binaryPathMapFileName = binaryPathMapFileName;
    }

    public String getBinaryLogoUrl() {
        return binaryLogoUrl;
    }

    public void setBinaryLogoUrl(String binaryLogoUrl) {
        this.binaryLogoUrl = binaryLogoUrl;
    }

    public String getBinaryCoverUrl() {
        return binaryCoverUrl;
    }

    public void setBinaryCoverUrl(String binaryCoverUrl) {
        this.binaryCoverUrl = binaryCoverUrl;
    }

    public String getBinaryPathMapUrl() {
        return binaryPathMapUrl;
    }

    public void setBinaryPathMapUrl(String binaryPathMapUrl) {
        this.binaryPathMapUrl = binaryPathMapUrl;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public List<SubscriptionTypeDTO> getSubscriptionTypes() {
        return subscriptionTypes;
    }

    public void setSubscriptionTypes(List<SubscriptionTypeDTO> subscriptionTypes) {
        this.subscriptionTypes = subscriptionTypes;
    }

    public List<PathTypeDTO> getPathTypes() {
        return pathTypes;
    }

    public void setPathTypes(List<PathTypeDTO> pathTypes) {
        this.pathTypes = pathTypes;
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
            ", type=" + getTypeId() +
            ", acsiTeam=" + getAcsiTeamId() +
            "}";
    }
}
