package br.com.houseseeker.service.v1;

import br.com.houseseeker.domain.exception.ExtendedRuntimeException;
import br.com.houseseeker.domain.jetimob.v1.PropertyInfoMetadata;
import br.com.houseseeker.domain.jetimob.v1.SearchPageMetadata;
import br.com.houseseeker.domain.property.UrbanPropertyContract;
import br.com.houseseeker.domain.provider.ProviderMetadata;
import br.com.houseseeker.domain.provider.ProviderParameters;
import br.com.houseseeker.domain.provider.ProviderScraperResponse;
import br.com.houseseeker.service.AbstractDataScraperService;
import lombok.extern.slf4j.Slf4j;
import okhttp3.ResponseBody;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

import java.io.IOException;
import java.time.Clock;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;
import java.util.stream.Stream;

import static br.com.houseseeker.util.RetrofitUtils.executeCall;
import static java.util.Objects.isNull;

@Service
@Slf4j
public class DataScraperV1Service extends AbstractDataScraperService {

    private static final String ORDER_QUERY_VALUE = "valor_max";

    private final SearchPageV1ScraperService searchPageV1ScraperService;
    private final PropertyPageV1ScraperService propertyPageV1ScraperService;
    private final MetadataTransferV1Service metadataTransferV1Service;

    public DataScraperV1Service(
            Clock clock,
            SearchPageV1ScraperService searchPageV1ScraperService,
            PropertyPageV1ScraperService propertyPageV1ScraperService,
            MetadataTransferV1Service metadataTransferV1Service
    ) {
        super(clock);
        this.searchPageV1ScraperService = searchPageV1ScraperService;
        this.propertyPageV1ScraperService = propertyPageV1ScraperService;
        this.metadataTransferV1Service = metadataTransferV1Service;
    }

    @Override
    protected ProviderScraperResponse execute(
            ProviderMetadata providerMetadata,
            ProviderParameters providerParameters,
            Retrofit retrofit
    ) {
        Api api = retrofit.create(Api.class);
        List<SearchPageMetadata> searchPageMetadataList = fetchAllDataFromSearchPage(api, providerMetadata);
        List<Pair<SearchPageMetadata.Item, PropertyInfoMetadata>> propertyInfoMetadataList = fetchAllDataFromPropertyPages(api, searchPageMetadataList);
        return generateResponse(providerMetadata, propertyInfoMetadataList, metadataTransferV1Service::transfer);
    }

    private List<SearchPageMetadata> fetchAllDataFromSearchPage(Api api, ProviderMetadata providerMetadata) {
        CompletableFuture<Stream<SearchPageMetadata>> searchPageMetadataSell = CompletableFuture.supplyAsync(
                () -> fetchAllDataFromSearchPage(
                        providerMetadata,
                        UrbanPropertyContract.SELL,
                        api::searchByContractSell
                )
        );

        CompletableFuture<Stream<SearchPageMetadata>> searchPageMetadataRent = CompletableFuture.supplyAsync(
                () -> fetchAllDataFromSearchPage(
                        providerMetadata,
                        UrbanPropertyContract.RENT,
                        api::searchByContractRent
                )
        );

        return Stream.of(searchPageMetadataSell, searchPageMetadataRent)
                     .flatMap(CompletableFuture::join)
                     .toList();
    }

    private Stream<SearchPageMetadata> fetchAllDataFromSearchPage(
            ProviderMetadata providerMetadata,
            UrbanPropertyContract contract,
            BiFunction<String, Integer, Call<ResponseBody>> apiCall
    ) {
        Stream.Builder<SearchPageMetadata> builder = Stream.builder();
        try {
            int page = 1;
            Integer totalPages = null;
            do {
                if (isNull(totalPages)) {
                    log.info("Provider {} requesting page {} ...", providerMetadata.getName(), page);
                } else {
                    log.info("Provider {} requesting page {}/{} ...", providerMetadata.getName(), page, totalPages);
                }
                try (var response = executeCall(apiCall.apply(ORDER_QUERY_VALUE, page))) {
                    SearchPageMetadata searchPageMetadata = searchPageV1ScraperService.scrap(response.string())
                                                                                      .setContract(contract);
                    builder.add(searchPageMetadata);
                    totalPages = searchPageMetadata.getPagination().getLastPage();

                    if (page >= totalPages)
                        break;
                }
                page++;
            } while (true);
        } catch (IOException e) {
            throw new ExtendedRuntimeException(e, "Execution failure for provider %s", providerMetadata.getName());
        }
        return builder.build();
    }

    private List<Pair<SearchPageMetadata.Item, PropertyInfoMetadata>> fetchAllDataFromPropertyPages(
            Api api,
            List<SearchPageMetadata> searchPageMetadataList
    ) {
        return searchPageMetadataList.stream()
                                     .parallel()
                                     .flatMap(spm -> fetchAllDataFromPropertyPages(api, spm))
                                     .toList();
    }

    private Stream<Pair<SearchPageMetadata.Item, PropertyInfoMetadata>> fetchAllDataFromPropertyPages(
            Api api,
            SearchPageMetadata searchPageMetadata
    ) {
        Stream.Builder<Pair<SearchPageMetadata.Item, PropertyInfoMetadata>> builder = Stream.builder();
        for (SearchPageMetadata.Item searchResultItem : searchPageMetadata.getItems()) {
            try (var responseBody = executeCall(api.getPage(searchResultItem.getPageLink()))) {
                builder.add(Pair.of(
                        searchResultItem,
                        propertyPageV1ScraperService.scrap(responseBody.string())
                                                    .setContract(searchPageMetadata.getContract())
                ));
            } catch (IOException e) {
                throw new ExtendedRuntimeException(e);
            }
        }
        return builder.build();
    }

    private interface Api {

        @GET("imoveis/a-venda")
        Call<ResponseBody> searchByContractSell(
                @Query("ordem") String order,
                @Query("pagina") Integer page
        );

        @GET("imoveis/alugar")
        Call<ResponseBody> searchByContractRent(
                @Query("ordem") String order,
                @Query("pagina") Integer page
        );

        @GET
        Call<ResponseBody> getPage(@Url String url);

    }

}
