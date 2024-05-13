package br.com.houseseeker.configuration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.converter.protobuf.ProtobufHttpMessageConverter;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = ProtoBufferConfiguration.class)
class ProtoBufferConfigurationTest {

    @Autowired
    private ProtobufHttpMessageConverter protobufHttpMessageConverter;

    @Test
    @DisplayName("expected to create proto buffer http message converter bean")
    void testProtobufHttpMessageConverterBean() {
        assertThat(protobufHttpMessageConverter).isNotNull();
    }

}