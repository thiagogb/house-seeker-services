package br.com.houseseeker.service.batch;

import br.com.houseseeker.domain.dto.UrbanPropertyDto;
import br.com.houseseeker.domain.dto.UrbanPropertyPriceVariationDto;
import br.com.houseseeker.mapper.UrbanPropertyPriceVariationMapper;
import br.com.houseseeker.service.UrbanPropertyPriceVariationService;
import br.com.houseseeker.service.proto.GetUrbanPropertyPriceVariationsResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UrbanPropertyPriceVariationBatchMappingService {

    private final UrbanPropertyPriceVariationService urbanPropertyPriceVariationService;
    private final UrbanPropertyPriceVariationMapper urbanPropertyPriceVariationMapper;

    public Map<UrbanPropertyDto, List<UrbanPropertyPriceVariationDto>> map(@NotNull List<UrbanPropertyDto> urbanProperties) {
        Map<Integer, List<UrbanPropertyPriceVariationDto>> propertyPriceVariationsMap = mapToDtoAndGroupByPropertyId(
                urbanPropertyPriceVariationService.findByUrbanProperties(UrbanPropertyDto.extractIds(urbanProperties))
        );

        return urbanProperties.stream()
                              .collect(Collectors.toMap(
                                      Function.identity(),
                                      urbanProperty -> propertyPriceVariationsMap.get(urbanProperty.getId())
                              ));
    }

    private Map<Integer, List<UrbanPropertyPriceVariationDto>> mapToDtoAndGroupByPropertyId(GetUrbanPropertyPriceVariationsResponse response) {
        return response.getUrbanPropertyPriceVariationsList()
                       .stream()
                       .collect(Collectors.groupingBy(
                               data -> data.getUrbanProperty().getId().getValue(),
                               Collectors.collectingAndThen(Collectors.toCollection(LinkedList::new), urbanPropertyPriceVariationMapper::toDto)
                       ));
    }

}
