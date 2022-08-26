package org.pac4j.demo.spring;

import org.pac4j.cas.client.CasClient;
import org.pac4j.cas.config.CasConfiguration;
import org.pac4j.core.client.Clients;
import org.pac4j.core.config.Config;
import org.pac4j.core.matching.matcher.PathMatcher;
import org.pac4j.http.client.direct.DirectBasicAuthClient;
import org.pac4j.http.credentials.authenticator.test.SimpleTestUsernamePasswordAuthenticator;
import org.pac4j.oauth.client.TwitterClient;
import org.pac4j.springframework.web.SecurityFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.server.WebFilter;

import java.util.Optional;

@Configuration
// to define the callback and logout controllers
@ComponentScan(basePackages = "org.pac4j.springframework.web")
public class Pac4jConfig {

    @Bean
    public Config config() {
        final TwitterClient twitterClient = new TwitterClient("CoxUiYwQOSFDReZYdjigBA", "2kAzunH5Btc4gRSaMr7D7MkyoJ5u1VzbOOzE8rBofs");

        final CasConfiguration configuration = new CasConfiguration("https://casserverpac4j.herokuapp.com/login");
        final CasClient casClient = new CasClient(configuration);
        casClient.setAuthorizationGenerator( (ctx, session, profile) -> {
            profile.addRole("ROLE_ADMIN");
            return Optional.of(profile);
        });

        final DirectBasicAuthClient directBasicAuthClient = new DirectBasicAuthClient(new SimpleTestUsernamePasswordAuthenticator());

        final Clients clients = new Clients("http://localhost:8080/callback", twitterClient, casClient, directBasicAuthClient);

        final Config config = new Config(clients);

        config.setHttpActionAdapter(new CustomHttpActionAdapter());

        return config;
    }

    @Bean
    public WebFilter twitterFilter() {
        return new SecurityFilter(config(), "TwitterClient", new PathMatcher().includePath("/twitter"));
    }

    @Bean
    public WebFilter casFilter() {
        return new SecurityFilter(config(), "CasClient", new PathMatcher().includePath("/cas"));
    }

    @Bean
    public WebFilter protectedFilter() {
        return new SecurityFilter(config(), new PathMatcher().includePath("/protected"));
    }

    @Bean
    public WebFilter dbaFilter() {
        return new SecurityFilter(config(), "DirectBasicAuthClient", new PathMatcher().includePath("/dba"));
    }
}
