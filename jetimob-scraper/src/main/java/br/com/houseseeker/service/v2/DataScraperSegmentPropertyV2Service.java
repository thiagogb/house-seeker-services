package br.com.houseseeker.service.v2;

import br.com.houseseeker.domain.exception.ExtendedRuntimeException;
import br.com.houseseeker.domain.jetimob.v2.PropertyInfoMetadata;
import br.com.houseseeker.domain.jetimob.v2.SearchPageMetadata;
import br.com.houseseeker.domain.property.UrbanPropertyContract;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.ResponseBody;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Url;

import java.io.IOException;
import java.util.List;

import static br.com.houseseeker.util.RetrofitUtils.executeCall;

@Service
@RequiredArgsConstructor
@Slf4j
public class DataScraperSegmentPropertyV2Service {

    private final PropertyPageV2ScraperService propertyPageV2ScraperService;

    public List<PropertyInfoMetadata> fetch(
            @NotNull Retrofit retrofit,
            @NotNull UrbanPropertyContract contract,
            @NotNull List<SearchPageMetadata.Item> searchItems
    ) {
        Api api = retrofit.create(Api.class);
        return searchItems.parallelStream()
                          .map(searchItem -> fetchPropertiesData(api, contract, searchItem))
                          .toList();
    }

    private PropertyInfoMetadata fetchPropertiesData(
            Api api,
            UrbanPropertyContract contract,
            SearchPageMetadata.Item searchResultItem
    ) {
        try {
            try (var response = executeCall(api.getPropertyData(searchResultItem.getPageLink()))) {
                return propertyPageV2ScraperService.scrap(response.string())
                                                   .setProviderCode(searchResultItem.getProviderCode())
                                                   .setUrl(searchResultItem.getPageLink())
                                                   .setContract(contract)
                                                   .setSubType(searchResultItem.getSubType());
            }
        } catch (IOException e) {
            throw new ExtendedRuntimeException(e, "Failed to fetch property %s", searchResultItem.getProviderCode());
        }
    }

    private interface Api {

        @GET
        Call<ResponseBody> getPropertyData(@Url String url);

    }

}
