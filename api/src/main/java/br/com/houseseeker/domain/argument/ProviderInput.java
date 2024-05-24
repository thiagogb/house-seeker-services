package br.com.houseseeker.domain.argument;

import br.com.houseseeker.domain.input.BooleanClauseInput;
import br.com.houseseeker.domain.input.BytesClauseInput;
import br.com.houseseeker.domain.input.IntegerClauseInput;
import br.com.houseseeker.domain.input.OrderInput;
import br.com.houseseeker.domain.input.PaginationInput;
import br.com.houseseeker.domain.input.ProviderMechanismClausesInput;
import br.com.houseseeker.domain.input.StringClauseInput;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProviderInput {

    private Clauses clauses;
    private Orders orders;
    private PaginationInput pagination;

    @Data
    @Builder
    public static final class Clauses {

        private IntegerClauseInput id;
        private StringClauseInput name;
        private StringClauseInput siteUrl;
        private StringClauseInput dataUrl;
        private ProviderMechanismClausesInput mechanism;
        private StringClauseInput params;
        private StringClauseInput cronExpression;
        private BytesClauseInput logo;
        private BooleanClauseInput active;

    }

    @Data
    @Builder
    public static final class Orders {

        private OrderInput id;
        private OrderInput name;
        private OrderInput siteUrl;
        private OrderInput dataUrl;
        private OrderInput mechanism;
        private OrderInput active;

    }

}
