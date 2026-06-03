# oebs-fullmakt-godkjenning-api

REST API service that integrates OEBS (Oracle E-Business Suite) with the fullmakt registry. The service exposes a REST endpoint that proxies requests to the fullmakt registry, enabling querying of fullmakter (authorization delegations) based on segment, segmentverdi, and minimum amount.

---

## Architecture

The service acts as a middleware between consumers and the external fullmakt registry. Incoming requests are authenticated via Azure AD, forwarded to the fullmakt registry with the caller's bearer token, and the response is returned as-is. All requests and responses are logged to an Oracle database table via `HttpLoggingFilter`.

---

## Functionality

### Endpoints

| Method | Path | Description |
|--------|------|-------------|
| `GET` | `/api/v1/fullmakt` | Retrieves fullmakter from the fullmakt registry |

**Query parameters for `GET /api/v1/fullmakt`:**

| Parameter | Required | Description |
|-----------|----------|-------------|
| `org_id` | No (default: `202`) | Organisation ID |
| `segmentverdi` | No | e.g. `159101` |
| `segment` | No | e.g. `Kostnadssted` |
| `minBelopsgrense` | No | e.g. `25000` |

**Headers:**
- `Authorization` – Required. Bearer token from Azure AD.

### Instances and OEBS environments

The service currently runs with three instances: t1, q1, and prod.

### OEBS PL/SQL procedures

#todo: Oppdatere med beskrivelse av integrasjon med OeBS og lenker til install-script, package specification og package body i OEBS-repositoryet.

---

## Dependencies

| System | Purpose |
|--------|---------|
| **Fullmakt registry** | External system providing fullmakt data; called via REST |
| **OEBS Oracle Database** | Stores request/response logs via `KallLogg` |
| **NAV Token Validation** | OAuth2/JWT security via Azure AD |
| **NAIS platform** | Container orchestration, secrets management, and deployment |

---

## Running Locally

To run the service locally, use the `local` profile and set the following environment variables. Values for all secrets can be retrieved from the NAIS console for the application `oebs-fullmakt-godkjenning-api-t1`:

- `OEBS_USERNAME` – username for OEBS
- `OEBS_PASSWORD` – password for OEBS
- `OEBS_URL` – JDBC URL for the OEBS Oracle database
- `FULLMAKTSREGISTER_URL` – URL for the fullmakt registry
- `AZURE_APP_CLIENT_ID` – client ID for the Azure AD app
- `AZURE_APP_WELL_KNOWN_URL` – discovery URL for the Azure AD app

You must also have connectivity to the OEBS database, which is located in the secure zone. You can either use **vdi-utvikler-oebs** (a VDI set up for development in the secure zone) or the **Global Secure Access Client**. For more information, see the [oksty developer documentation](https://github.com/navikt/oksty-documentation).

[Swagger UI](http://localhost:8080/swagger-ui/index.html) is available when running locally.

---

## Testing

Unit tests are set up using JUnit, Mockito, and WireMock.

- `FullmaktServiceTest` – tests HTTP forwarding, query parameters, and Authorization header handling using WireMock
- `ControllerTest` – tests request/response mapping using MockMvc with a mocked `FullmaktService`
- `HttpLoggingFilterTest` – tests that all requests and responses are correctly logged to the database

Run all tests with:
```bash
mvn verify
```

---

## Monitoring and Alerting

No alerting is currently configured. Issues must be detected by users experiencing errors when calling the API, or through observed problems that can be traced back to the API.

Standard application monitoring is available via Grafana dashboards:
- [Grafana dashboard for t1](https://grafana.nav.cloud.nais.io/a/nais-apm-app/services/team-oebs/oebs-fullmakt-godkjenning-api-t1?namespace=team-oebs&environment=dev-gcp)
- [Grafana dashboard for q1](https://grafana.nav.cloud.nais.io/a/nais-apm-app/services/team-oebs/oebs-fullmakt-godkjenning-api-q1?namespace=team-oebs&environment=dev-gcp)
- [Grafana dashboard for prod](https://grafana.nav.cloud.nais.io/a/nais-apm-app/services/team-oebs/oebs-fullmakt-godkjenning-api?namespace=team-oebs&environment=prod-gcp)

---

## Deploy

### Branching strategy
- Feature development should happen on dedicated branches with a PR to `main`.
- Merging to `main` triggers deployment to **all environments** (T1, Q1, and production).

### Referencing Jira tasks
Include the Jira task key in the branch name and/or commit message. All PRs are squash-merged into main, so the most important thing is that the Jira issue is referenced in the squash commit message and that the PR title references the Jira issue. For example, if working on `OEBS-123`, the commit message should include `feat(OEBS-123): new rest endpoint` and the PR title should follow the same format. If a PR covers multiple Jira issues, all should be referenced, e.g. `feat(OEBS-123, OEBS-124): new rest endpoint and tests`. All individual commits should be listed in the PR description.

### Deployment pipeline
Deployments are handled by GitHub Actions.

### Promotion criteria
Before deploying to production:
- All tests must pass (`mvn verify`).
- SonarCloud analysis must not introduce new critical issues.

---

## Documentation

### Swagger / OpenAPI
Swagger UI is available when the application is running:

- [Swagger t1](https://oebs-fullmakt-godkjenning-api-t1.intern.dev.nav.no/swagger-ui/index.html)
- [Swagger q1](https://oebs-fullmakt-godkjenning-api-q1.intern.dev.nav.no/swagger-ui/index.html)
- [Swagger prod](https://oebs-fullmakt-godkjenning-api.intern.nav.no/swagger-ui/index.html)

### System documentation
#todo: Legg til lenker til systemdokumentasjon i Teams.
