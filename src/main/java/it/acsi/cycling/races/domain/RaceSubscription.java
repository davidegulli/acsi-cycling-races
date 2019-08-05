package it.acsi.cycling.races.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.time.Instant;

import it.acsi.cycling.races.domain.enumeration.GenderType;

import it.acsi.cycling.races.domain.enumeration.PaymentType;

/**
 * A RaceSubscription.
 */
@Entity
@Table(name = "race_subscription")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "racesubscription")
public class RaceSubscription implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "surname", nullable = false)
    private String surname;

    @NotNull
    @Column(name = "birth_date", nullable = false)
    private String birthDate;

    @NotNull
    @Column(name = "birth_place", nullable = false)
    private String birthPlace;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false)
    private GenderType gender;

    @NotNull
    @Column(name = "tax_code", nullable = false)
    private String taxCode;

    @NotNull
    @Column(name = "email", nullable = false)
    private String email;

    @NotNull
    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "category")
    private String category;

    @NotNull
    @Column(name = "subcription_type_id", nullable = false)
    private Long subcriptionTypeId;

    @NotNull
    @Column(name = "path_type", nullable = false)
    private Long pathType;

    @NotNull
    @Column(name = "team_id", nullable = false)
    private Long teamId;

    @NotNull
    @Column(name = "athlete_id", nullable = false)
    private String athleteId;

    @NotNull
    @Column(name = "date", nullable = false)
    private Instant date;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_type", nullable = false)
    private PaymentType paymentType;

    @Column(name = "payed")
    private Boolean payed;

    @Column(name = "payed_price")
    private Double payedPrice;

    @ManyToOne
    @JsonIgnoreProperties("subscriptionTypes")
    private Race race;

    @ManyToOne
    @JsonIgnoreProperties("subscriptions")
    private Race race;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public RaceSubscription name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public RaceSubscription surname(String surname) {
        this.surname = surname;
        return this;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public RaceSubscription birthDate(String birthDate) {
        this.birthDate = birthDate;
        return this;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getBirthPlace() {
        return birthPlace;
    }

    public RaceSubscription birthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
        return this;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }

    public GenderType getGender() {
        return gender;
    }

    public RaceSubscription gender(GenderType gender) {
        this.gender = gender;
        return this;
    }

    public void setGender(GenderType gender) {
        this.gender = gender;
    }

    public String getTaxCode() {
        return taxCode;
    }

    public RaceSubscription taxCode(String taxCode) {
        this.taxCode = taxCode;
        return this;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }

    public String getEmail() {
        return email;
    }

    public RaceSubscription email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public RaceSubscription phone(String phone) {
        this.phone = phone;
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCategory() {
        return category;
    }

    public RaceSubscription category(String category) {
        this.category = category;
        return this;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Long getSubcriptionTypeId() {
        return subcriptionTypeId;
    }

    public RaceSubscription subcriptionTypeId(Long subcriptionTypeId) {
        this.subcriptionTypeId = subcriptionTypeId;
        return this;
    }

    public void setSubcriptionTypeId(Long subcriptionTypeId) {
        this.subcriptionTypeId = subcriptionTypeId;
    }

    public Long getPathType() {
        return pathType;
    }

    public RaceSubscription pathType(Long pathType) {
        this.pathType = pathType;
        return this;
    }

    public void setPathType(Long pathType) {
        this.pathType = pathType;
    }

    public Long getTeamId() {
        return teamId;
    }

    public RaceSubscription teamId(Long teamId) {
        this.teamId = teamId;
        return this;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    public String getAthleteId() {
        return athleteId;
    }

    public RaceSubscription athleteId(String athleteId) {
        this.athleteId = athleteId;
        return this;
    }

    public void setAthleteId(String athleteId) {
        this.athleteId = athleteId;
    }

    public Instant getDate() {
        return date;
    }

    public RaceSubscription date(Instant date) {
        this.date = date;
        return this;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public RaceSubscription paymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
        return this;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public Boolean isPayed() {
        return payed;
    }

    public RaceSubscription payed(Boolean payed) {
        this.payed = payed;
        return this;
    }

    public void setPayed(Boolean payed) {
        this.payed = payed;
    }

    public Double getPayedPrice() {
        return payedPrice;
    }

    public RaceSubscription payedPrice(Double payedPrice) {
        this.payedPrice = payedPrice;
        return this;
    }

    public void setPayedPrice(Double payedPrice) {
        this.payedPrice = payedPrice;
    }

    public Race getRace() {
        return race;
    }

    public RaceSubscription race(Race race) {
        this.race = race;
        return this;
    }

    public void setRace(Race race) {
        this.race = race;
    }

    public Race getRace() {
        return race;
    }

    public RaceSubscription race(Race race) {
        this.race = race;
        return this;
    }

    public void setRace(Race race) {
        this.race = race;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RaceSubscription)) {
            return false;
        }
        return id != null && id.equals(((RaceSubscription) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "RaceSubscription{" +
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
            ", pathType=" + getPathType() +
            ", teamId=" + getTeamId() +
            ", athleteId='" + getAthleteId() + "'" +
            ", date='" + getDate() + "'" +
            ", paymentType='" + getPaymentType() + "'" +
            ", payed='" + isPayed() + "'" +
            ", payedPrice=" + getPayedPrice() +
            "}";
    }
}
