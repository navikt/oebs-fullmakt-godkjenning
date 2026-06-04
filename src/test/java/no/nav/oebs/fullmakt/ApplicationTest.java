package no.nav.oebs.fullmakt;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Enhetstest for loading av applikasjonskonteksten.
 */
@SpringBootTest
@AllArgsConstructor
class ApplicationTest {

	private ApplicationContext applicationContext;

	@Test
	void applicationContextShouldLoad() {
		assertThat(applicationContext).isNotNull();
	}
}