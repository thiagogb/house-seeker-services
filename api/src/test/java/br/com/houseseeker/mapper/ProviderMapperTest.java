package br.com.houseseeker.mapper;

import br.com.houseseeker.domain.input.ProviderCreationInput;
import br.com.houseseeker.domain.input.ProviderEditionInput;
import br.com.houseseeker.domain.proto.ProviderData;
import br.com.houseseeker.domain.provider.ProviderMechanism;
import com.google.protobuf.BoolValue;
import com.google.protobuf.ByteString;
import com.google.protobuf.BytesValue;
import com.google.protobuf.Int32Value;
import com.google.protobuf.StringValue;
import graphql.schema.DataFetchingEnvironment;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = ProviderMapperImpl.class)
@ExtendWith(MockitoExtension.class)
class ProviderMapperTest {

    private static final ProviderData DEFAULT_PROVIDER_DATA = ProviderData.newBuilder()
                                                                          .setId(Int32Value.of(1))
                                                                          .setName(StringValue.of("Test provider"))
                                                                          .setSiteUrl(StringValue.of("http://localhost"))
                                                                          .setDataUrl(StringValue.of("http://localhost/api"))
                                                                          .setMechanism(StringValue.of(ProviderMechanism.ALAN_WGT.name()))
                                                                          .setParams(StringValue.of("{}"))
                                                                          .setCronExpression(StringValue.of("* * *"))
                                                                          .setLogo(BytesValue.of(ByteString.copyFrom("content", StandardCharsets.UTF_8)))
                                                                          .setActive(BoolValue.of(true))
                                                                          .build();
    private static final ProviderCreationInput DEFAULT_PROVIDER_CREATION_INPUT = ProviderCreationInput.builder()
                                                                                                      .name("Test provider")
                                                                                                      .siteUrl("http://localhost")
                                                                                                      .dataUrl("http://localhost/api")
                                                                                                      .mechanism(ProviderMechanism.JETIMOB_V1)
                                                                                                      .params("{}")
                                                                                                      .cronExpression("* * *")
                                                                                                      .logo("Y29udGVudA==")
                                                                                                      .active(true)
                                                                                                      .build();
    private static final String[] DTO_EXTRACTED_ATTRIBUTES = new String[]{
            "id", "name", "siteUrl", "dataUrl", "mechanism", "params", "cronExpression", "logoUrl", "active"
    };

    private static final String[] DATA_EXTRACTED_ATTRIBUTES = new String[]{
            "id", "name", "siteUrl", "dataUrl", "mechanism", "params", "cronExpression", "logo", "active"
    };

    @Autowired
    private ProviderMapper providerMapper;

    @Mock
    private DataFetchingEnvironment mockedDataFetchingEnvironment;

    @Test
    @DisplayName("given a data list when calls toDto then expects")
    void givenADataList_whenCallsToDto_thenExpects() {
        assertThat(providerMapper.toDto(List.of(DEFAULT_PROVIDER_DATA)))
                .extracting(DTO_EXTRACTED_ATTRIBUTES)
                .containsExactly(
                        tuple(
                                1,
                                "Test provider",
                                "http://localhost",
                                "http://localhost/api",
                                ProviderMechanism.ALAN_WGT,
                                "{}",
                                "* * *",
                                "http://localhost/api/rest/providers/1/logo",
                                true
                        )
                );
    }

    @Test
    @DisplayName("given a single data when calls toDto then expects")
    void givenASingleData_whenCallsToDto_thenExpects() {
        assertThat(providerMapper.toDto(DEFAULT_PROVIDER_DATA))
                .extracting(DTO_EXTRACTED_ATTRIBUTES)
                .containsExactly(
                        1,
                        "Test provider",
                        "http://localhost",
                        "http://localhost/api",
                        ProviderMechanism.ALAN_WGT,
                        "{}",
                        "* * *",
                        "http://localhost/api/rest/providers/1/logo",
                        true
                );
    }

    @Test
    @DisplayName("given a input when calls toData then expects")
    void givenAInput_whenCallsToData_thenExpects() {
        assertThat(providerMapper.toData(DEFAULT_PROVIDER_CREATION_INPUT))
                .extracting(DATA_EXTRACTED_ATTRIBUTES)
                .containsExactly(
                        Int32Value.getDefaultInstance(),
                        StringValue.of("Test provider"),
                        StringValue.of("http://localhost"),
                        StringValue.of("http://localhost/api"),
                        StringValue.of(ProviderMechanism.JETIMOB_V1.name()),
                        StringValue.of("{}"),
                        StringValue.of("* * *"),
                        BytesValue.of(ByteString.copyFrom("content", StandardCharsets.UTF_8)),
                        BoolValue.of(true)
                );
    }

