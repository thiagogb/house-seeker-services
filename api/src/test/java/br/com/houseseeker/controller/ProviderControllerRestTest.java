package br.com.houseseeker.controller;

import br.com.houseseeker.service.ProviderService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class ProviderControllerRestTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProviderService providerService;

    @Test
    @DisplayName("given a provider without logo when calls getLogo then expects 204")
    void givenAProviderWithoutLogo_whenCallsGetLogo_thenExpects204() throws Exception {
        when(providerService.getLogo(1)).thenReturn(null);

        mockMvc.perform(get("/api/rest/providers/1/logo"))
               .andDo(print())
               .andExpect(status().isNoContent());

        verify(providerService, times(1)).getLogo(1);
        verifyNoMoreInteractions(providerService);
    }

    @Test
    @DisplayName("given a provider with logo when calls getLogo then expects 200")
    void givenAProviderWithLogo_whenCallsGetLogo_thenExpects200() throws Exception {
        var output = "logoContent".getBytes();

        when(providerService.getLogo(1)).thenReturn(output);

        mockMvc.perform(get("/api/rest/providers/1/logo"))
               .andDo(print())
               .andExpect(status().isOk())
               .andExpect(content().bytes(output));

        verify(providerService, times(1)).getLogo(1);
        verifyNoMoreInteractions(providerService);
    }

    @Test
    @DisplayName("given a provider id when calls wipe then expects 200")
    void givenAProviderId_whenCallsWipe_thenExpect200() throws Exception {
        mockMvc.perform(delete("/api/rest/providers/1/wipe"))
               .andDo(print())
               .andExpect(status().isOk())
               .andExpect(content().string(EMPTY));

        verify(providerService, times(1)).wipe(1);
        verifyNoMoreInteractions(providerService);
    }

}