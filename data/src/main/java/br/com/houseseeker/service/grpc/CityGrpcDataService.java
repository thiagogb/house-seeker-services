package br.com.houseseeker.service.grpc;

import br.com.houseseeker.domain.projection.City;
import br.com.houseseeker.mapper.CityMapper;
import br.com.houseseeker.service.UrbanPropertyLocationService;
import br.com.houseseeker.service.proto.CityDataServiceGrpc;
import br.com.houseseeker.service.proto.GetCitiesRequest;
import br.com.houseseeker.service.proto.GetCitiesResponse;
import br.com.houseseeker.util.PaginationUtils;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.data.domain.Page;

import static br.com.houseseeker.util.GrpcServiceUtils.execute;

@GrpcService
@RequiredArgsConstructor
@Slf4j
public class CityGrpcDataService extends CityDataServiceGrpc.CityDataServiceImplBase {

    private final UrbanPropertyLocationService urbanPropertyLocationService;
    private final CityMapper cityMapper;

    @Override
    public void getCities(GetCitiesRequest request, StreamObserver<GetCitiesResponse> responseObserver) {
        log.info("GRPC: executing getCities ...");
        execute(responseObserver, () -> {
            Page<City> result = urbanPropertyLocationService.findDistinctCitiesBy(request);
            return GetCitiesResponse.newBuilder()
                                    .addAllCities(cityMapper.toProto(result.getContent()))
                                    .setPagination(PaginationUtils.toPaginationResponseData(result))
                                    .build();
        });
    }

}
