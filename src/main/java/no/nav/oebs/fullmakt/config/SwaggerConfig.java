package no.nav.oebs.fullmakt.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class SwaggerConfig {

	public static final String FULLMAKT = "Fullmakt API";

	@Value("${oebs.environment}")
	String env;

	@Value("${oebs.date}")
	String dato;

	@Value("${oebs.version}")
	String versjon;

@Bean
	public OpenAPI apiInfo() {
		return new OpenAPI()
				.info(new Info()
						.title(env + " - (NAIS)")
						.description("""
								<p>REST API'er som er tilbudt av Oebs.</p>
								<p>Sikkerhet:</p>
								<ul>
								<li>API'et støtter aksesstoken utstedt av Azure AD</li>""")
						.version(versjon + " " + "("+dato+")"));
	}
}
