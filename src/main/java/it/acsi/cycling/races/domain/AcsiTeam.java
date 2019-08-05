package it.acsi.cycling.races.domain;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A AcsiTeam.
 */
@Entity
@Table(name = "acsi_team")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "acsiteam")
public class AcsiTeam implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @NotNull
    @Column(name = "code", nullable = false)
    private String code;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "user_id", nullable = false)
    private String userId;

    @OneToMany(mappedBy = "acsiTeam")
    private Set<Race> races = new HashSet<>();

    @OneToMany(mappedBy = "team")
    private Set<Contact> contacts = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public AcsiTeam code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public AcsiTeam name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public AcsiTeam userId(String userId) {
        this.userId = userId;
        return this;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Set<Race> getRaces() {
        return races;
    }

    public AcsiTeam races(Set<Race> races) {
        this.races = races;
        return this;
    }

    public AcsiTeam addRace(Race race) {
        this.races.add(race);
        race.setAcsiTeam(this);
        return this;
    }

    public AcsiTeam removeRace(Race race) {
        this.races.remove(race);
        race.setAcsiTeam(null);
        return this;
    }

    public void setRaces(Set<Race> races) {
        this.races = races;
    }

    public Set<Contact> getContacts() {
        return contacts;
    }

    public AcsiTeam contacts(Set<Contact> contacts) {
        this.contacts = contacts;
        return this;
    }

    public AcsiTeam addContact(Contact contact) {
        this.contacts.add(contact);
        contact.setTeam(this);
        return this;
    }

    public AcsiTeam removeContact(Contact contact) {
        this.contacts.remove(contact);
        contact.setTeam(null);
        return this;
    }

    public void setContacts(Set<Contact> contacts) {
        this.contacts = contacts;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AcsiTeam)) {
            return false;
        }
        return id != null && id.equals(((AcsiTeam) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "AcsiTeam{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", name='" + getName() + "'" +
            ", userId='" + getUserId() + "'" +
            "}";
    }
}
