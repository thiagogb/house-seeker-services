package br.com.houseseeker.entity;

import br.com.houseseeker.domain.property.UrbanPropertyContract;
import br.com.houseseeker.domain.property.UrbanPropertyStatus;
import br.com.houseseeker.domain.property.UrbanPropertyType;
import br.com.houseseeker.entity.converter.BooleanToVarCharConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
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
import jakarta.persistence.UniqueConstraint;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.proxy.HibernateProxy;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Entity
@Table(
        name = "urban_property",
        uniqueConstraints = {@UniqueConstraint(name = "urban_property_uk_provider_code", columnNames = {"id_provider", "provider_code"})}
)
@NoArgsConstructor
@Getter
@ToString
@Accessors(chain = true)
public class UrbanProperty implements Serializable {

    @Serial
    private static final long serialVersionUID = -4252580736833927461L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "urban_property_seq_gen")
    @SequenceGenerator(name = "urban_property_seq_gen", sequenceName = "urban_property_seq", allocationSize = 1)
    @Column(name = "id", nullable = false, updatable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_provider", nullable = false, updatable = false)
    @Setter
    @ToString.Exclude
    private Provider provider;

    @Column(name = "provider_code", nullable = false, length = 50)
    @Setter
    private String providerCode;

    @Column(name = "url", nullable = false)
    @Setter
    private String url;

    @Column(name = "contract", nullable = false, length = 5)
    @Enumerated(EnumType.STRING)
    @Setter
    private UrbanPropertyContract contract;

    @Column(name = "type", length = 25)
    @Enumerated(EnumType.STRING)
    @Setter
    private UrbanPropertyType type;

    @Column(name = "sub_type", length = 100)
    @Setter
    private String subType;

    @Column(name = "dormitories")
    @Setter
    private Integer dormitories;

    @Column(name = "suites")
    @Setter
    private Integer suites;

    @Column(name = "bathrooms")
    @Setter
    private Integer bathrooms;

    @Column(name = "garages")
    @Setter
    private Integer garages;

    @Column(name = "sell_price", precision = 11, scale = 2)
    @Setter
    private BigDecimal sellPrice;

    @Column(name = "sell_price_variation", precision = 8, scale = 2)
    @Setter
    private BigDecimal sellPriceVariation;

    @Column(name = "rent_price", precision = 11, scale = 2)
    @Setter
    private BigDecimal rentPrice;

    @Column(name = "rent_price_variation", precision = 8, scale = 2)
    @Setter
    private BigDecimal rentPriceVariation;

    @Column(name = "condominium_price", precision = 11, scale = 2)
    @Setter
    private BigDecimal condominiumPrice;

    @Column(name = "condominium_price_variation", precision = 8, scale = 2)
    @Setter
    private BigDecimal condominiumPriceVariation;

    @Column(name = "condominium_name")
    @Setter
    private String condominiumName;

    @Column(name = "exchangeable", length = 1)
    @Convert(converter = BooleanToVarCharConverter.class)
    @Setter
    private Boolean exchangeable;

    @Column(name = "status", length = 10)
    @Enumerated(EnumType.STRING)
    @Setter
    private UrbanPropertyStatus status;

    @Column(name = "financeable", length = 1)
    @Convert(converter = BooleanToVarCharConverter.class)
    @Setter
    private Boolean financeable;

    @Column(name = "occupied", length = 1)
    @Convert(converter = BooleanToVarCharConverter.class)
    @Setter
    private Boolean occupied;

    @Column(name = "notes")
    @Setter
    private String notes;

    @Column(name = "creation_date", nullable = false, updatable = false)
    @Setter
    private LocalDateTime creationDate;

    @Column(name = "last_analysis_date")
    @Setter
    private LocalDateTime lastAnalysisDate;

    @Column(name = "exclusion_date")
    @Setter
    private LocalDateTime exclusionDate;

    @Column(name = "analyzable", nullable = false, length = 1)
    @Convert(converter = BooleanToVarCharConverter.class)
    @Setter
    private Boolean analyzable;

    @Builder
    public UrbanProperty(
            Provider provider,
            String providerCode,
            String url,
            UrbanPropertyContract contract,
            UrbanPropertyType type,
            String subType,
            Integer dormitories,
            Integer suites,
            Integer bathrooms,
            Integer garages,
            BigDecimal sellPrice,
            BigDecimal sellPriceVariation,
            BigDecimal rentPrice,
            BigDecimal rentPriceVariation,
            BigDecimal condominiumPrice,
            BigDecimal condominiumPriceVariation,
            String condominiumName,
            Boolean exchangeable,
            UrbanPropertyStatus status,
            Boolean financeable,
            Boolean occupied,
            String notes,
            LocalDateTime creationDate,
            LocalDateTime lastAnalysisDate,
            LocalDateTime exclusionDate,
            Boolean analyzable
    ) {
        this.provider = provider;
        this.providerCode = providerCode;
        this.url = url;
        this.contract = contract;
        this.type = type;
        this.subType = subType;
        this.dormitories = dormitories;
        this.suites = suites;
        this.bathrooms = bathrooms;
        this.garages = garages;
        this.sellPrice = sellPrice;
        this.sellPriceVariation = sellPriceVariation;
        this.rentPrice = rentPrice;
        this.rentPriceVariation = rentPriceVariation;
        this.condominiumPrice = condominiumPrice;
        this.condominiumPriceVariation = condominiumPriceVariation;
        this.condominiumName = condominiumName;
        this.exchangeable = exchangeable;
        this.status = status;
        this.financeable = financeable;
        this.occupied = occupied;
        this.notes = notes;
        this.creationDate = creationDate;
        this.lastAnalysisDate = lastAnalysisDate;
        this.exclusionDate = exclusionDate;
        this.analyzable = analyzable;
    }

    @Override
    public final boolean equals(Object obj) {
        if (this == obj) return true;
        if (isNull(obj)) return false;
        Class<?> oEffectiveClass = obj instanceof HibernateProxy hibernateProxy
                ? hibernateProxy.getHibernateLazyInitializer().getPersistentClass()
                : obj.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy hibernateProxy
                ? hibernateProxy.getHibernateLazyInitializer().getPersistentClass()
                : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        UrbanProperty that = (UrbanProperty) obj;
        return nonNull(getId()) && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy hibernateProxy
                ? hibernateProxy.getHibernateLazyInitializer().getPersistentClass().hashCode()
                : getClass().hashCode();
    }

}
