package it.acsi.cycling.races.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A SubscriptionType.
 */
@Entity
@Table(name = "subscription_type")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "subscriptiontype")
public class SubscriptionType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "rules")
    private String rules;

    @Column(name = "distance")
    private Integer distance;

    @Column(name = "price")
    private Double price;

    @OneToMany(mappedBy = "subscriptionType")
    private Set<SubscriptionDiscount> discounts = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("subscriptionTypes")
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

    public SubscriptionType name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public SubscriptionType description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRules() {
        return rules;
    }

    public SubscriptionType rules(String rules) {
        this.rules = rules;
        return this;
    }

    public void setRules(String rules) {
        this.rules = rules;
    }

    public Integer getDistance() {
        return distance;
    }

    public SubscriptionType distance(Integer distance) {
        this.distance = distance;
        return this;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    public Double getPrice() {
        return price;
    }

    public SubscriptionType price(Double price) {
        this.price = price;
        return this;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Set<SubscriptionDiscount> getDiscounts() {
        return discounts;
    }

    public SubscriptionType discounts(Set<SubscriptionDiscount> subscriptionDiscounts) {
        this.discounts = subscriptionDiscounts;
        return this;
    }

    public SubscriptionType addDiscounts(SubscriptionDiscount subscriptionDiscount) {
        this.discounts.add(subscriptionDiscount);
        subscriptionDiscount.setSubscriptionType(this);
        return this;
    }

    public SubscriptionType removeDiscounts(SubscriptionDiscount subscriptionDiscount) {
        this.discounts.remove(subscriptionDiscount);
        subscriptionDiscount.setSubscriptionType(null);
        return this;
    }

    public void setDiscounts(Set<SubscriptionDiscount> subscriptionDiscounts) {
        this.discounts = subscriptionDiscounts;
    }

    public Race getRace() {
        return race;
    }

    public SubscriptionType race(Race race) {
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
        if (!(o instanceof SubscriptionType)) {
            return false;
        }
        return id != null && id.equals(((SubscriptionType) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "SubscriptionType{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", rules='" + getRules() + "'" +
            ", distance=" + getDistance() +
            ", price=" + getPrice() +
            "}";
    }
}
