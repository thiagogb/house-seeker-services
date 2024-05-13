package br.com.houseseeker.service.v4;

import br.com.houseseeker.domain.jetimob.v4.PropertyInfoResponse;
import br.com.houseseeker.domain.jetimob.v4.SearchPageResponse;
import br.com.houseseeker.domain.provider.ProviderMetadata;
import br.com.houseseeker.domain.provider.ProviderParameters;
import br.com.houseseeker.domain.provider.ProviderScraperResponse;
import br.com.houseseeker.service.AbstractDataScraperService;
import br.com.houseseeker.util.RetrofitUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

import java.time.Clock;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class DataScraperV4Service extends AbstractDataScraperService {

    private final MetadataTransferV4Service metadataTransferV4Service;

    @Value("${data.scraper.v4.page-size}")
    private int chunkSize;

    public DataScraperV4Service(Clock clock, MetadataTransferV4Service metadataTransferV4Service) {
        super(clock);
        this.metadataTransferV4Service = metadataTransferV4Service;
    }

    @Override
    protected ProviderScraperResponse execute(
            ProviderMetadata providerMetadata,
            ProviderParameters providerParameters,
            Retrofit retrofit
    ) {
        Api api = retrofit.create(Api.class);
        Pair<SearchPageResponse, Integer> firstPageInfo = fetchFirstPageAndCalculateTotalOfPages(api, providerMetadata);
        List<SearchPageResponse.Item> propertyItems = firstPageInfo.getValue() > 0
                ? fetchAllDataFromPages(api, providerMetadata, firstPageInfo)
                : Collections.emptyList();
        List<PropertyInfoResponse> propertyInfos = !propertyItems.isEmpty()
                ? fetchAllPropertiesData(api, providerMetadata, propertyItems)
                : Collections.emptyList();
        return generateResponse(providerMetadata, propertyInfos, metadataTransferV4Service::transfer);
    }

    private Pair<SearchPageResponse, Integer> fetchFirstPageAndCalculateTotalOfPages(Api api, ProviderMetadata providerMetadata) {
        log.info("Calculating total of pages ...");
        SearchPageResponse response = RetrofitUtils.executeCall(api.search(chunkSize, 0));
        int totalOfPages = Math.ceilDiv(response.getTotalItems(), chunkSize);
        log.info("Provider {} has a total of {} pages to fetch", providerMetadata.getName(), totalOfPages);
        return Pair.of(response, totalOfPages);
    }

    private List<SearchPageResponse.Item> fetchAllDataFromPages(
            Api api,
            ProviderMetadata providerMetadata,
            Pair<SearchPageResponse, Integer> firstPageInfo
    ) {
        List<CompletableFuture<SearchPageResponse>> requestList = new LinkedList<>(
                List.of(CompletableFuture.completedFuture(firstPageInfo.getKey()))
        );

        int totalOfPages = firstPageInfo.getValue();
        for (int i = 2; i <= totalOfPages; i++) {
            int page = i;
            requestList.add(CompletableFuture.supplyAsync(() -> {
                log.info("Provider {} requesting page {} of {} ...", providerMetadata.getName(), page, totalOfPages);
                return RetrofitUtils.executeCall(api.search(chunkSize, chunkSize * (page - 1)));
            }));
        }

        return requestList.stream()
                          .flatMap(cf -> cf.join().getItems().stream())
                          .toList();
    }

    private List<PropertyInfoResponse> fetchAllPropertiesData(
            Api api,
            ProviderMetadata providerMetadata,
            List<SearchPageResponse.Item> items
    ) {
        log.info("Fetching details of {} properties", items.size());
        List<CompletableFuture<PropertyInfoResponse>> requestList = items.stream()
                                                                         .map(sr -> fetchPropertyData(api, providerMetadata, sr))
                                                                         .toList();
        return requestList.stream().map(CompletableFuture::join).toList();
    }

    private CompletableFuture<PropertyInfoResponse> fetchPropertyData(
            Api api,
            ProviderMetadata providerMetadata,
            SearchPageResponse.Item item
    ) {
        return CompletableFuture.supplyAsync(() -> RetrofitUtils.executeCall(api.getProperty(item.getCode()))
                                                                .setUrl(providerMetadata.getSiteUrl().concat(item.getUrl()))
        );
    }

    private interface Api {

        @GET("api/frontend/real-estate-data/property/list")
        Call<SearchPageResponse> search(
                @Query("chunkSize") Integer chunkSize,
                @Query("offset") Integer offset
        );

        @GET("api/frontend/real-estate-data/property/{code}")
        Call<PropertyInfoResponse> getProperty(@Path("code") String code);

    }

}
