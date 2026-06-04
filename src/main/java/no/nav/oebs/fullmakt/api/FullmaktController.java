package no.nav.oebs.fullmakt.api;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import no.nav.oebs.fullmakt.config.FullmaktSwagger;
import no.nav.oebs.fullmakt.config.SwaggerConfig;
import no.nav.oebs.fullmakt.service.FullmaktService;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@Validated
@RequestMapping(path = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
@Tag(name = SwaggerConfig.FULLMAKT, description = "Fullmakt API")
public class FullmaktController {

    private final FullmaktService fullmaktservice;
    public FullmaktController(FullmaktService fullmaktservice) {
        this.fullmaktservice = fullmaktservice;
    }

    @GetMapping(path = "/fullmakt")
    @FullmaktSwagger
    public String hentFullmakter(
            @RequestParam(name = "org_id", defaultValue = "202") Integer orgid,
            @RequestParam(name = "segmentverdi", required = false)
            @Parameter(description = "f.eks. 159101") String segmentverdi,
            @RequestParam(name = "segment", required = false)
            @Parameter(description = "f.eks. Kostnadssted") String segment,
            @RequestParam(name = "minBelopsgrense", required = false)
            @Parameter(description = "f.eks. 25000") Integer minBelopsgrense,
            @RequestHeader(name = "Authorization") String authorizationHeader) {
        return fullmaktservice.hentFullmaktService(segmentverdi, segment, minBelopsgrense, authorizationHeader);
    }
}

