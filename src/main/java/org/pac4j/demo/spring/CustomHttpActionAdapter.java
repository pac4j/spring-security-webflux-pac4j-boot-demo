package org.pac4j.demo.spring;

import org.pac4j.core.context.WebContext;
import org.pac4j.core.exception.http.HttpAction;
import org.pac4j.springframework.context.SpringWebfluxWebContext;
import org.pac4j.springframework.http.SpringWebfluxHttpActionAdapter;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatusCode;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

public class CustomHttpActionAdapter extends SpringWebfluxHttpActionAdapter {

    @Override
    public Mono<?> adapt(final HttpAction action, final WebContext context) {
        if (action != null) {
            final var code = action.getCode();

            if (code == 401) {
                return write(context, code, "<html><body><h1>unauthorized</h1><br /><a href=\"/\">Home</a></body></html>");
            } else if (code == 403) {
                return write(context, code, "<html><body><h1>forbidden</h1><br /><a href=\"/\">Home</a></body></html>");
            } else if (code == 500) {
                return write(context, code, "<html><body><h1>internal error</h1><br /><a href=\"/\">Home</a></body></html>");
            }
        }

        return super.adapt(action, context);
    }

    protected Mono<Void> write(final WebContext context, final int code, final String content) {
        context.setResponseContentType("text/html;charset=UTF-8");
        final var response = ((SpringWebfluxWebContext) context).getNativeResponse();
        response.setStatusCode(HttpStatusCode.valueOf(code));
        final DataBuffer data = response.bufferFactory().wrap(content.getBytes(StandardCharsets.UTF_8));
        return response.writeWith(Mono.just(data));
    }
}
