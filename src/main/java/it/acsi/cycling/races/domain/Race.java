package it.acsi.cycling.races.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import it.acsi.cycling.races.domain.enumeration.RaceStatus;

/**
 * A Race.
 */
@Entity
@Table(name = "race")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "race")
public class Race implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @NotNull
    @Column(name = "location", nullable = false)
    private String location;

    @Column(name = "description")
    private String description;

    @Column(name = "info")
    private String info;

    @NotNull
    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "rules")
    private String rules;

    @Column(name = "subscription_expiration_date")
    private Instant subscriptionExpirationDate;

    @Column(name = "attributes")
    private String attributes;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private RaceStatus status;

    @OneToMany(mappedBy = "race")
    private Set<Contact> contacts = new HashSet<>();

    @OneToMany(mappedBy = "race")
    private Set<File> attachments = new HashSet<>();

    @OneToMany(mappedBy = "race")
    private Set<PathType> pathTypes = new HashSet<>();

    @OneToMany(mappedBy = "race")
    private Set<SubscriptionType> subscriptionTypes = new HashSet<>();

    @OneToMany(mappedBy = "race")
    private Set<RaceSubscription> subscriptions = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("races")
    private RaceType type;

    @ManyToOne
    @JsonIgnoreProperties("races")
    private AcsiTeam acsiTeam;

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

    public Race name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDate() {
        return date;
    }

    public Race date(LocalDate date) {
        this.date = date;
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public Race location(String location) {
        this.location = location;
        return this;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public Race description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInfo() {
        return info;
    }

    public Race info(String info) {
        this.info = info;
        return this;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getAddress() {
        return address;
    }

    public Race address(String address) {
        this.address = address;
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Race latitude(Double latitude) {
        this.latitude = latitude;
        return this;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Race longitude(Double longitude) {
        this.longitude = longitude;
        return this;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getRules() {
        return rules;
    }

    public Race rules(String rules) {
        this.rules = rules;
        return this;
    }

    public void setRules(String rules) {
        this.rules = rules;
    }

    public Instant getSubscriptionExpirationDate() {
        return subscriptionExpirationDate;
    }

    public Race subscriptionExpirationDate(Instant subscriptionExpirationDate) {
        this.subscriptionExpirationDate = subscriptionExpirationDate;
        return this;
    }

    public void setSubscriptionExpirationDate(Instant subscriptionExpirationDate) {
        this.subscriptionExpirationDate = subscriptionExpirationDate;
    }

    public String getAttributes() {
        return attributes;
    }

    public Race attributes(String attributes) {
        this.attributes = attributes;
        return this;
    }

    public void setAttributes(String attributes) {
        this.attributes = attributes;
    }

    public RaceStatus getStatus() {
        return status;
    }

    public Race status(RaceStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(RaceStatus status) {
        this.status = status;
    }

    public Set<Contact> getContacts() {
        return contacts;
    }

    public Race contacts(Set<Contact> contacts) {
        this.contacts = contacts;
        return this;
    }

    public Race addContact(Contact contact) {
        this.contacts.add(contact);
        contact.setRace(this);
        return this;
    }

    public Race removeContact(Contact contact) {
        this.contacts.remove(contact);
        contact.setRace(null);
        return this;
    }

    public void setContacts(Set<Contact> contacts) {
        this.contacts = contacts;
    }

    public Set<File> getAttachments() {
        return attachments;
    }

    public Race attachments(Set<File> files) {
        this.attachments = files;
        return this;
    }

    public Race addAttachment(File file) {
        this.attachments.add(file);
        file.setRace(this);
        return this;
    }

    public Race removeAttachment(File file) {
        this.attachments.remove(file);
        file.setRace(null);
        return this;
    }

    public void setAttachments(Set<File> files) {
        this.attachments = files;
    }

    public Set<PathType> getPathTypes() {
        return pathTypes;
    }

    public Race pathTypes(Set<PathType> pathTypes) {
        this.pathTypes = pathTypes;
        return this;
    }

    public Race addPathType(PathType pathType) {
        this.pathTypes.add(pathType);
        pathType.setRace(this);
        return this;
    }

    public Race removePathType(PathType pathType) {
        this.pathTypes.remove(pathType);
        pathType.setRace(null);
        return this;
    }

    public void setPathTypes(Set<PathType> pathTypes) {
        this.pathTypes = pathTypes;
    }

    public Set<SubscriptionType> getSubscriptionTypes() {
        return subscriptionTypes;
    }

    public Race subscriptionTypes(Set<SubscriptionType> subscriptionTypes) {
        this.subscriptionTypes = subscriptionTypes;
        return this;
    }

    public Race addSubscriptionType(SubscriptionType subscriptionType) {
        this.subscriptionTypes.add(subscriptionType);
        subscriptionType.setRace(this);
        return this;
    }

    public Race removeSubscriptionType(SubscriptionType subscriptionType) {
        this.subscriptionTypes.remove(subscriptionType);
        subscriptionType.setRace(null);
        return this;
    }

    public void setSubscriptionTypes(Set<SubscriptionType> subscriptionTypes) {
        this.subscriptionTypes = subscriptionTypes;
    }

    public Set<RaceSubscription> getSubscriptions() {
        return subscriptions;
    }

    public Race subscriptions(Set<RaceSubscription> raceSubscriptions) {
        this.subscriptions = raceSubscriptions;
        return this;
    }

    public Race addSubscription(RaceSubscription raceSubscription) {
        this.subscriptions.add(raceSubscription);
        raceSubscription.setRace(this);
        return this;
    }

    public Race removeSubscription(RaceSubscription raceSubscription) {
        this.subscriptions.remove(raceSubscription);
        raceSubscription.setRace(null);
        return this;
    }

    public void setSubscriptions(Set<RaceSubscription> raceSubscriptions) {
        this.subscriptions = raceSubscriptions;
    }

    public RaceType getType() {
        return type;
    }

    public Race type(RaceType raceType) {
        this.type = raceType;
        return this;
    }

    public void setType(RaceType raceType) {
        this.type = raceType;
    }

    public AcsiTeam getAcsiTeam() {
        return acsiTeam;
    }

    public Race acsiTeam(AcsiTeam acsiTeam) {
        this.acsiTeam = acsiTeam;
        return this;
    }

    public void setAcsiTeam(AcsiTeam acsiTeam) {
        this.acsiTeam = acsiTeam;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Race)) {
            return false;
        }
        return id != null && id.equals(((Race) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Race{" +
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
            "}";
    }
}
