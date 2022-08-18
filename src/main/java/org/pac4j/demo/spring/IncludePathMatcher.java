package org.pac4j.demo.spring;

import org.pac4j.core.context.WebContext;
import org.pac4j.core.context.session.SessionStore;
import org.pac4j.core.matching.matcher.Matcher;

/**
 * To be refactored and moved to pac4j-core (PathMatcher).
 */
public class IncludePathMatcher implements Matcher {

    private final String path;

    public IncludePathMatcher(final String path) {
        this.path = path;
    }

    @Override
    public boolean matches(final WebContext context, final SessionStore sessionStore) {
        final String requestPath = context.getPath();
        return requestPath != null && requestPath.startsWith(path);
    }
}
