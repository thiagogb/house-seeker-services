package br.com.houseseeker.mapper;

import br.com.houseseeker.domain.proto.ProviderData;
import br.com.houseseeker.domain.provider.ProviderMechanism;
import br.com.houseseeker.entity.Provider;
import com.google.protobuf.BoolValue;
import com.google.protobuf.ByteString;
import com.google.protobuf.BytesValue;
import com.google.protobuf.Int32Value;
import com.google.protobuf.StringValue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;

@SpringBootTest(classes = ProviderMapperImpl.class)
class ProviderMapperTest {

    private static final String[] EXTRACTED_ATTRIBUTES = new String[]{
            "id", "name", "siteUrl", "dataUrl", "mechanism", "params", "cronExpression", "logo", "active"
    };

    private static final Provider TEST_PROVIDER_ENTITY_1 = Provider.builder()
                                                                   .id(1)
                                                                   .name("Test Provider 1")
                                                                   .siteUrl("http://test.provider.com/1")
                                                                   .dataUrl("http://test.provider.com/1/api")
                                                                   .mechanism(ProviderMechanism.JETIMOB_V1)
                                                                   .params("{\"connection\":{}}")
                                                                   .cronExpression("0 0 9 ? * MON,WED,FRI *")
                                                                   .logo("logo1".getBytes(StandardCharsets.UTF_8))
                                                                   .active(true)
                                                                   .build();

    private static final Provider TEST_PROVIDER_ENTITY_2 = Provider.builder()
                                                                   .id(2)
                                                                   .name("Test Provider 2")
                                                                   .siteUrl("http://test.provider.com/2")
                                                                   .dataUrl("http://test.provider.com/2/api")
                                                                   .mechanism(ProviderMechanism.JETIMOB_V2)
                                                                   .params("{\"connection\":{}}")
                                                                   .cronExpression("0 0 9 ? * MON,WED,FRI *")
                                                                   .logo("logo2".getBytes(StandardCharsets.UTF_8))
                                                                   .active(false)
                                                                   .build();

    private static final ProviderData TEST_PROVIDER_DATA = ProviderData.newBuilder()
                                                                       .setId(Int32Value.of(1))
                                                                       .setName(StringValue.of("Test Provider 1"))
                                                                       .setSiteUrl(StringValue.of("http://test.provider.com/1"))
                                                                       .setDataUrl(StringValue.of("http://test.provider.com/1/api"))
                                                                       .setMechanism(StringValue.of("JETIMOB_V1"))
                                                                       .setParams(StringValue.of("{\"connection\":{}}"))
                                                                       .setCronExpression(StringValue.of("0 0 9 ? * MON,WED,FRI *"))
                                                                       .setLogo(BytesValue.of(ByteString.copyFrom("logo1", StandardCharsets.UTF_8)))
                                                                       .setActive(BoolValue.of(true))
                                                                       .build();

    @Autowired
    private ProviderMapper providerMapper;

    @Test
    @DisplayName("given a provider list when calls toProto then expects")
    void givenAProviderList_whenCallsToProto_thenExpects() {
        assertThat(providerMapper.toProto(List.of(TEST_PROVIDER_ENTITY_1, TEST_PROVIDER_ENTITY_2)))
                .extracting(EXTRACTED_ATTRIBUTES)
                .containsExactly(
                        tuple(toProtoWrappers(TEST_PROVIDER_ENTITY_1)),
                        tuple(toProtoWrappers(TEST_PROVIDER_ENTITY_2))
                );
    }

    @Test
    @DisplayName("given a provider when calls toProto then expects")
    void givenAProvider_whenCallsToProto_thenExpects() {
        assertThat(providerMapper.toProto(TEST_PROVIDER_ENTITY_1))
                .extracting(EXTRACTED_ATTRIBUTES)
                .containsExactly(toProtoWrappers(TEST_PROVIDER_ENTITY_1));
    }

