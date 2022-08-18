package org.pac4j.demo.spring;

import org.pac4j.core.context.WebContext;
import org.pac4j.core.context.session.SessionStore;
import org.pac4j.core.exception.http.HttpAction;
import org.pac4j.core.profile.ProfileManager;
import org.pac4j.springframework.context.SpringWebfluxSessionStore;
import org.pac4j.springframework.context.SpringWebfluxWebContext;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Map;

@Controller
public class Application {

    @RequestMapping("/")
    public Mono<String> root(final ServerWebExchange exchange, final Map<String, Object> map) throws HttpAction {
        return index(exchange, map);
    }

    @RequestMapping("/index.html")
    public Mono<String> index(final ServerWebExchange exchange, final Map<String, Object> map) throws HttpAction {
        final WebContext context = new SpringWebfluxWebContext(exchange);
        final SessionStore sessionStore = new SpringWebfluxSessionStore(exchange);
        final ProfileManager profileManager = new ProfileManager(context, sessionStore);
        map.put("profiles", profileManager.getProfiles());
        map.put("sessionId", sessionStore.getSessionId(context, false).orElse("nosession"));
        map.put("context", "");
        return ReactiveSecurityContextHolder.getContext().map(ctx -> {
            map.put("context", ctx);
            return "index";
        }).defaultIfEmpty("index");
    }

    @RequestMapping(value = {"/twitter/index.html", "/cas/index.html", "/dba/index.html",
            "/protected/index.html", "/admin/index.html", "/user/index.html"})
    public Mono<String> protect(final ServerWebExchange exchange, final Map<String, Object> map) {
        return protectedIndex(exchange, map);
    }

    @RequestMapping("/info.html")
    public String info(final ServerWebExchange exchange, final Map<String, Object> map) {
        final SessionStore sessionStore = new SpringWebfluxSessionStore(exchange);
        final double averageWaitTime = ((double) SpringWebfluxSessionStore.getWaitedTime()) / SpringWebfluxSessionStore.getNbWaitCalls();
        map.put("averageWaitTime", averageWaitTime);
        map.put("sessionStore", sessionStore);
        return "info";
    }

    @RequestMapping("/login")
    public String login() {
        return "loginform";
    }

    protected Mono<String> protectedIndex(final ServerWebExchange exchange, final Map<String, Object> map) {
        final WebContext context = new SpringWebfluxWebContext(exchange);
        final SessionStore sessionStore = new SpringWebfluxSessionStore(exchange);
        final ProfileManager profileManager = new ProfileManager(context, sessionStore);
        map.put("profiles", profileManager.getProfiles());
        map.put("context", "");
        return ReactiveSecurityContextHolder.getContext().map(ctx -> {
            map.put("context", ctx);
            return "protectedIndex";
        }).defaultIfEmpty("protectedIndex");
    }
}
