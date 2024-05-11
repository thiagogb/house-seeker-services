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
import jakarta.persistence.UniqueConstraint;
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
@Table(
        name = "category_urban_property",
        uniqueConstraints = {
                @UniqueConstraint(name = "category_urban_property_uk_1", columnNames = {"id_category", "id_urban_property"}),
                @UniqueConstraint(name = "category_urban_property_uk_2", columnNames = {"id_category", "show_order"})
        }
)
@NoArgsConstructor
@Getter
@ToString
@Accessors(chain = true)
public class CategoryUrbanProperty implements Serializable {

    @Serial
    private static final long serialVersionUID = 5583117297347988696L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "category_urban_property_seq_gen")
    @SequenceGenerator(name = "category_urban_property_seq_gen", sequenceName = "category_urban_property_seq", allocationSize = 1)
    @Column(name = "id", nullable = false, updatable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_category", nullable = false)
    @Setter
    @ToString.Exclude
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_urban_property", nullable = false)
    @Setter
    @ToString.Exclude
    private UrbanProperty urbanProperty;

    @Column(name = "show_order", nullable = false)
    @Setter
    private Integer showOrder;

    @Override
    public final boolean equals(Object obj) {
        return EntityUtils.isEqual(this, obj, o -> nonNull(getId()) && Objects.equals(getId(), o.getId()));
    }

    @Override
    public final int hashCode() {
        return EntityUtils.hashCode(this);
    }

}
