package com.mo_apps.abs.core.providers;

import com.mo_apps.abs.resources.BaseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import static javax.ws.rs.core.Response.Status.NOT_IMPLEMENTED;

@Provider
public class UnsupportedOperationExceptionMapper
        implements javax.ws.rs.ext.ExceptionMapper<UnsupportedOperationException> {

    public static final Logger LOGGER = LoggerFactory.getLogger(UnsupportedOperationExceptionMapper.class);

    @Override
    public Response toResponse(UnsupportedOperationException exception) {
        LOGGER.warn(exception.getMessage(), exception.fillInStackTrace());

        BaseResult result = new BaseResult(true, 501, "Not implemented");

        return Response
                .status(NOT_IMPLEMENTED)
                .type(MediaType.APPLICATION_JSON_TYPE)
                .entity(result)
                .build();
    }
}
