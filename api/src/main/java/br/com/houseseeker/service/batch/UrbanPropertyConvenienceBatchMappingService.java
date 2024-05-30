package br.com.houseseeker.service.batch;

import br.com.houseseeker.domain.dto.UrbanPropertyConvenienceDto;
import br.com.houseseeker.domain.dto.UrbanPropertyDto;
import br.com.houseseeker.mapper.UrbanPropertyConvenienceMapper;
import br.com.houseseeker.service.UrbanPropertyConvenienceService;
import br.com.houseseeker.service.proto.GetUrbanPropertyConveniencesResponse;
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
public class UrbanPropertyConvenienceBatchMappingService {

    private final UrbanPropertyConvenienceService urbanPropertyConvenienceService;
    private final UrbanPropertyConvenienceMapper urbanPropertyConvenienceMapper;

    public Map<UrbanPropertyDto, List<UrbanPropertyConvenienceDto>> map(@NotNull List<UrbanPropertyDto> urbanProperties) {
        Map<Integer, List<UrbanPropertyConvenienceDto>> propertyConveniencesMap = mapToDtoAndGroupByPropertyId(
                urbanPropertyConvenienceService.findByUrbanProperties(UrbanPropertyDto.extractIds(urbanProperties))
        );

        return urbanProperties.stream()
                              .collect(Collectors.toMap(
                                      Function.identity(),
                                      urbanProperty -> propertyConveniencesMap.get(urbanProperty.getId())
                              ));
    }

    private Map<Integer, List<UrbanPropertyConvenienceDto>> mapToDtoAndGroupByPropertyId(GetUrbanPropertyConveniencesResponse response) {
        return response.getUrbanPropertyConveniencesList()
                       .stream()
                       .collect(Collectors.groupingBy(
                               data -> data.getUrbanProperty().getId().getValue(),
                               Collectors.collectingAndThen(Collectors.toCollection(LinkedList::new), urbanPropertyConvenienceMapper::toDto)
                       ));
    }

}
