package eu.possiblex.participantportal.utilities;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Component
@Slf4j
public class RequestFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException, PossibleXException {
        try {
            chain.doFilter(request, response);
        } catch (PossibleXException e) {
            log.error("PossibleXException: {}", e.getMessage());
            ((HttpServletResponse) response).setStatus(e.getStatus().value());
        } catch (Exception e) {
            if (e.getCause() instanceof PossibleXException) {
                PossibleXException ex = (PossibleXException) e.getCause();
                ((HttpServletResponse) response).setStatus(ex.getStatus().value());
                log.error("Error: {}", ex.getMessage());
                log.error("Error2: {}", ex.getStatus().value());
            } else {
                ((HttpServletResponse) response).setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            }

        }
    }
}
