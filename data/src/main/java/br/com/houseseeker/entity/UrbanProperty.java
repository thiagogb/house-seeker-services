package br.com.houseseeker.entity;

import br.com.houseseeker.domain.property.UrbanPropertyContract;
import br.com.houseseeker.domain.property.UrbanPropertyStatus;
import br.com.houseseeker.domain.property.UrbanPropertyType;
import br.com.houseseeker.entity.converter.BooleanToVarCharConverter;
import br.com.houseseeker.util.EntityUtils;
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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

import static java.util.Objects.nonNull;

@Entity
@Table(
        name = "urban_property",
        uniqueConstraints = {@UniqueConstraint(name = "urban_property_uk_provider_code", columnNames = {"id_provider", "provider_code"})}
)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
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
    @ToString.Exclude
    private Provider provider;

    @Column(name = "provider_code", nullable = false, length = 50)
    private String providerCode;

    @Column(name = "url", nullable = false)
    private String url;

    @Column(name = "contract", nullable = false, length = 5)
    @Enumerated(EnumType.STRING)
    private UrbanPropertyContract contract;

    @Column(name = "type", length = 25)
    @Enumerated(EnumType.STRING)
    private UrbanPropertyType type;

    @Column(name = "sub_type", length = 100)
    private String subType;

    @Column(name = "dormitories")
    private Integer dormitories;

    @Column(name = "suites")
    private Integer suites;

    @Column(name = "bathrooms")
    private Integer bathrooms;

    @Column(name = "garages")
    private Integer garages;

    @Column(name = "sell_price", precision = 11, scale = 2)
    private BigDecimal sellPrice;

    @Column(name = "rent_price", precision = 11, scale = 2)
    private BigDecimal rentPrice;

    @Column(name = "condominium_price", precision = 11, scale = 2)
    private BigDecimal condominiumPrice;

    @Column(name = "condominium_name")
    private String condominiumName;

    @Column(name = "exchangeable", length = 1)
    @Convert(converter = BooleanToVarCharConverter.class)
    private Boolean exchangeable;

    @Column(name = "status", length = 10)
    @Enumerated(EnumType.STRING)
    private UrbanPropertyStatus status;

    @Column(name = "financeable", length = 1)
    @Convert(converter = BooleanToVarCharConverter.class)
    private Boolean financeable;

    @Column(name = "occupied", length = 1)
    @Convert(converter = BooleanToVarCharConverter.class)
    private Boolean occupied;

    @Column(name = "notes")
    private String notes;

    @Column(name = "creation_date", nullable = false, updatable = false)
    private LocalDateTime creationDate;

    @Column(name = "last_analysis_date")
    private LocalDateTime lastAnalysisDate;

    @Column(name = "exclusion_date")
    private LocalDateTime exclusionDate;

    @Column(name = "analyzable", nullable = false, length = 1)
    @Convert(converter = BooleanToVarCharConverter.class)
    private Boolean analyzable;

    @Override
    public final boolean equals(Object obj) {
        return EntityUtils.isEqual(this, obj, o -> nonNull(getId()) && Objects.equals(getId(), o.getId()));
    }

    @Override
    public final int hashCode() {
        return EntityUtils.hashCode(this);
    }

}
