package br.com.houseseeker.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ListUtilsTest {

    @Test
    @DisplayName("given a empty list and search index 0 when calls getAtIndex then expects empty")
    void givenEmptyListAndSearchIndexZero_whenCallsGetAtIndex_thenExpectsEmpty() {
        assertThat(ListUtils.getAtIndex(Collections.emptyList(), 0)).isEmpty();
    }

    @Test
    @DisplayName("given a list with one item and search index 0 when calls getAtIndex then expects present")
    void givenAListWithOneItemAndSearchIndexZero_whenCallsGetAtIndex_thenExpectsPresent() {
        assertThat(ListUtils.getAtIndex(List.of(1), 0)).hasValue(1);
    }

    @Test
    @DisplayName("given a list with two items and search index 1 when calls getAtIndex then expects present")
    void givenAListWithTwoItemsAndSearchIndexOne_whenCallsGetAtIndex_thenExpectsPresent() {
        assertThat(ListUtils.getAtIndex(List.of("a", "b"), 1)).hasValue("b");
    }

    @Test
    @DisplayName("given a list with two items and search index 2 when calls getAtIndex then expects empty")
    void givenAListWithTwoItemsAndSearchIndexTwo_whenCallsGetAtIndex_thenExpectsPresent() {
        assertThat(ListUtils.getAtIndex(List.of("a", "b"), 2)).isEmpty();
    }

}