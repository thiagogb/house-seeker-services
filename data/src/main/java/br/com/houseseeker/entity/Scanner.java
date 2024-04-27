package br.com.houseseeker.entity;

import br.com.houseseeker.entity.converter.ScannerStatusToVarCharConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
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
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.proxy.HibernateProxy;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Entity
@Table(name = "scanner")
@NoArgsConstructor
@Getter
@ToString
@Accessors(chain = true)
public class Scanner implements Serializable {

    @Serial
    private static final long serialVersionUID = -1866372174812822860L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "scanner_seq_gen")
    @SequenceGenerator(name = "scanner_seq_gen", sequenceName = "scanner_seq", allocationSize = 1)
    @Column(name = "id", nullable = false, updatable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_provider", nullable = false, updatable = false)
    @Setter
    private Provider provider;

    @Column(name = "creation_date", nullable = false, updatable = false)
    @Setter
    private LocalDateTime creationDate;

    @Column(name = "end_date", nullable = false, updatable = false)
    @Setter
    private LocalDateTime endDate;

    @Column(name = "status", nullable = false, updatable = false, length = 1)
    @Convert(converter = ScannerStatusToVarCharConverter.class)
    @Setter
    private ScannerStatus status;

    @Column(name = "error_message")
    @Setter
    private String errorMessage;

    @Column(name = "stack_trace")
    @Setter
    private String stackTrace;

    @Builder
    public Scanner(
            Provider provider,
            LocalDateTime creationDate,
            LocalDateTime endDate,
            ScannerStatus status,
            String errorMessage,
            String stackTrace
    ) {
        this.provider = provider;
        this.creationDate = creationDate;
        this.endDate = endDate;
        this.status = status;
        this.errorMessage = errorMessage;
        this.stackTrace = stackTrace;
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
        Scanner that = (Scanner) obj;
        return nonNull(getId()) && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy hibernateProxy
                ? hibernateProxy.getHibernateLazyInitializer().getPersistentClass().hashCode()
                : getClass().hashCode();
    }

    @RequiredArgsConstructor
    @Getter
    public enum ScannerStatus {

        SUCCESS("S"),
        FAILED("F");

        private final String value;

        public static Optional<ScannerStatus> findByValue(String value) {
            for (ScannerStatus scannerStatus : values())
                if (scannerStatus.getValue().equals(value))
                    return Optional.of(scannerStatus);

            return Optional.empty();
        }

    }

}
