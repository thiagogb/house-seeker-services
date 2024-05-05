package br.com.houseseeker.domain.jetimob.v2;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SearchPageMetadata {

    private final List<Item> items;
    private final Pagination pagination;

    @Data
    @Builder
    public static final class Item {

        private final String pageLink;
        private final String providerCode;

    }

    @Data
    @Builder
    public static final class Pagination {

        private final boolean isLastPage;

    }

}
