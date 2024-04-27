package br.com.houseseeker.entity;

import br.com.houseseeker.domain.provider.ProviderMechanism;
import br.com.houseseeker.entity.converter.BooleanToVarCharConverter;
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
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.proxy.HibernateProxy;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Entity
@Table(name = "provider", uniqueConstraints = {@UniqueConstraint(name = "provider_uk_name", columnNames = "name")})
@NoArgsConstructor
@Getter
@ToString
public class Provider implements Serializable {

    @Serial
    private static final long serialVersionUID = -3278326702355874652L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "provider_seq_gen")
    @SequenceGenerator(name = "provider_seq_gen", sequenceName = "provider_seq", allocationSize = 1)
    @Column(name = "id", nullable = false, updatable = false)
    private Integer id;

    @Column(name = "name", nullable = false)
    @Setter
    private String name;

    @Column(name = "site_url", nullable = false)
    @Setter
    private String siteUrl;

    @Column(name = "data_url")
    @Setter
    private String dataUrl;

    @Column(name = "mechanism", nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    @Setter
    private ProviderMechanism mechanism;

    @Column(name = "params")
    @Setter
    private String params;

    @Column(name = "cron_expression")
    @Setter
    private String cronExpression;

    @Column(name = "logo")
    @Setter
    private byte[] logo;

    @Column(name = "active", nullable = false, length = 1)
    @Convert(converter = BooleanToVarCharConverter.class)
    @Setter
    private Boolean active;

    @Builder
    public Provider(
            String name,
            String siteUrl,
            String dataUrl,
            ProviderMechanism mechanism,
            String params,
            String cronExpression,
            byte[] logo,
            Boolean active
    ) {
        this.name = name;
        this.siteUrl = siteUrl;
        this.dataUrl = dataUrl;
        this.mechanism = mechanism;
        this.params = params;
        this.cronExpression = cronExpression;
        this.logo = logo;
        this.active = active;
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
        Provider provider = (Provider) obj;
        return nonNull(id) && Objects.equals(id, provider.id);
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy hibernateProxy
                ? hibernateProxy.getHibernateLazyInitializer().getPersistentClass().hashCode()
                : getClass().hashCode();
    }

}
