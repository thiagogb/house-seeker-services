package br.com.houseseeker.domain.jetimob.v1;

import br.com.houseseeker.domain.urbanProperty.UrbanPropertyContract;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Builder
@Accessors(chain = true)
public class SearchPageMetadata {

    private UrbanPropertyContract contract;
    private final List<Item> items;
    private final Pagination pagination;

    @Data
    @Builder
    public static final class Item {

        private final String pageLink;
        private final String subType;
        private final String providerCode;

    }

    @Data
    @Builder
    public static final class Pagination {

        private final int currentPage;
        private final int lastPage;

    }

}
