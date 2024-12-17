package no.nav.oebs.fullmakt.service;

import lombok.extern.slf4j.Slf4j;
import no.nav.oebs.fullmakt.Application;
import no.nav.oebs.fullmakt.utils.ObjektMaps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import java.util.Collections;
import java.util.Optional;


@Slf4j
@Service
@Transactional(noRollbackFor = { Exception.class })
public class FullmaktService extends ObjektMaps {
    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    @Value("${url}")
    String url;

    @Value("${request}")
    String request;

    public String hentFullmaktService(String segmentverdi, String segment,
                               Integer minBelopsgrense, String token) //
    {

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        headers.setBearerAuth(token);

        headers.getFirst(HttpHeaders.AUTHORIZATION);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParamIfPresent("segmentverdi", Optional.ofNullable(segmentverdi))
                .queryParamIfPresent("segment", Optional.ofNullable(segment))
                .queryParamIfPresent("minBelopsgrense", Optional.ofNullable(minBelopsgrense));

        ResponseEntity<String> response = restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.GET, entity, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
           // logger.info("200 OK: {}", response.getBody());
            return response.getBody();
        } else {
            // logger.info("{}", response.getStatusCode());
            return response.getStatusCode().toString();
        }
    }
}
