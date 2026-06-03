package no.nav.oebs.fullmakt.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.HttpClientErrorException;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FullmaktServiceTest {

    static WireMockServer wireMockServer = new WireMockServer(WireMockConfiguration.options().dynamicPort());

    static {
        wireMockServer.start();
    }

    private FullmaktService fullmaktService;

    @BeforeEach
    void setUp() {
        fullmaktService = new FullmaktService();
        ReflectionTestUtils.setField(fullmaktService, "url",
                "http://localhost:" + wireMockServer.port() + "/fullmakt");
    }

    @AfterEach
    void resetWireMock() {
        wireMockServer.resetAll();
    }

    @Test
    void hentFullmaktService_returnererResponseBodyVed200() {
        wireMockServer.stubFor(get(urlPathEqualTo("/fullmakt"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody("[{\"fullmakt\":\"data\"}]")));

        String result = fullmaktService.hentFullmaktService(null, null, null, "test-token");

        assertThat(result).isEqualTo("[{\"fullmakt\":\"data\"}]");
    }

    @Test
    void hentFullmaktService_kasterExceptionVedIkke200() {
        wireMockServer.stubFor(get(urlPathEqualTo("/fullmakt"))
                .willReturn(aResponse()
                        .withStatus(404)));

        assertThatThrownBy(() ->
                fullmaktService.hentFullmaktService(null, null, null, "test-token"))
                .isInstanceOf(HttpClientErrorException.class)
                .hasMessageContaining("404");
    }

    @Test
    void hentFullmaktService_senderMedSegmentverdiSomQueryParam() {
        wireMockServer.stubFor(get(urlPathEqualTo("/fullmakt"))
                .withQueryParam("segmentverdi", equalTo("159101"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody("[]")));

        String result = fullmaktService.hentFullmaktService("159101", null, null, "test-token");

        assertThat(result).isEqualTo("[]");
        wireMockServer.verify(getRequestedFor(urlPathEqualTo("/fullmakt"))
                .withQueryParam("segmentverdi", equalTo("159101")));
    }

    @Test
    void hentFullmaktService_senderMedAlleValgfrieQueryParams() {
        wireMockServer.stubFor(get(urlPathEqualTo("/fullmakt"))
                .withQueryParam("segmentverdi", equalTo("159101"))
                .withQueryParam("segment", equalTo("Kostnadssted"))
                .withQueryParam("minBelopsgrense", equalTo("25000"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody("[{\"result\":\"ok\"}]")));

        String result = fullmaktService.hentFullmaktService("159101", "Kostnadssted", 25000, "test-token");

        assertThat(result).isEqualTo("[{\"result\":\"ok\"}]");
    }

    @Test
    void hentFullmaktService_senderAuthorizationHeader() {
        wireMockServer.stubFor(get(urlPathEqualTo("/fullmakt"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody("[]")));

        fullmaktService.hentFullmaktService(null, null, null, "mitt-token");

        wireMockServer.verify(getRequestedFor(urlPathEqualTo("/fullmakt"))
                .withHeader("Authorization", equalTo("Bearer mitt-token")));
    }
}
