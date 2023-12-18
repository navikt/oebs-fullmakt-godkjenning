package no.nav.oebs.fullmakt.db.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
@Table(name = "XXRTV_FULLMAKT_TABLE")
public class FullmaktEntity {

    @Id
    @SequenceGenerator(name = "XXRTV_FULLMAKT_S", sequenceName = "XXRTV_FULLMAKT_S", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "XXRTV_FULLMAKT_S")
    @Column(name = "ID")
    private Long fullmakt_id;

    @Column(name = "KORRELASJON_ID")
    private String korrelasjon_id;

    @Column(name = "CREATION_DATE")
    private LocalDateTime creation_date;

    @Column(name = "JSON_MELDING")
    private String json_melding;

}
