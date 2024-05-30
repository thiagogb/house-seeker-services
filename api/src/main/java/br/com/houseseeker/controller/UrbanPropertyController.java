package br.com.houseseeker.controller;

import br.com.houseseeker.domain.argument.UrbanPropertyInput;
import br.com.houseseeker.domain.dto.UrbanPropertyConvenienceDto;
import br.com.houseseeker.domain.dto.UrbanPropertyDto;
import br.com.houseseeker.domain.dto.UrbanPropertyLocationDto;
import br.com.houseseeker.domain.dto.UrbanPropertyMeasureDto;
import br.com.houseseeker.domain.dto.UrbanPropertyMediaDto;
import br.com.houseseeker.domain.dto.UrbanPropertyPriceVariationDto;
import br.com.houseseeker.domain.output.UrbanPropertyOutput;
import br.com.houseseeker.mapper.PaginationMapper;
import br.com.houseseeker.mapper.UrbanPropertyMapper;
import br.com.houseseeker.service.UrbanPropertyService;
import br.com.houseseeker.service.batch.UrbanPropertyConvenienceBatchMappingService;
import br.com.houseseeker.service.batch.UrbanPropertyLocationBatchMappingService;
import br.com.houseseeker.service.batch.UrbanPropertyMeasureBatchMappingService;
import br.com.houseseeker.service.batch.UrbanPropertyMediaBatchMappingService;
import br.com.houseseeker.service.batch.UrbanPropertyPriceVariationBatchMappingService;
import br.com.houseseeker.service.proto.GetUrbanPropertiesResponse;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.BatchMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@Slf4j
public class UrbanPropertyController extends AbstractController {

    private final UrbanPropertyService urbanPropertyService;
    private final UrbanPropertyLocationBatchMappingService urbanPropertyLocationBatchMappingService;
    private final UrbanPropertyMeasureBatchMappingService urbanPropertyMeasureBatchMappingService;
    private final UrbanPropertyConvenienceBatchMappingService urbanPropertyConvenienceBatchMappingService;
    private final UrbanPropertyMediaBatchMappingService urbanPropertyMediaBatchMappingService;
    private final UrbanPropertyPriceVariationBatchMappingService urbanPropertyPriceVariationBatchMappingService;
    private final UrbanPropertyMapper urbanPropertyMapper;
    private final PaginationMapper paginationMapper;

    @QueryMapping
    public UrbanPropertyOutput urbanProperties(@Argument UrbanPropertyInput input, DataFetchingEnvironment dataFetchingEnvironment) {
        GetUrbanPropertiesResponse response = urbanPropertyService.findBy(getProjections(dataFetchingEnvironment), input);
        return UrbanPropertyOutput.builder()
                                  .rows(urbanPropertyMapper.toDto(response.getUrbanPropertiesList()))
                                  .pagination(paginationMapper.toOutput(response.getPagination()))
                                  .build();
    }

    @BatchMapping(typeName = "UrbanPropertyDto", field = "location")
    public Map<UrbanPropertyDto, UrbanPropertyLocationDto> locations(List<UrbanPropertyDto> urbanProperties) {
        return urbanPropertyLocationBatchMappingService.map(urbanProperties);
    }

    @BatchMapping(typeName = "UrbanPropertyDto", field = "measure")
    public Map<UrbanPropertyDto, UrbanPropertyMeasureDto> measures(List<UrbanPropertyDto> urbanProperties) {
        return urbanPropertyMeasureBatchMappingService.map(urbanProperties);
    }

    @BatchMapping(typeName = "UrbanPropertyDto", field = "conveniences")
    public Map<UrbanPropertyDto, List<UrbanPropertyConvenienceDto>> conveniences(List<UrbanPropertyDto> urbanProperties) {
        return urbanPropertyConvenienceBatchMappingService.map(urbanProperties);
    }

    @BatchMapping(typeName = "UrbanPropertyDto", field = "medias")
    public Map<UrbanPropertyDto, List<UrbanPropertyMediaDto>> medias(List<UrbanPropertyDto> urbanProperties) {
        return urbanPropertyMediaBatchMappingService.map(urbanProperties);
    }

    @BatchMapping(typeName = "UrbanPropertyDto", field = "priceVariations")
    public Map<UrbanPropertyDto, List<UrbanPropertyPriceVariationDto>> priceVariations(List<UrbanPropertyDto> urbanProperties) {
        return urbanPropertyPriceVariationBatchMappingService.map(urbanProperties);
    }

}
