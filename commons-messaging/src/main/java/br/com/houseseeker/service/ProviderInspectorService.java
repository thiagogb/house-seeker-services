package br.com.houseseeker.service;

import br.com.houseseeker.domain.provider.ProviderMetadata;
import br.com.houseseeker.domain.provider.ProviderParameters;
import br.com.houseseeker.util.ObjectMapperUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static org.apache.commons.lang3.StringUtils.isBlank;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProviderInspectorService {

    private final ObjectMapper objectMapper;

    public ProviderParameters getParameters(@NotNull ProviderMetadata providerMetadata) {
        if (isBlank(providerMetadata.getParams()))
            return ProviderParameters.DEFAULT;

        return ObjectMapperUtils.deserializeAs(objectMapper, providerMetadata.getParams(), ProviderParameters.class);
    }

}
