package com.mo_apps.abs.core.providers;

import com.mo_apps.abs.resources.BaseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ConstraintViolationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConstraintViolationExceptionMapper.class);
    private static final int UNPROCESSABLE_ENTITY = 422;

    @Override
    public Response toResponse(ConstraintViolationException exception) {

        String message = exception.getMessage();
        for (ConstraintViolation violation : exception.getConstraintViolations()) {
            message += violation.getMessage() + ";";
        }

        LOGGER.warn(message, exception.fillInStackTrace());

        BaseResult result = new BaseResult(true, UNPROCESSABLE_ENTITY, message);
        return Response
                .status(UNPROCESSABLE_ENTITY)
                .type(MediaType.APPLICATION_JSON_TYPE)
                .entity(result)
                .build();
    }
}
