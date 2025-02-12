package eu.possiblex.participantportal.application.configuration;

import eu.possiblex.participantportal.business.control.*;
import eu.possiblex.participantportal.utilities.LogUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.IOException;

@Configuration
@EnableScheduling
public class AppConfigurer {

    private static final int EXCHANGE_STRATEGY_SIZE = 16 * 1024 * 1024;

    private static final ExchangeStrategies EXCHANGE_STRATEGIES = ExchangeStrategies.builder()
        .codecs(codecs -> codecs.defaultCodecs().maxInMemorySize(EXCHANGE_STRATEGY_SIZE)).build();

    @Value("${edc.x-api-key}")
    private String edcAccessKey;

    @Value("${edc.mgmt-base-url}")
    private String edcMgmtUrl;

    @Value("${fh.catalog.repo.url}")
    private String fhCatalogRepoUrl;

    @Value("${fh.catalog.sparql.url}")
    private String fhCatalogSparqlUrl;

    @Value("${sd-creation-wizard-api.base-url}")
    private String sdCreationWizardApiBaseUri;

    @Value("${fh.catalog.secret-key}")
    private String fhCatalogSecretKey;

    @Value("${daps-server.base-url}")
    private String dapsServerBaseUri;

    @Value("${spring.security.admin.username}")
    private String adminUsername;

    @Value("${spring.security.admin.password}")
    private String adminPassword;

    @Bean
    public EdcClient edcClient() {

        WebClient webClient = WebClient.builder().baseUrl(edcMgmtUrl).clientConnector(LogUtils.createHttpClient())
            .defaultHeader("X-API-Key", edcAccessKey).build();
        HttpServiceProxyFactory httpServiceProxyFactory = HttpServiceProxyFactory.builder()
            .exchangeAdapter(WebClientAdapter.create(webClient)).build();
        return httpServiceProxyFactory.createClient(EdcClient.class);
    }

    @Bean
    public TechnicalFhCatalogClient technicalFhCatalogClient() {

        WebClient webClient = WebClient.builder().baseUrl(fhCatalogRepoUrl).clientConnector(LogUtils.createHttpClient())
            .defaultHeaders(httpHeaders -> {
                httpHeaders.set("Content-Type", "application/json");
                httpHeaders.set("Authorization", "Bearer " + fhCatalogSecretKey);
            }).build();
        HttpServiceProxyFactory httpServiceProxyFactory = HttpServiceProxyFactory.builder()
            .exchangeAdapter(WebClientAdapter.create(webClient)).build();
        return httpServiceProxyFactory.createClient(TechnicalFhCatalogClient.class);
    }

    @Bean
    public SparqlFhCatalogClient sparqlFhCatalogClient() {

        WebClient webClient = WebClient.builder().baseUrl(fhCatalogSparqlUrl)
            .clientConnector(LogUtils.createHttpClient()).defaultHeaders(httpHeaders -> {
                httpHeaders.set("Content-Type", "application/json");
                httpHeaders.set("Authorization", "Bearer " + fhCatalogSecretKey);
            }).build();
        HttpServiceProxyFactory httpServiceProxyFactory = HttpServiceProxyFactory.builder()
            .exchangeAdapter(WebClientAdapter.create(webClient)).build();
        return httpServiceProxyFactory.createClient(SparqlFhCatalogClient.class);
    }

    @Bean
    public SdCreationWizardApiClient sdCreationWizardApiClient() {

        WebClient webClient = WebClient.builder().exchangeStrategies(EXCHANGE_STRATEGIES)
            .baseUrl(sdCreationWizardApiBaseUri).build();
        HttpServiceProxyFactory httpServiceProxyFactory = HttpServiceProxyFactory.builderFor(
            WebClientAdapter.create(webClient)).build();
        return httpServiceProxyFactory.createClient(SdCreationWizardApiClient.class);
    }

    @Bean
    public OmejdnConnectorApiClient dapsConnectorApiClient() {

        WebClient webClient = WebClient.builder().baseUrl(dapsServerBaseUri + "/api/v1/connectors")
            .clientConnector(LogUtils.createHttpClient()).build();
        HttpServiceProxyFactory httpServiceProxyFactory = HttpServiceProxyFactory.builder()
            .exchangeAdapter(WebClientAdapter.create(webClient)).build();
        return httpServiceProxyFactory.createClient(OmejdnConnectorApiClient.class);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(
                authorizeHttpRequests -> authorizeHttpRequests.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                    .requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                    .requestMatchers("common/version").permitAll().anyRequest().authenticated())
            .httpBasic(basic -> basic.authenticationEntryPoint(new CustomAuthenticationEntryPoint()))
            .csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {

        UserDetails admin = User.builder().username(adminUsername).password(passwordEncoder().encode(adminPassword))
            .roles("ADMIN").build();

        return new InMemoryUserDetailsManager(admin);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {

        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(@NonNull CorsRegistry registry) {

                registry.addMapping("/**").allowedOriginPatterns("*")
                    .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS").allowedHeaders("*")
                    .allowCredentials(true);
            }
        };
    }

    private static class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
        @Override
        public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException {

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setHeader("WWW-Authenticate", "");
            response.getWriter().write("Unauthorized");
        }
    }
}
