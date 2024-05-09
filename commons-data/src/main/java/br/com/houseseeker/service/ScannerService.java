package br.com.houseseeker.service;

import br.com.houseseeker.domain.provider.ProviderScraperResponse;
import br.com.houseseeker.entity.Provider;
import br.com.houseseeker.entity.Scanner;
import br.com.houseseeker.repository.ScannerRepository;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScannerService {

    private final ScannerRepository scannerRepository;
    private final Clock clock;

    public Scanner successful(@NotNull Provider provider, @NotNull LocalDateTime creationDate) {
        return scannerRepository.saveAndFlush(
                Scanner.builder()
                       .provider(provider)
                       .creationDate(creationDate)
                       .endDate(LocalDateTime.now(clock))
                       .status(Scanner.ScannerStatus.SUCCESS)
                       .build()
        );
    }

    public Scanner failed(@NotNull Provider provider, @NotNull LocalDateTime creationDate, @NotNull ProviderScraperResponse.ErrorInfo errorInfo) {
        return scannerRepository.saveAndFlush(
                Scanner.builder()
                       .provider(provider)
                       .creationDate(creationDate)
                       .endDate(LocalDateTime.now(clock))
                       .status(Scanner.ScannerStatus.FAILED)
                       .errorMessage(String.format("%s: %s", errorInfo.getClassName(), errorInfo.getMessage()))
                       .stackTrace(errorInfo.getStackTrace())
                       .build()
        );
    }

    public Scanner failed(@NotNull Provider provider, @NotNull LocalDateTime creationDate, @NotNull Throwable throwable) {
        return scannerRepository.saveAndFlush(
                Scanner.builder()
                       .provider(provider)
                       .creationDate(creationDate)
                       .endDate(LocalDateTime.now(clock))
                       .status(Scanner.ScannerStatus.FAILED)
                       .errorMessage(throwable.getMessage())
                       .stackTrace(ExceptionUtils.getStackTrace(throwable))
                       .build()
        );
    }

}
