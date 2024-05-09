package br.com.houseseeker.entity;

import br.com.houseseeker.util.EntityUtils;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

import static java.util.Objects.nonNull;

@Entity
@Table(name = "urban_property_location")
@NoArgsConstructor
@Getter
@ToString
@Accessors(chain = true)
public class UrbanPropertyLocation implements Serializable {

    @Serial
    private static final long serialVersionUID = 1159740294928704315L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "urban_property_location_seq_gen")
    @SequenceGenerator(name = "urban_property_location_seq_gen", sequenceName = "urban_property_location_seq", allocationSize = 1)
    @Column(name = "id", nullable = false, updatable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_urban_property", nullable = false, updatable = false)
    @Setter
    @ToString.Exclude
    private UrbanProperty urbanProperty;

    @Column(name = "state", length = 2)
    @Setter
    private String state;

    @Column(name = "city")
    @Setter
    private String city;

    @Column(name = "district")
    @Setter
    private String district;

    @Column(name = "zipcode", length = 8)
    @Setter
    private String zipCode;

    @Column(name = "street_name")
    @Setter
    private String streetName;

    @Column(name = "street_number")
    @Setter
    private Integer streetNumber;

    @Column(name = "complement")
    @Setter
    private String complement;

    @Column(name = "latitude", precision = 12, scale = 9)
    @Setter
    private BigDecimal latitude;

    @Column(name = "longitude", precision = 12, scale = 9)
    @Setter
    private BigDecimal longitude;

    @Builder
    public UrbanPropertyLocation(
            UrbanProperty urbanProperty,
            String state,
            String city,
            String district,
            String zipCode,
            String streetName,
            Integer streetNumber,
            String complement,
            BigDecimal latitude,
            BigDecimal longitude
    ) {
        this.urbanProperty = urbanProperty;
        this.state = state;
        this.city = city;
        this.district = district;
        this.zipCode = zipCode;
        this.streetName = streetName;
        this.streetNumber = streetNumber;
        this.complement = complement;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public final boolean equals(Object obj) {
        return EntityUtils.isEqual(this, obj, o -> nonNull(getId()) && Objects.equals(getId(), o.getId()));
    }

    @Override
    public final int hashCode() {
        return EntityUtils.hashCode(this);
    }

}