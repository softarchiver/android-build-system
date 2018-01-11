package com.mo_apps.abs.core.providers;

import com.mo_apps.abs.resources.BaseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class RuntimeExceptionMapper implements ExceptionMapper<RuntimeException> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RuntimeExceptionMapper.class);

    @Override
    public Response toResponse(RuntimeException exception) {
        LOGGER.error(exception.getMessage(), exception.fillInStackTrace());

        BaseResult result = new BaseResult(true, 500, "Server error");
        return Response
                .serverError()
                .type(MediaType.APPLICATION_JSON_TYPE)
                .entity(result)
                .build();
    }

}
