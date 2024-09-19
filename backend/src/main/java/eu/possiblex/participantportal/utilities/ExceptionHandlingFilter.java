package eu.possiblex.participantportal.utilities;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Component
@Slf4j
public class ExceptionHandlingFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            chain.doFilter(request, response);
        } catch (Exception e) {
            if (isPossibleXException(e)) {
                PossibleXException ex = getPossibleXException(e);
                ((HttpServletResponse) response).setStatus(ex.getStatus().value());
                log.error("PossibleXException: {}", ex);
            } else {
                ((HttpServletResponse) response).setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
                log.error("Exception: {}", e);
            }

        }
    }

    public boolean isPossibleXException(Throwable e) {
        PossibleXException pe = getPossibleXException(e);
        return pe != null;
    }

    public PossibleXException getPossibleXException(Throwable e) {
        while (e != null) {
            if (e instanceof PossibleXException) {
                return (PossibleXException) e;
            }
            e = e.getCause();
        }
        return null;
    }
}
