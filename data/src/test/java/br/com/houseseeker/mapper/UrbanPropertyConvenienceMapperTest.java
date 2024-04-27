package br.com.houseseeker.mapper;

import br.com.houseseeker.entity.UrbanProperty;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = UrbanPropertyConvenienceMapperImpl.class)
class UrbanPropertyConvenienceMapperTest {

    private static final String[] EXTRACTED_ATTRIBUTES = new String[]{"id", "urbanProperty", "description"};

    @Autowired
    private UrbanPropertyConvenienceMapper urbanPropertyConvenienceMapper;

    @Test
    @DisplayName("given a null property and description when calls createEntity then expects")
    void givenANullPropertyAndDescription_whenCallsCreateEntity_thenExpects() {
        assertThat(urbanPropertyConvenienceMapper.createEntity(null, null)).isNull();
    }

    @Test
    @DisplayName("given a property and description not null when calls createEntity then expects")
    void givenAPropertyAndDescriptionNotNull_whenCallsCreateEntity_thenExpects() {
        UrbanProperty urbanProperty = UrbanProperty.builder().build();
        String description = "Example convenience";

        assertThat(urbanPropertyConvenienceMapper.createEntity(urbanProperty, description))
                .extracting(EXTRACTED_ATTRIBUTES)
                .containsExactly(null, urbanProperty, "Example convenience");
    }

}