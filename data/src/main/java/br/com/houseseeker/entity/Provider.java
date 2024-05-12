package br.com.houseseeker.entity;

import br.com.houseseeker.domain.provider.ProviderMechanism;
import br.com.houseseeker.entity.converter.BooleanToVarCharConverter;
import br.com.houseseeker.util.EntityUtils;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
import java.util.Objects;

import static java.util.Objects.nonNull;

@Entity
@Table(name = "provider", uniqueConstraints = {@UniqueConstraint(name = "provider_uk_name", columnNames = "name")})
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class Provider implements Serializable {

    @Serial
    private static final long serialVersionUID = -3278326702355874652L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "provider_seq_gen")
    @SequenceGenerator(name = "provider_seq_gen", sequenceName = "provider_seq", allocationSize = 1)
    @Column(name = "id", nullable = false, updatable = false)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "site_url", nullable = false)
    private String siteUrl;

    @Column(name = "data_url")
    private String dataUrl;

    @Column(name = "mechanism", nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private ProviderMechanism mechanism;

    @Column(name = "params")
    private String params;

    @Column(name = "cron_expression")
    private String cronExpression;

    @Column(name = "logo")
    private byte[] logo;

    @Column(name = "active", nullable = false, length = 1)
    @Convert(converter = BooleanToVarCharConverter.class)
    private Boolean active;

    @Override
    public final boolean equals(Object obj) {
        return EntityUtils.isEqual(this, obj, o -> nonNull(getId()) && Objects.equals(getId(), o.getId()));
    }

    @Override
    public final int hashCode() {
        return EntityUtils.hashCode(this);
    }

}
