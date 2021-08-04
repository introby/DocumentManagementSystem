package by.intro.dms.exception;

import by.intro.dms.model.response.CustomExceptionResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        System.err.println(authException.getMessage());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        CustomExceptionResponse exceptionResponse = new CustomExceptionResponse();
        exceptionResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        exceptionResponse.setError("Unauthorized");
        exceptionResponse.setMessage(authException.getMessage());
        exceptionResponse.setPath(request.getServletPath());

        final ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), exceptionResponse);
    }
}
