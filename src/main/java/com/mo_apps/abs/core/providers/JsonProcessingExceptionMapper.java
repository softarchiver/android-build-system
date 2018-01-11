package com.mo_apps.abs.core.providers;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.mo_apps.abs.resources.BaseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class JsonProcessingExceptionMapper implements ExceptionMapper<JsonProcessingException> {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonProcessingExceptionMapper.class);

    @Override
    public Response toResponse(JsonProcessingException exception) {
        BaseResult result;

        if (exception instanceof JsonGenerationException) {
            LOGGER.warn("Error generation JSON", exception);

            result = new BaseResult(true, 500, "Server error");
            return Response
                    .serverError()
                    .type(MediaType.APPLICATION_JSON_TYPE)
                    .entity(result)
                    .build();
        }

        String message = exception.getOriginalMessage();

        if (message.startsWith("No suitable constructor found")) {
            LOGGER.error("Unable to deserialize specific type", exception);

            result = new BaseResult(true, 500, "Server error");
            return Response
                    .serverError()
                    .type(MediaType.APPLICATION_JSON_TYPE)
                    .entity(result)
                    .build();
        }

        LOGGER.warn("Unable to process JSON", exception);

        result = new BaseResult(true, 400, "Bad request");
        return Response
                .status(Response.Status.BAD_REQUEST)
                .type(MediaType.APPLICATION_JSON_TYPE)
                .entity(result)
                .build();
    }
}
