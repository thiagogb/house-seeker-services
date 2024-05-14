package br.com.houseseeker.service.validator;

import br.com.houseseeker.domain.exception.GrpcStatusException;
import br.com.houseseeker.util.ObjectMapperUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.grpc.Status;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Service
@RequiredArgsConstructor
@Slf4j
public class EntityValidatorService {

    private final Validator validator;
    private final ObjectMapper objectMapper;

    public <T> void validate(T object) {
        Errors errors = validator.validateObject(object);

        if (!errors.hasErrors())
            return;

        throw new GrpcStatusException(
                Status.INVALID_ARGUMENT,
                ObjectMapperUtils.serialize(objectMapper, errors.getAllErrors())
        );
    }

}
