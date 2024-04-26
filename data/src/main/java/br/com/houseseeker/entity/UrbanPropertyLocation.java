package br.com.houseseeker.entity;

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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.proxy.HibernateProxy;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

import static java.util.Objects.isNull;
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

    @Override
    public final boolean equals(Object obj) {
        if (this == obj) return true;
        if (isNull(obj)) return false;
        Class<?> oEffectiveClass = obj instanceof HibernateProxy
                ? ((HibernateProxy) obj).getHibernateLazyInitializer().getPersistentClass()
                : obj.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy
                ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass()
                : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        UrbanPropertyLocation that = (UrbanPropertyLocation) obj;
        return nonNull(getId()) && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy
                ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode()
                : getClass().hashCode();
    }

}
