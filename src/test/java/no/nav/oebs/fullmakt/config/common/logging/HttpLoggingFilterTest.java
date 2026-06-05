package no.nav.oebs.fullmakt.config.common.logging;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import no.nav.oebs.fullmakt.config.common.mdc.MdcOperations;
import no.nav.oebs.fullmakt.db.entity.KallLogg;
import no.nav.oebs.fullmakt.db.repository.KallLoggRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HttpLoggingFilterTest {

    @Mock
    private KallLoggRepository kallLoggRepository;

    @Mock
    private FilterChain filterChain;

    private HttpLoggingFilter httpLoggingFilter;

    @BeforeEach
    void setUp() {
        httpLoggingFilter = new HttpLoggingFilter(kallLoggRepository);
    }

    @Test
    void doFilterInternal_shouldCallFilterChain() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/validerkontostreng");
        MockHttpServletResponse response = new MockHttpServletResponse();

        httpLoggingFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(any(), any());
    }

    @Test
    void doFilterInternal_shouldSaveKallLogg() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/validerkontostreng");
        MockHttpServletResponse response = new MockHttpServletResponse();
        response.setStatus(200);

        httpLoggingFilter.doFilterInternal(request, response, filterChain);

        ArgumentCaptor<KallLogg> captor = ArgumentCaptor.forClass(KallLogg.class);
        verify(kallLoggRepository, times(1)).save(captor.capture());

        KallLogg savedLogg = captor.getValue();
        assertEquals("GET", savedLogg.getMethod());
        assertEquals("/validerkontostreng", savedLogg.getOperation());
        assertEquals(200, savedLogg.getStatus());
        assertEquals(KallLogg.TYPE_REST, savedLogg.getType());
        assertEquals(KallLogg.RETNING_INN, savedLogg.getKallRetning());
    }

    @Test
    void doFilterInternal_shouldSaveKallLoggEvenWhenFilterChainThrows() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/validerkontostreng");
        MockHttpServletResponse response = new MockHttpServletResponse();

        doThrow(new RuntimeException("noe gikk galt")).when(filterChain).doFilter(any(), any());

        assertThrows(RuntimeException.class, () ->
                httpLoggingFilter.doFilterInternal(request, response, filterChain));

        verify(kallLoggRepository, times(1)).save(any());
    }

    @Test
    void doFilterInternal_shouldNotThrowWhenSaveKallLoggFails() {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/validerkontostreng");
        MockHttpServletResponse response = new MockHttpServletResponse();

        when(kallLoggRepository.save(any())).thenThrow(new RuntimeException("DB utilgjengelig"));

        assertDoesNotThrow(() ->
                httpLoggingFilter.doFilterInternal(request, response, filterChain));
    }

    @Test
    void doFilterInternal_shouldIncludeQueryStringInRequest() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/v1/fullmakt");
        request.setQueryString("segmentverdi=159101&segment=Kostnadssted");
        MockHttpServletResponse response = new MockHttpServletResponse();

        httpLoggingFilter.doFilterInternal(request, response, filterChain);

        ArgumentCaptor<KallLogg> captor = ArgumentCaptor.forClass(KallLogg.class);
        verify(kallLoggRepository).save(captor.capture());

        assertTrue(captor.getValue().getRequest().contains("segmentverdi=159101&segment=Kostnadssted"));
    }

    @Test
    void doFilterInternal_shouldIncludeRequestHeadersInRequest() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/v1/fullmakt");
        request.addHeader("Authorization", "Bearer test-token");
        request.addHeader("Content-Type", "application/json");
        MockHttpServletResponse response = new MockHttpServletResponse();

        httpLoggingFilter.doFilterInternal(request, response, filterChain);

        ArgumentCaptor<KallLogg> captor = ArgumentCaptor.forClass(KallLogg.class);
        verify(kallLoggRepository).save(captor.capture());

        String loggedRequest = captor.getValue().getRequest();
        assertTrue(loggedRequest.contains("Authorization"));
        assertTrue(loggedRequest.contains("Bearer test-token"));
    }

    @Test
    void doFilterInternal_shouldIncludeRequestBodyInRequest() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/api/v1/fullmakt");
        request.setContentType("application/json");
        byte[] body = "{\"key\":\"value\"}".getBytes(StandardCharsets.UTF_8);
        request.setContent(body);
        MockHttpServletResponse response = new MockHttpServletResponse();

        // ContentCachingRequestWrapper cacher kun body etter at den er lest - simuler at filterChain leser den
        doAnswer(invocation -> {
            HttpServletRequest req = invocation.getArgument(0);
            req.getInputStream().readAllBytes();
            return null;
        }).when(filterChain).doFilter(any(), any());

        httpLoggingFilter.doFilterInternal(request, response, filterChain);

        ArgumentCaptor<KallLogg> captor = ArgumentCaptor.forClass(KallLogg.class);
        verify(kallLoggRepository).save(captor.capture());

        assertTrue(captor.getValue().getRequest().contains("{\"key\":\"value\"}"));
    }

    @Test
    void doFilterInternal_shouldIncludeResponseStatusInResponse() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/v1/fullmakt");
        MockHttpServletResponse response = new MockHttpServletResponse();
        response.setStatus(201);

        httpLoggingFilter.doFilterInternal(request, response, filterChain);

        ArgumentCaptor<KallLogg> captor = ArgumentCaptor.forClass(KallLogg.class);
        verify(kallLoggRepository).save(captor.capture());

        assertEquals(201, captor.getValue().getStatus());
        assertTrue(captor.getValue().getResponse().contains("201"));
    }

    @Test
    void doFilterInternal_shouldIncludeResponseHeadersInResponse() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/v1/fullmakt");
        MockHttpServletResponse response = new MockHttpServletResponse();
        response.addHeader("Content-Type", "application/json");

        httpLoggingFilter.doFilterInternal(request, response, filterChain);

        ArgumentCaptor<KallLogg> captor = ArgumentCaptor.forClass(KallLogg.class);
        verify(kallLoggRepository).save(captor.capture());

        assertTrue(captor.getValue().getResponse().contains("Content-Type"));
    }

    @Test
    void doFilterInternal_shouldSetTidspunktOgKalltid() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/v1/fullmakt");
        MockHttpServletResponse response = new MockHttpServletResponse();

        httpLoggingFilter.doFilterInternal(request, response, filterChain);

        ArgumentCaptor<KallLogg> captor = ArgumentCaptor.forClass(KallLogg.class);
        verify(kallLoggRepository).save(captor.capture());

        KallLogg savedLogg = captor.getValue();
        assertNotNull(savedLogg.getTidspunkt());
        assertTrue(savedLogg.getKalltid() >= 0);
    }

    @Test
    void doFilterInternal_shouldSetKorrelasjonIdFraMdc() throws ServletException, IOException {
        MdcOperations.put(MdcOperations.MDC_CORRELATION_ID, "test-korrelasjon-id");

        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/v1/fullmakt");
        MockHttpServletResponse response = new MockHttpServletResponse();

        try {
            httpLoggingFilter.doFilterInternal(request, response, filterChain);

            ArgumentCaptor<KallLogg> captor = ArgumentCaptor.forClass(KallLogg.class);
            verify(kallLoggRepository).save(captor.capture());

            assertEquals("test-korrelasjon-id", captor.getValue().getKorrelasjonId());
        } finally {
            MdcOperations.remove(MdcOperations.MDC_CORRELATION_ID);
        }
    }

    @Test
    void doFilterInternal_shouldHandleAlreadyWrappedRequest() throws ServletException, IOException {
        MockHttpServletRequest mockRequest = new MockHttpServletRequest("GET", "/api/v1/fullmakt");
        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(mockRequest, -1);
        MockHttpServletResponse response = new MockHttpServletResponse();

        httpLoggingFilter.doFilterInternal(wrappedRequest, response, filterChain);

        verify(filterChain, times(1)).doFilter(eq(wrappedRequest), any());
        verify(kallLoggRepository, times(1)).save(any());
    }

    @Test
    void doFilterInternal_shouldHandleAlreadyWrappedResponse() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/v1/fullmakt");
        MockHttpServletResponse mockResponse = new MockHttpServletResponse();
        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(mockResponse);

        httpLoggingFilter.doFilterInternal(request, wrappedResponse, filterChain);

        verify(filterChain, times(1)).doFilter(any(), eq(wrappedResponse));
        verify(kallLoggRepository, times(1)).save(any());
    }
}

