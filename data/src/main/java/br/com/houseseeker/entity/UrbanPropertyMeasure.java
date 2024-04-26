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
@Table(name = "urban_property_measure")
@NoArgsConstructor
@Getter
@ToString
@Accessors(chain = true)
public class UrbanPropertyMeasure implements Serializable {

    @Serial
    private static final long serialVersionUID = -1955104042452496668L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "urban_property_measure_seq_gen")
    @SequenceGenerator(name = "urban_property_measure_seq_gen", sequenceName = "urban_property_measure_seq", allocationSize = 1)
    @Column(name = "id", nullable = false, updatable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_urban_property", nullable = false, updatable = false)
    @Setter
    @ToString.Exclude
    private UrbanProperty urbanProperty;

    @Column(name = "total_area", precision = 11, scale = 2)
    @Setter
    private BigDecimal totalArea;

    @Column(name = "private_area", precision = 11, scale = 2)
    @Setter
    private BigDecimal privateArea;

    @Column(name = "usable_area", precision = 11, scale = 2)
    @Setter
    private BigDecimal usableArea;

    @Column(name = "terrain_total_area", precision = 11, scale = 2)
    @Setter
    private BigDecimal terrainTotalArea;

    @Column(name = "terrain_front", precision = 11, scale = 2)
    @Setter
    private BigDecimal terrainFront;

    @Column(name = "terrain_back", precision = 11, scale = 2)
    @Setter
    private BigDecimal terrainBack;

    @Column(name = "terrain_left", precision = 11, scale = 2)
    @Setter
    private BigDecimal terrainLeft;

    @Column(name = "terrain_right", precision = 11, scale = 2)
    @Setter
    private BigDecimal terrainRight;

    @Column(name = "area_unit", length = 2)
    @Setter
    private String areaUnit;

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
        UrbanPropertyMeasure that = (UrbanPropertyMeasure) obj;
        return nonNull(getId()) && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy
                ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode()
                : getClass().hashCode();
    }

}
