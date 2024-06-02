package br.com.houseseeker.domain.input;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public abstract class AbstractEnumClauseInput<T extends Enum<T>, U extends AbstractInput<T>, V extends AbstractListInput<T>> {

    private Boolean isNull;
    private Boolean isNotNull;
    private U isEqual;
    private U isNotEqual;
    private V isIn;
    private V isNotIn;

}
