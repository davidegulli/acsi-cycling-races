package it.acsi.cycling.races.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.time.LocalDate;

import it.acsi.cycling.races.domain.enumeration.DiscountType;

/**
 * A SubscriptionDiscount.
 */
@Entity
@Table(name = "subscription_discount")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "subscriptiondiscount")
public class SubscriptionDiscount implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "discount")
    private Double discount;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private DiscountType type;

    @Column(name = "expiration_date")
    private LocalDate expirationDate;

    @ManyToOne
    @JsonIgnoreProperties("discounts")
    private SubscriptionType subscriptionType;

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

    public SubscriptionDiscount name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getDiscount() {
        return discount;
    }

    public SubscriptionDiscount discount(Double discount) {
        this.discount = discount;
        return this;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public DiscountType getType() {
        return type;
    }

    public SubscriptionDiscount type(DiscountType type) {
        this.type = type;
        return this;
    }

    public void setType(DiscountType type) {
        this.type = type;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public SubscriptionDiscount expirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
        return this;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public SubscriptionType getSubscriptionType() {
        return subscriptionType;
    }

    public SubscriptionDiscount subscriptionType(SubscriptionType subscriptionType) {
        this.subscriptionType = subscriptionType;
        return this;
    }

    public void setSubscriptionType(SubscriptionType subscriptionType) {
        this.subscriptionType = subscriptionType;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SubscriptionDiscount)) {
            return false;
        }
        return id != null && id.equals(((SubscriptionDiscount) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "SubscriptionDiscount{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", discount=" + getDiscount() +
            ", type='" + getType() + "'" +
            ", expirationDate='" + getExpirationDate() + "'" +
            "}";
    }
}
