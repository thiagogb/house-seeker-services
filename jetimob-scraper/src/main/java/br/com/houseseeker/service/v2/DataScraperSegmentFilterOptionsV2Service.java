package br.com.houseseeker.service.v2;

import br.com.houseseeker.domain.exception.ExtendedRuntimeException;
import br.com.houseseeker.domain.jetimob.v2.FilterOptionsMetadata;
import br.com.houseseeker.domain.jetimob.v2.ScraperAnalysisProperties;
import br.com.houseseeker.domain.property.UrbanPropertyContract;
import br.com.houseseeker.domain.provider.ProviderMetadata;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.ResponseBody;
import org.apache.hc.core5.net.URIBuilder;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Url;

import java.io.IOException;
import java.net.URISyntaxException;

import static br.com.houseseeker.util.RetrofitUtils.executeCall;

@Service
@RequiredArgsConstructor
@Slf4j
public class DataScraperSegmentFilterOptionsV2Service {

    private final FilterOptionsV2ScraperService filterOptionsV2ScraperService;

    public FilterOptionsMetadata fetch(
            @NotNull Retrofit retrofit,
            @NotNull ProviderMetadata providerMetadata,
            @NotNull ScraperAnalysisProperties analysisProperties,
            @NotNull String segment,
            @NotNull UrbanPropertyContract contract
    ) {
        Api api = retrofit.create(Api.class);
        return fetchFilterOptions(api, analysisProperties, providerMetadata.getBaseUrl(), segment, contract);
    }

    private FilterOptionsMetadata fetchFilterOptions(
            Api api,
            ScraperAnalysisProperties analysisProperties,
            String baseUrl,
            String segment,
            UrbanPropertyContract contract
    ) {
        try {
            String url = new URIBuilder(baseUrl).appendPathSegments(segment).build().toString();
            try (var response = executeCall(api.getFilterOptions(url))) {
                FilterOptionsMetadata filterOptionsMetadata = filterOptionsV2ScraperService.scrap(response.string());
                return filterOptionsMetadata.setContract(contract)
                                            .setCities(analysisProperties.applyCitiesFilter(filterOptionsMetadata.getCities()))
                                            .setTypes(analysisProperties.applySubTypesFilter(filterOptionsMetadata.getTypes()));
            }
        } catch (IOException | URISyntaxException e) {
            throw new ExtendedRuntimeException(e, "Failed to fetch filter options for segment %s", segment);
        }
    }

    private interface Api {

        @GET
        Call<ResponseBody> getFilterOptions(@Url String url);

    }

}
