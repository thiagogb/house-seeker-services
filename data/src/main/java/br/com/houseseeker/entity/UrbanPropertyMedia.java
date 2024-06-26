package br.com.houseseeker.entity;

import br.com.houseseeker.domain.property.UrbanPropertyMediaType;
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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

import static java.util.Objects.nonNull;

@Entity
@Table(name = "urban_property_media")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class UrbanPropertyMedia implements Serializable {

    @Serial
    private static final long serialVersionUID = -2150821381344259380L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "urban_property_media_seq_gen")
    @SequenceGenerator(name = "urban_property_media_seq_gen", sequenceName = "urban_property_media_seq", allocationSize = 1)
    @Column(name = "id", nullable = false, updatable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_urban_property", nullable = false, updatable = false)
    @ToString.Exclude
    private UrbanProperty urbanProperty;

    @Column(name = "link", nullable = false, length = 1000)
    private String link;

    @Column(name = "link_thumb", length = 1000)
    private String linkThumb;

    @Column(name = "media_type", nullable = false, length = 5)
    @Enumerated(EnumType.STRING)
    private UrbanPropertyMediaType mediaType;

    @Column(name = "extension", nullable = false, length = 10)
    private String extension;

    @Override
    public final boolean equals(Object obj) {
        return EntityUtils.isEqual(this, obj, o -> nonNull(getId()) && Objects.equals(getId(), o.getId()));
    }

    @Override
    public final int hashCode() {
        return EntityUtils.hashCode(this);
    }

}
