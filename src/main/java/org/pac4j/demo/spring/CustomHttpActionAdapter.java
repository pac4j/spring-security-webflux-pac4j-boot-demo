package org.pac4j.demo.spring;

import org.pac4j.core.context.WebContext;
import org.pac4j.core.exception.http.HttpAction;
import org.pac4j.springframework.context.SpringWebfluxWebContext;
import org.pac4j.springframework.http.SpringWebfluxHttpActionAdapter;
import org.springframework.core.io.buffer.DataBuffer;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

public class CustomHttpActionAdapter extends SpringWebfluxHttpActionAdapter {

    @Override
    public Object adapt(final HttpAction action, final WebContext context) {
        if (action != null) {
            var code = action.getCode();

            // to be refactored if pac4j offers new UnauthorizedAction(content), ...
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

    protected Object write(final WebContext context, final int code, final String content) {
        context.setResponseContentType("text/html;charset=UTF-8");
        final var response = ((SpringWebfluxWebContext) context).getNativeResponse();
        response.setRawStatusCode(code);
        final DataBuffer data = response.bufferFactory().wrap(content.getBytes(StandardCharsets.UTF_8));
        ((SpringWebfluxWebContext) context).setResult(response.writeWith(Mono.just(data)));
        return null;
    }
}
