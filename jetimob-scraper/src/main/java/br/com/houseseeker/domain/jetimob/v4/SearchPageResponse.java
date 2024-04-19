package br.com.houseseeker.domain.jetimob.v4;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class SearchPageResponse {

    @JsonProperty("totalItems")
    private Integer totalItems;

    @JsonProperty("items")
    private List<Item> items;

    @Data
    public static final class Item {

        @JsonProperty("code")
        private String code;

        @JsonProperty("url")
        private String url;

    }
}
