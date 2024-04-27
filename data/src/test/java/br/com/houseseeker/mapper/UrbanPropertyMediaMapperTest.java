package br.com.houseseeker.mapper;

import br.com.houseseeker.domain.property.AbstractUrbanPropertyMediaMetadata;
import br.com.houseseeker.domain.property.UrbanPropertyMediaType;
import br.com.houseseeker.entity.UrbanProperty;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static br.com.houseseeker.mock.UrbanPropertyMetadataMocks.residentialSellingHouse;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = UrbanPropertyMediaMapperImpl.class)
class UrbanPropertyMediaMapperTest {

    private static final String[] EXTRACTED_ATTRIBUTES = new String[]{
            "id",
            "urbanProperty",
            "link",
            "linkThumb",
            "mediaType",
            "extension"
    };

    @Autowired
    private UrbanPropertyMediaMapper urbanPropertyMediaMapper;

    @Test
    @DisplayName("given a property and metadata when calls createEntity then expects")
    void givenAPropertyAndMetadata_whenCallsCreateEntity_thenExpects() {
        UrbanProperty urbanProperty = UrbanProperty.builder().build();
        AbstractUrbanPropertyMediaMetadata metadata = residentialSellingHouse().getMedias().getFirst();

        assertThat(urbanPropertyMediaMapper.createEntity(urbanProperty, metadata))
                .extracting(EXTRACTED_ATTRIBUTES)
                .containsExactly(
                        null,
                        urbanProperty,
                        "http://property/123/img1.jpg",
                        "http://property/123/img1-thumb.jpg",
                        UrbanPropertyMediaType.IMAGE,
                        "jpg"
                );
    }

}