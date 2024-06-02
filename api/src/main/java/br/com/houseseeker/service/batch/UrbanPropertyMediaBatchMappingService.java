package br.com.houseseeker.service.batch;

import br.com.houseseeker.domain.dto.UrbanPropertyDto;
import br.com.houseseeker.domain.dto.UrbanPropertyMediaDto;
import br.com.houseseeker.mapper.UrbanPropertyMediaMapper;
import br.com.houseseeker.service.UrbanPropertyMediaService;
import br.com.houseseeker.service.proto.GetUrbanPropertyMediasResponse;
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
public class UrbanPropertyMediaBatchMappingService {

    private final UrbanPropertyMediaService urbanPropertyMediaService;
    private final UrbanPropertyMediaMapper urbanPropertyMediaMapper;

    public Map<UrbanPropertyDto, List<UrbanPropertyMediaDto>> map(@NotNull List<UrbanPropertyDto> urbanProperties) {
        Map<Integer, List<UrbanPropertyMediaDto>> propertyMediasMap = mapToDtoAndGroupByPropertyId(
                urbanPropertyMediaService.findByUrbanProperties(UrbanPropertyDto.extractIds(urbanProperties))
        );

        return urbanProperties.stream()
                              .collect(Collectors.toMap(
                                      Function.identity(),
                                      urbanProperty -> propertyMediasMap.get(urbanProperty.getId())
                              ));
    }

    private Map<Integer, List<UrbanPropertyMediaDto>> mapToDtoAndGroupByPropertyId(GetUrbanPropertyMediasResponse response) {
        return response.getUrbanPropertyMediasList()
                       .stream()
                       .collect(Collectors.groupingBy(
                               data -> data.getUrbanProperty().getId().getValue(),
                               Collectors.collectingAndThen(Collectors.toCollection(LinkedList::new), urbanPropertyMediaMapper::toDto)
                       ));
    }

}
