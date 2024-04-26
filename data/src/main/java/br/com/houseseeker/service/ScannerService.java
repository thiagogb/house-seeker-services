package br.com.houseseeker.service;

import br.com.houseseeker.entity.Provider;
import br.com.houseseeker.entity.Scanner;
import br.com.houseseeker.repository.ScannerRepository;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScannerService {

    private final ScannerRepository scannerRepository;

    public Scanner successful(@NotNull Provider provider, @NotNull LocalDateTime creationDate) {
        return scannerRepository.saveAndFlush(
                Scanner.builder()
                       .provider(provider)
                       .creationDate(creationDate)
                       .endDate(LocalDateTime.now())
                       .status(Scanner.ScannerStatus.SUCCESS)
                       .build()
        );
    }

    public Scanner failed(@NotNull Provider provider, @NotNull LocalDateTime creationDate, @NotNull Throwable throwable) {
        return scannerRepository.saveAndFlush(
                Scanner.builder()
                       .provider(provider)
                       .creationDate(creationDate)
                       .endDate(LocalDateTime.now())
                       .status(Scanner.ScannerStatus.FAILED)
                       .errorMessage(throwable.getMessage())
                       .stackTrace(ExceptionUtils.getStackTrace(throwable))
                       .build()
        );
    }

}