    @Test
    @DisplayName("given a empty provider when calls toProto then expects")
    void givenAEmptyProvider_whenCallsToProto_thenExpects() {
        Provider provider = Provider.builder().build();

        assertThat(providerMapper.toProto(provider))
                .extracting(EXTRACTED_ATTRIBUTES)
                .containsExactly(
                        Int32Value.getDefaultInstance(),
                        StringValue.getDefaultInstance(),
                        StringValue.getDefaultInstance(),
                        StringValue.getDefaultInstance(),
                        StringValue.getDefaultInstance(),
                        StringValue.getDefaultInstance(),
                        StringValue.getDefaultInstance(),
                        BytesValue.getDefaultInstance(),
                        BoolValue.getDefaultInstance()
                );
    }

    @Test
    @DisplayName("given a proto when calls createEntity then expects")
    void givenAProto_whenCallsCreateEntity_thenExpects() {
        assertThat(providerMapper.createEntity(TEST_PROVIDER_DATA))
                .extracting(EXTRACTED_ATTRIBUTES)
                .containsExactly(
                        null,
                        TEST_PROVIDER_DATA.getName().getValue(),
                        TEST_PROVIDER_DATA.getSiteUrl().getValue(),
                        TEST_PROVIDER_DATA.getDataUrl().getValue(),
                        ProviderMechanism.valueOf(TEST_PROVIDER_DATA.getMechanism().getValue()),
                        TEST_PROVIDER_DATA.getParams().getValue(),
                        TEST_PROVIDER_DATA.getCronExpression().getValue(),
                        TEST_PROVIDER_DATA.getLogo().getValue().toByteArray(),
                        TEST_PROVIDER_DATA.getActive().getValue()
                );
    }

    @Test
    @DisplayName("given a empty proto when calls createEntity then expects")
    void givenAEmptyProto_whenCallsCreateEntity_thenExpects() {
        assertThat(providerMapper.createEntity(ProviderData.getDefaultInstance())).hasAllNullFieldsOrProperties();
    }

    @Test
    @DisplayName("given a proto and provider when calls copyToEntity then expects")
    void givenAProtoAndProvider_whenCallsCopyToEntity_thenExpects() {
        Provider provider = Provider.builder().id(1).build();

        providerMapper.copyToEntity(TEST_PROVIDER_DATA, provider);

        assertThat(provider)
                .extracting(EXTRACTED_ATTRIBUTES)
                .containsExactly(
                        1,
                        TEST_PROVIDER_DATA.getName().getValue(),
                        TEST_PROVIDER_DATA.getSiteUrl().getValue(),
                        TEST_PROVIDER_DATA.getDataUrl().getValue(),
                        ProviderMechanism.valueOf(TEST_PROVIDER_DATA.getMechanism().getValue()),
                        TEST_PROVIDER_DATA.getParams().getValue(),
                        TEST_PROVIDER_DATA.getCronExpression().getValue(),
                        TEST_PROVIDER_DATA.getLogo().getValue().toByteArray(),
                        TEST_PROVIDER_DATA.getActive().getValue()
                );
    }

    @Test
    @DisplayName("given a empty proto and provider when calls copyToEntity then expects")
    void givenAEmptyProtoAndProvider_whenCallsCopyToEntity_thenExpects() {
        Provider provider = TEST_PROVIDER_ENTITY_1.toBuilder().build();

        providerMapper.copyToEntity(ProviderData.getDefaultInstance(), provider);

        assertThat(provider).hasAllNullFieldsOrPropertiesExcept("id");
    }

    private Object[] toProtoWrappers(Provider provider) {
        return new Object[]{
                Int32Value.of(provider.getId()),
                StringValue.of(provider.getName()),
                StringValue.of(provider.getSiteUrl()),
                StringValue.of(provider.getDataUrl()),
                StringValue.of(provider.getMechanism().name()),
                StringValue.of(provider.getParams()),
                StringValue.of(provider.getCronExpression()),
                BytesValue.of(ByteString.copyFrom(provider.getLogo())),
                BoolValue.of(provider.getActive())
        };
    }

}