    @Test
    @DisplayName("given a input with all attributes filled and a existing data when calls copyToData then expects")
    void givenAInputWithAllAttributesFilledAndExistingData_whenCallsCopyToData_thenExpects() {
        var input = ProviderEditionInput.builder()
                                        .name("Test provider edited")
                                        .siteUrl("http://127.0.0.1")
                                        .dataUrl("http://127.0.0.1/api")
                                        .mechanism(ProviderMechanism.JETIMOB_V2)
                                        .params("{\"connection\":{}}")
                                        .cronExpression("0 0 0")
                                        .logo("Y29udGVudCBlZGl0ZWQ=")
                                        .active(false)
                                        .build();
        var data = DEFAULT_PROVIDER_DATA.toBuilder();

        when(mockedDataFetchingEnvironment.getArgument("input")).thenReturn(
                Map.of(
                        "name", true,
                        "siteUrl", true,
                        "dataUrl", true,
                        "mechanism", true,
                        "params", true,
                        "cronExpression", true,
                        "logo", true,
                        "active", true
                )
        );

        input.detectedChangedArguments(mockedDataFetchingEnvironment);

        providerMapper.copyToData(input, data);

        assertThat(data.build())
                .extracting(DATA_EXTRACTED_ATTRIBUTES)
                .containsExactly(
                        Int32Value.of(1),
                        StringValue.of("Test provider edited"),
                        StringValue.of("http://127.0.0.1"),
                        StringValue.of("http://127.0.0.1/api"),
                        StringValue.of(ProviderMechanism.JETIMOB_V2.name()),
                        StringValue.of("{\"connection\":{}}"),
                        StringValue.of("0 0 0"),
                        BytesValue.of(ByteString.copyFrom("content edited", StandardCharsets.UTF_8)),
                        BoolValue.of(false)
                );
    }

    @Test
    @DisplayName("given a input with partial attributes filled and a existing data when calls copyToData then expects")
    void givenAInputWithPartialAttributesFilledAndExistingData_whenCallsCopyToData_thenExpects() {
        var input = ProviderEditionInput.builder()
                                        .name("Test provider edited")
                                        .siteUrl("http://127.0.0.1")
                                        .active(false)
                                        .build();
        var data = DEFAULT_PROVIDER_DATA.toBuilder();

        when(mockedDataFetchingEnvironment.getArgument("input")).thenReturn(
                Map.of(
                        "name", true,
                        "siteUrl", true,
                        "active", true
                )
        );

        input.detectedChangedArguments(mockedDataFetchingEnvironment);

        providerMapper.copyToData(input, data);

        assertThat(data.build())
                .extracting(DATA_EXTRACTED_ATTRIBUTES)
                .containsExactly(
                        Int32Value.of(1),
                        StringValue.of("Test provider edited"),
                        StringValue.of("http://127.0.0.1"),
                        StringValue.of("http://localhost/api"),
                        StringValue.of(ProviderMechanism.ALAN_WGT.name()),
                        StringValue.of("{}"),
                        StringValue.of("* * *"),
                        BytesValue.of(ByteString.copyFrom("content", StandardCharsets.UTF_8)),
                        BoolValue.of(false)
                );
    }

    @Test
    @DisplayName("given a input with partial attributes filled with nulls and a existing data when calls copyToData then expects")
    void givenAInputWithPartialAttributesFilledWithNullsAndExistingData_whenCallsCopyToData_thenExpects() {
        var input = ProviderEditionInput.builder().build();
        var data = DEFAULT_PROVIDER_DATA.toBuilder();

        when(mockedDataFetchingEnvironment.getArgument("input")).thenReturn(
                Map.of(
                        "dataUrl", true,
                        "params", true,
                        "logo", true
                )
        );

        input.detectedChangedArguments(mockedDataFetchingEnvironment);

        providerMapper.copyToData(input, data);

        assertThat(data.build())
                .extracting(DATA_EXTRACTED_ATTRIBUTES)
                .containsExactly(
                        Int32Value.of(1),
                        StringValue.of("Test provider"),
                        StringValue.of("http://localhost"),
                        StringValue.getDefaultInstance(),
                        StringValue.of(ProviderMechanism.ALAN_WGT.name()),
                        StringValue.getDefaultInstance(),
                        StringValue.of("* * *"),
                        BytesValue.getDefaultInstance(),
                        BoolValue.of(true)
                );
    }

}