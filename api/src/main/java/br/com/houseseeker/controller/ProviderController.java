package br.com.houseseeker.controller;

import br.com.houseseeker.domain.argument.ProviderInput;
import br.com.houseseeker.domain.dto.ProviderDto;
import br.com.houseseeker.domain.input.ProviderCreationInput;
import br.com.houseseeker.domain.input.ProviderEditionInput;
import br.com.houseseeker.domain.output.ProviderOutput;
import br.com.houseseeker.mapper.PaginationMapper;
import br.com.houseseeker.mapper.ProviderMapper;
import br.com.houseseeker.service.ProviderService;
import br.com.houseseeker.service.proto.GetProvidersDataResponse;
import graphql.schema.DataFetchingEnvironment;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping(path = "/api/rest/providers")
@RequiredArgsConstructor
@Slf4j
public class ProviderController extends AbstractController {

    private final ProviderService providerService;
    private final ProviderMapper providerMapper;
    private final PaginationMapper paginationMapper;

    @QueryMapping
    public ProviderOutput providers(@Argument ProviderInput input, DataFetchingEnvironment dataFetchingEnvironment) {
        GetProvidersDataResponse response = providerService.findBy(getProjections(dataFetchingEnvironment), input);
        return ProviderOutput.builder()
                             .rows(providerMapper.toDto(response.getProvidersList()))
                             .pagination(paginationMapper.toOutput(response.getPagination()))
                             .build();
    }

    @MutationMapping
    public ProviderDto insertProvider(@Valid @Argument ProviderCreationInput input) {
        return providerMapper.toDto(providerService.insert(input));
    }

    @MutationMapping
    public ProviderDto updateProvider(
            @Argument Integer id,
            @Valid @Argument ProviderEditionInput input,
            DataFetchingEnvironment dataFetchingEnvironment
    ) {
        input.detectedChangedArguments(dataFetchingEnvironment);
        return providerMapper.toDto(providerService.update(id, input));
    }

    @GetMapping("/{id}/logo")
    public ResponseEntity<byte[]> getProviderLogo(@PathVariable("id") Integer id) {
        return Optional.ofNullable(providerService.getLogo(id))
                       .map(ResponseEntity::ok)
                       .orElseGet(() -> ResponseEntity.noContent().build());
    }

    @DeleteMapping("/{id}/wipe")
    public ResponseEntity<Void> wipeProvider(@PathVariable("id") Integer id) {
        providerService.wipe(id);
        return ResponseEntity.ok().build();
    }

}
