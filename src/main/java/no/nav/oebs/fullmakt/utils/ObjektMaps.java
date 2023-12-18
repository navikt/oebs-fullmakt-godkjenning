package no.nav.oebs.fullmakt.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import no.nav.oebs.fullmakt.exception.JsonMappingException;
import no.nav.oebs.fullmakt.exception.TechnicalPlsqlException;
import no.nav.oebs.fullmakt.exception.UgyldigInputException;

/**
 * Superklasse med felles funksjonalitet for implementasjon av tjenestespesifikke Service-klasser.
 */
public class ObjektMaps {
    private ObjectMapper objectMapper;

    public ObjektMaps() {
    }

    public ObjektMaps(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * Mapper fra Java- til JSON-objekt.
     */
    public <T> String toJson(T object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new JsonMappingException(e);
        }
    }

    /**
     * Mapper fra JSON- til Java-objekt.
     */
    public <T> T toObject(String json, Class<T> valueType) {
        try {
            return objectMapper.readValue(json, valueType);
        } catch (JsonProcessingException e) {
            throw new JsonMappingException(e);
        }
    }

    /**
     * Mapper fra JSON- til Java-objekt der generisk typeinformasjon må brukes under mappingen. Dette gjelder typisk for List-
     * og Map-objekter.
     */
    public <T> T toObject(String json, TypeReference<T> objectTypeRef) {
        try {
            return objectMapper.readValue(json, objectTypeRef);
        } catch (JsonProcessingException e) {
            throw new JsonMappingException(e);
        }
    }
}
