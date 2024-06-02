package br.com.houseseeker.service.batch;

import br.com.houseseeker.domain.dto.UrbanPropertyDto;
import br.com.houseseeker.domain.dto.UrbanPropertyLocationDto;
import br.com.houseseeker.mapper.UrbanPropertyLocationMapper;
import br.com.houseseeker.service.UrbanPropertyLocationService;
import br.com.houseseeker.service.proto.GetUrbanPropertyLocationsResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UrbanPropertyLocationBatchMappingService {

    private final UrbanPropertyLocationService urbanPropertyLocationService;
    private final UrbanPropertyLocationMapper urbanPropertyLocationMapper;

    public Map<UrbanPropertyDto, UrbanPropertyLocationDto> map(@NotNull List<UrbanPropertyDto> urbanProperties) {
        Map<Integer, UrbanPropertyLocationDto> propertyLocationMap = mapToDtoAndGroupByPropertyId(
                urbanPropertyLocationService.findByUrbanProperties(UrbanPropertyDto.extractIds(urbanProperties))
        );

        return urbanProperties.stream()
                              .collect(Collectors.toMap(
                                      Function.identity(),
                                      urbanProperty -> propertyLocationMap.get(urbanProperty.getId())
                              ));
    }

    private Map<Integer, UrbanPropertyLocationDto> mapToDtoAndGroupByPropertyId(GetUrbanPropertyLocationsResponse response) {
        return response.getUrbanPropertyLocationsList()
                       .stream()
                       .collect(Collectors.toMap(
                               data -> data.getUrbanProperty().getId().getValue(),
                               urbanPropertyLocationMapper::toDto
                       ));
    }

}
