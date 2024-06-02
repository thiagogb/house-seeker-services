package br.com.houseseeker.service.batch;

import br.com.houseseeker.domain.dto.UrbanPropertyDto;
import br.com.houseseeker.domain.dto.UrbanPropertyMeasureDto;
import br.com.houseseeker.mapper.UrbanPropertyMeasureMapper;
import br.com.houseseeker.service.UrbanPropertyMeasureService;
import br.com.houseseeker.service.proto.GetUrbanPropertyMeasuresResponse;
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
public class UrbanPropertyMeasureBatchMappingService {

    private final UrbanPropertyMeasureService urbanPropertyMeasureService;
    private final UrbanPropertyMeasureMapper urbanPropertyMeasureMapper;

    public Map<UrbanPropertyDto, UrbanPropertyMeasureDto> map(@NotNull List<UrbanPropertyDto> urbanProperties) {
        Map<Integer, UrbanPropertyMeasureDto> propertyMeasureMap = mapToDtoAndGroupByPropertyId(
                urbanPropertyMeasureService.findByUrbanProperties(UrbanPropertyDto.extractIds(urbanProperties))
        );

        return urbanProperties.stream()
                              .collect(Collectors.toMap(
                                      Function.identity(),
                                      urbanProperty -> propertyMeasureMap.get(urbanProperty.getId())
                              ));
    }

    private Map<Integer, UrbanPropertyMeasureDto> mapToDtoAndGroupByPropertyId(GetUrbanPropertyMeasuresResponse response) {
        return response.getUrbanPropertyMeasuresList()
                       .stream()
                       .collect(Collectors.toMap(
                               data -> data.getUrbanProperty().getId().getValue(),
                               urbanPropertyMeasureMapper::toDto
                       ));
    }

}
