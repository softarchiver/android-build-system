package com.mo_apps.abs.core.providers;

import org.eclipse.jetty.io.EofException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class EofExceptionMapper implements ExceptionMapper<EofException> {

    private static final Logger LOGGER = LoggerFactory.getLogger(EofExceptionMapper.class);

    @Override
    public Response toResponse(EofException exception) {
        LOGGER.warn(exception.getMessage(), exception.fillInStackTrace());

        return Response
                .status(Response.Status.BAD_REQUEST)
                .build();
    }

}
