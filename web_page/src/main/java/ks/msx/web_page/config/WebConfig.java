package ks.msx.web_page.config;

import ks.msx.web_page.config.entry_point.JwtEntryPoint;
import ks.msx.web_page.config.filter.JwtFilter;
import ks.msx.web_page.controller.LoginController;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.InMemoryOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.client.web.HttpSessionOAuth2AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;
import java.util.Objects;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class WebConfig {
    private final JwtEntryPoint jwtEntryPoint;
    private final JwtFilter jwtFilter;
    private final Environment env;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(WHITE_LIST_URL).permitAll()
                        .requestMatchers(RESTRICTED_AREA_LIST_URL).authenticated()
                        .anyRequest().permitAll()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exc -> exc.authenticationEntryPoint(jwtEntryPoint))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .formLogin(f -> f
                        .loginPage(LoginController.PATH)
                        .defaultSuccessUrl(LoginController.PATH+"/login")
                )
                .oauth2Login(l -> l
                        .loginPage("/log/oauth2")
                        .defaultSuccessUrl("/success/log")
                        .authorizationEndpoint(end -> end.baseUri("/oauth2/authorize-client")
                                .authorizationRequestRepository(authorizationRequestRepository()))
                        .tokenEndpoint(t -> t.accessTokenResponseClient(accessTokenResponseClient()))
                );
        return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public AuthorizationRequestRepository<OAuth2AuthorizationRequest> authorizationRequestRepository(){
        return new HttpSessionOAuth2AuthorizationRequestRepository();
    }

    @Bean
    public OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> accessTokenResponseClient(){
        return new DefaultAuthorizationCodeTokenResponseClient();
    }

    private static final String CLIENT_PROPERTY_KEY = "spring.security.oauth2.client.registration.";

    private ClientRegistration getRegistration(String client){
        String clientId = env.getProperty(CLIENT_PROPERTY_KEY + client + ".client-id");
        String clientSecret = env.getProperty(CLIENT_PROPERTY_KEY + client + ".client-secret");
        if (clientId == null){
            return null;
        }

        return CommonOAuth2Provider.GOOGLE.getBuilder(client)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .build();
    }

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository(){
        List<ClientRegistration> registrations = clients.stream()
                .map(this::getRegistration)
                .filter(Objects::nonNull)
                .toList();
        return new InMemoryClientRegistrationRepository(registrations);
    }

    @Bean
    public OAuth2AuthorizedClientService authorizedClientService() {
        return new InMemoryOAuth2AuthorizedClientService(clientRegistrationRepository());
    }

    private static final List<String> clients = List.of("google");

    private static final String[] WHITE_LIST_URL = {
            "/",
            LoginController.PATH,
            LoginController.PATH+"/login",
            LoginController.PATH+"/reg",
            LoginController.PATH+"/registration",
            LoginController.PATH+"/logout",
            "/log/oauth2",
            "/success/log",
            "/test/google"
    };
    private static final String[] RESTRICTED_AREA_LIST_URL = {
            "/test",
            "/test1"
    };
}