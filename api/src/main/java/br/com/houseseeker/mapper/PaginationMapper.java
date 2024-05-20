package br.com.houseseeker.mapper;

import br.com.houseseeker.domain.output.PaginationOutput;
import br.com.houseseeker.domain.proto.PaginationResponseData;
import org.mapstruct.Mapper;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public abstract class PaginationMapper {

    public abstract PaginationOutput toOutput(PaginationResponseData data);

}
