package br.com.houseseeker.entity;

import br.com.houseseeker.util.EntityUtils;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

import static java.util.Objects.nonNull;

@Entity
@Table(name = "urban_property_price_variation")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class UrbanPropertyPriceVariation implements Serializable {

    @Serial
    private static final long serialVersionUID = -1731189698948803643L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "urban_property_price_variation_seq_gen")
    @SequenceGenerator(name = "urban_property_price_variation_seq_gen", sequenceName = "urban_property_price_variation_seq", allocationSize = 1)
    @Column(name = "id", nullable = false, updatable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_urban_property", nullable = false, updatable = false)
    @ToString.Exclude
    private UrbanProperty urbanProperty;

    @Column(name = "analysis_date", nullable = false)
    private LocalDateTime analysisDate;

    @Column(name = "type", nullable = false, length = 15)
    @Enumerated(EnumType.STRING)
    private Type type;

    @Column(name = "price", precision = 11, scale = 2)
    private BigDecimal price;

    @Column(name = "variation", precision = 8, scale = 2)
    private BigDecimal variation;

    @Override
    public final boolean equals(Object obj) {
        return EntityUtils.isEqual(this, obj, o -> nonNull(getId()) && Objects.equals(getId(), o.getId()));
    }

    @Override
    public final int hashCode() {
        return EntityUtils.hashCode(this);
    }

    @RequiredArgsConstructor
    public enum Type {

        SELL {
            @Override
            public BigDecimal getComparatorAttribute(UrbanProperty urbanProperty) {
                return urbanProperty.getSellPrice();
            }
        }, RENT {
            @Override
            public BigDecimal getComparatorAttribute(UrbanProperty urbanProperty) {
                return urbanProperty.getRentPrice();
            }
        }, CONDOMINIUM {
            @Override
            public BigDecimal getComparatorAttribute(UrbanProperty urbanProperty) {
                return urbanProperty.getCondominiumPrice();
            }
        };

        public abstract BigDecimal getComparatorAttribute(@NotNull UrbanProperty urbanProperty);

    }

}
