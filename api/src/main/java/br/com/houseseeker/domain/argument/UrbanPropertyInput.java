package br.com.houseseeker.domain.argument;

import br.com.houseseeker.domain.input.BooleanClauseInput;
import br.com.houseseeker.domain.input.DateTimeClauseInput;
import br.com.houseseeker.domain.input.FloatClauseInput;
import br.com.houseseeker.domain.input.IntegerClauseInput;
import br.com.houseseeker.domain.input.OrderInput;
import br.com.houseseeker.domain.input.PaginationInput;
import br.com.houseseeker.domain.input.StringClauseInput;
import br.com.houseseeker.domain.input.UrbanPropertyContractClauseInput;
import br.com.houseseeker.domain.input.UrbanPropertyStatusClauseInput;
import br.com.houseseeker.domain.input.UrbanPropertyTypeClauseInput;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UrbanPropertyInput {

    private Clauses clauses;
    private Orders orders;
    private PaginationInput pagination;

    @Data
    @Builder
    public static final class Clauses {

        private IntegerClauseInput id;
        private IntegerClauseInput providerId;
        private StringClauseInput providerCode;
        private StringClauseInput url;
        private UrbanPropertyContractClauseInput contract;
        private UrbanPropertyTypeClauseInput type;
        private StringClauseInput subType;
        private IntegerClauseInput dormitories;
        private IntegerClauseInput suites;
        private IntegerClauseInput bathrooms;
        private IntegerClauseInput garages;
        private FloatClauseInput sellPrice;
        private FloatClauseInput rentPrice;
        private FloatClauseInput condominiumPrice;
        private StringClauseInput condominiumName;
        private BooleanClauseInput exchangeable;
        private UrbanPropertyStatusClauseInput status;
        private BooleanClauseInput financeable;
        private BooleanClauseInput occupied;
        private StringClauseInput notes;
        private DateTimeClauseInput creationDate;
        private DateTimeClauseInput lastAnalysisDate;
        private DateTimeClauseInput exclusionDate;
        private BooleanClauseInput analyzable;

    }

    @Data
    @Builder
    public static final class Orders {

        private OrderInput id;
        private OrderInput providerId;
        private OrderInput providerCode;
        private OrderInput url;
        private OrderInput contract;
        private OrderInput type;
        private OrderInput subType;
        private OrderInput dormitories;
        private OrderInput suites;
        private OrderInput bathrooms;
        private OrderInput garages;
        private OrderInput sellPrice;
        private OrderInput rentPrice;
        private OrderInput condominiumPrice;
        private OrderInput condominiumName;
        private OrderInput exchangeable;
        private OrderInput status;
        private OrderInput financeable;
        private OrderInput occupied;
        private OrderInput notes;
        private OrderInput creationDate;
        private OrderInput lastAnalysisDate;
        private OrderInput exclusionDate;
        private OrderInput analyzable;

    }

}
