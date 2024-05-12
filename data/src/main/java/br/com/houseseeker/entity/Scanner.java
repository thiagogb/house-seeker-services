package br.com.houseseeker.entity;

import br.com.houseseeker.entity.converter.ScannerStatusToVarCharConverter;
import br.com.houseseeker.util.EntityUtils;
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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

import static java.util.Objects.nonNull;

@Entity
@Table(name = "scanner")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
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
    private Provider provider;

    @Column(name = "creation_date", nullable = false, updatable = false)
    private LocalDateTime creationDate;

    @Column(name = "end_date", nullable = false, updatable = false)
    private LocalDateTime endDate;

    @Column(name = "status", nullable = false, updatable = false, length = 1)
    @Convert(converter = ScannerStatusToVarCharConverter.class)
    private ScannerStatus status;

    @Column(name = "error_message")
    private String errorMessage;

    @Column(name = "stack_trace")
    private String stackTrace;

    @Override
    public final boolean equals(Object obj) {
        return EntityUtils.isEqual(this, obj, o -> nonNull(getId()) && Objects.equals(getId(), o.getId()));
    }

    @Override
    public final int hashCode() {
        return EntityUtils.hashCode(this);
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
