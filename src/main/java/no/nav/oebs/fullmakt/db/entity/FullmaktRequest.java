package no.nav.oebs.fullmakt.db.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

import java.time.LocalDate;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@JsonInclude(Include.NON_NULL)
@JsonPropertyOrder({ "org_id", "segmentverdi", "segment", "belop" })
public class FullmaktRequest {

    private Integer org_id;

    private String segmentverdi;

    private String segment;

    private Integer minBelopsgrense;

}
