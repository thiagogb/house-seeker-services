package br.com.houseseeker.mock;

import br.com.houseseeker.domain.proto.ProviderData;
import com.google.protobuf.BoolValue;
import com.google.protobuf.ByteString;
import com.google.protobuf.BytesValue;
import com.google.protobuf.Int32Value;
import com.google.protobuf.StringValue;
import lombok.experimental.UtilityClass;

import java.nio.charset.StandardCharsets;

@UtilityClass
public class ProviderDataMocks {

    public ProviderData testProviderWithId(Integer id) {
        return ProviderData.newBuilder()
                           .setId(Int32Value.of(id))
                           .setName(StringValue.of(String.format("Test Provider %d", id)))
                           .setSiteUrl(StringValue.of(String.format("http://test.provider.com/%d", id)))
                           .setDataUrl(StringValue.of(String.format("http://test.provider.com/%d/api", id)))
                           .setMechanism(StringValue.of("JETIMOB_V1"))
                           .setParams(StringValue.of("{\"connection\":{}}"))
                           .setCronExpression(StringValue.of("0 0 9 ? * MON,WED,FRI *"))
                           .setLogo(BytesValue.of(ByteString.copyFrom(String.format("logo%d", id), StandardCharsets.UTF_8)))
                           .setActive(BoolValue.of(true))
                           .build();
    }

}
