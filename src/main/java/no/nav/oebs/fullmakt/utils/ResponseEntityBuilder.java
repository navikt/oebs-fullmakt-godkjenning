package no.nav.oebs.fullmakt.utils;

import org.springframework.http.ResponseEntity;
import no.nav.oebs.fullmakt.db.entity.ApiError;

public class ResponseEntityBuilder {
    public static ResponseEntity<Object> build(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }
}
