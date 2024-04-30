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
import java.util.Objects;

import static java.util.Objects.nonNull;

@Entity
@Table(name = "urban_property_convenience")
@NoArgsConstructor
@Getter
@ToString
@Accessors(chain = true)
public class UrbanPropertyConvenience implements Serializable {

    @Serial
    private static final long serialVersionUID = -3717901525303641030L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "urban_property_convenience_seq_gen")
    @SequenceGenerator(name = "urban_property_convenience_seq_gen", sequenceName = "urban_property_convenience_seq", allocationSize = 1)
    @Column(name = "id", nullable = false, updatable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_urban_property", nullable = false, updatable = false)
    @Setter
    @ToString.Exclude
    private UrbanProperty urbanProperty;

    @Column(name = "description", nullable = false)
    @Setter
    private String description;

    @Builder
    public UrbanPropertyConvenience(UrbanProperty urbanProperty, String description) {
        this.urbanProperty = urbanProperty;
        this.description = description;
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
