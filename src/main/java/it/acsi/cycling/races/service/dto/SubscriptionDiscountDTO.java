package it.acsi.cycling.races.service.dto;
import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import it.acsi.cycling.races.domain.enumeration.DiscountType;

/**
 * A DTO for the {@link it.acsi.cycling.races.domain.SubscriptionDiscount} entity.
 */
public class SubscriptionDiscountDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    private Double discount;

    private DiscountType type;

    private LocalDate expirationDate;


    private Long subscriptionTypeId;

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

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public DiscountType getType() {
        return type;
    }

    public void setType(DiscountType type) {
        this.type = type;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Long getSubscriptionTypeId() {
        return subscriptionTypeId;
    }

    public void setSubscriptionTypeId(Long subscriptionTypeId) {
        this.subscriptionTypeId = subscriptionTypeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SubscriptionDiscountDTO subscriptionDiscountDTO = (SubscriptionDiscountDTO) o;
        if (subscriptionDiscountDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), subscriptionDiscountDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SubscriptionDiscountDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", discount=" + getDiscount() +
            ", type='" + getType() + "'" +
            ", expirationDate='" + getExpirationDate() + "'" +
            ", subscriptionType=" + getSubscriptionTypeId() +
            "}";
    }
}
