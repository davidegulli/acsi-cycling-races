package it.acsi.cycling.races.service.dto;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Objects;

import it.acsi.cycling.races.domain.AcsiTeam;
import it.acsi.cycling.races.domain.enumeration.GenderType;
import it.acsi.cycling.races.domain.enumeration.PaymentType;

/**
 * A DTO for the {@link it.acsi.cycling.races.domain.RaceSubscription} entity.
 */
public class RaceSubscriptionDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String surname;

    @NotNull
    private String birthDate;

    @NotNull
    private String birthPlace;

    @NotNull
    private GenderType gender;

    @NotNull
    private String taxCode;

    @NotNull
    private String email;

    @NotNull
    private String phone;

    private String category;

    @NotNull
    private Long subcriptionTypeId;

    private Long teamId;

    @NotNull
    private String teamCode;

    @NotNull
    private String teamName;

    private Instant date;

    @NotNull
    private String athleteId;

    private String attribute;

    @NotNull
    private PaymentType paymentType;

    private String paymentReceivedCode;

    private Boolean payed;

    private Double payedPrice;

    private Long raceId;

    private byte[] binaryPersonalIdDocFile;

    private String binaryPersonalIdDocContentType;

    private String binaryPersonalIdDocFileName;

    private byte[] binaryMedicalCertificationDocFile;

    private String binaryMedicalCertificationDocContentType;

    private String binaryMedicalCertificationDocFileName;

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

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getBirthPlace() {
        return birthPlace;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }

    public GenderType getGender() {
        return gender;
    }

    public void setGender(GenderType gender) {
        this.gender = gender;
    }

    public String getTaxCode() {
        return taxCode;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Long getSubcriptionTypeId() {
        return subcriptionTypeId;
    }

    public void setSubcriptionTypeId(Long subcriptionTypeId) {
        this.subcriptionTypeId = subcriptionTypeId;
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public Boolean getPayed() {
        return payed;
    }

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    public String getAthleteId() {
        return athleteId;
    }

    public void setAthleteId(String athleteId) {
        this.athleteId = athleteId;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public String getPaymentReceivedCode() {
        return paymentReceivedCode;
    }

    public void setPaymentReceivedCode(String paymentReceivedCode) {
        this.paymentReceivedCode = paymentReceivedCode;
    }

    public Boolean isPayed() {
        return payed;
    }

    public void setPayed(Boolean payed) {
        this.payed = payed;
    }

    public Double getPayedPrice() {
        return payedPrice;
    }

    public void setPayedPrice(Double payedPrice) {
        this.payedPrice = payedPrice;
    }

    public Long getRaceId() {
        return raceId;
    }

    public void setRaceId(Long raceId) {
        this.raceId = raceId;
    }

    public String getTeamCode() {
        return teamCode;
    }

    public void setTeamCode(String teamCode) {
        this.teamCode = teamCode;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public byte[] getBinaryPersonalIdDocFile() {
        return binaryPersonalIdDocFile;
    }

    public void setBinaryPersonalIdDocFile(byte[] binaryPersonalIdDocFile) {
        this.binaryPersonalIdDocFile = binaryPersonalIdDocFile;
    }

    public String getBinaryPersonalIdDocContentType() {
        return binaryPersonalIdDocContentType;
    }

    public void setBinaryPersonalIdDocContentType(String binaryPersonalIdDocContentType) {
        this.binaryPersonalIdDocContentType = binaryPersonalIdDocContentType;
    }

    public byte[] getBinaryMedicalCertificationDocFile() {
        return binaryMedicalCertificationDocFile;
    }

    public void setBinaryMedicalCertificationDocFile(byte[] binaryMedicalCertificationDocFile) {
        this.binaryMedicalCertificationDocFile = binaryMedicalCertificationDocFile;
    }

    public String getBinaryMedicalCertificationDocContentType() {
        return binaryMedicalCertificationDocContentType;
    }

    public void setBinaryMedicalCertificationDocContentType(String binaryMedicalCertificationDocContentType) {
        this.binaryMedicalCertificationDocContentType = binaryMedicalCertificationDocContentType;
    }

    public String getBinaryPersonalIdDocFileName() {
        return binaryPersonalIdDocFileName;
    }

    public void setBinaryPersonalIdDocFileName(String binaryPersonalIdDocFileName) {
        this.binaryPersonalIdDocFileName = binaryPersonalIdDocFileName;
    }

    public String getBinaryMedicalCertificationDocFileName() {
        return binaryMedicalCertificationDocFileName;
    }

    public void setBinaryMedicalCertificationDocFileName(String binaryMedicalCertificationDocFileName) {
        this.binaryMedicalCertificationDocFileName = binaryMedicalCertificationDocFileName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        RaceSubscriptionDTO raceSubscriptionDTO = (RaceSubscriptionDTO) o;
        if (raceSubscriptionDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), raceSubscriptionDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "RaceSubscriptionDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", surname='" + getSurname() + "'" +
            ", birthDate='" + getBirthDate() + "'" +
            ", birthPlace='" + getBirthPlace() + "'" +
            ", gender='" + getGender() + "'" +
            ", taxCode='" + getTaxCode() + "'" +
            ", email='" + getEmail() + "'" +
            ", phone='" + getPhone() + "'" +
            ", category='" + getCategory() + "'" +
            ", subcriptionTypeId=" + getSubcriptionTypeId() +
            ", teamId=" + getTeamId() +
            ", athleteId='" + getAthleteId() + "'" +
            ", date='" + getDate() + "'" +
            ", attribute='" + getAttribute() + "'" +
            ", paymentType='" + getPaymentType() + "'" +
            ", paymentReceivedCode='" + getPaymentReceivedCode() + "'" +
            ", payed='" + isPayed() + "'" +
            ", payedPrice=" + getPayedPrice() +
            ", race=" + getRaceId() +
            "}";
    }
}
