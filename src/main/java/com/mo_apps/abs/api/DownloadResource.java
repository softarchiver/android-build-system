package com.mo_apps.abs.api;

import com.codahale.metrics.annotation.Metered;
import com.mo_apps.abs.core.util.FormatUtils;
import com.mo_apps.jwt_auth.auth.JwtClaim;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import io.dropwizard.auth.Auth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * API endpoint для скачивания .apk и .keystore файлов.
 */
@Api("Download API")
@Path("/dl")
@Produces(MediaType.APPLICATION_OCTET_STREAM)
public class DownloadResource {
    private final Logger logger = LoggerFactory.getLogger(DownloadResource.class);

    private String downloadsHome;

    public DownloadResource(String downloadsHome) {
        this.downloadsHome = FormatUtils.formatPath(downloadsHome);
    }

    @ApiOperation(
            value = "Download APK",
            produces = "application/octet-stream",
            response = Void.class
    )
    @Metered
    @GET
    @Path("/apk/{userId}/{appId}")
    public StreamingOutput fetchApk(/*@Auth() JwtClaim claim,*/
                                    @PathParam("userId") @ApiParam(required = true) String userId,
                                    @PathParam("appId") @ApiParam(required = true) String appId) {
        return output -> {
            // FIXME стоит впилить сюда что-то поумнее?
            String apkName = "release.apk";
            File apk = new File(
                    downloadsHome + String.format("%s_%s", userId, appId) + File.separator + apkName
            );

            try {
                FileInputStream stream = new FileInputStream(apk);
                int nextByte = 0;
                while ((nextByte = stream.read()) != -1) {
                    output.write(nextByte);
                }
                output.flush();
                output.close();
            } catch (FileNotFoundException e) {
                logger.warn("Requested apk not found. user ID:{} app ID:{}",
                        userId,
                        appId,
                        e.fillInStackTrace()
                );
                throw new WebApplicationException(Response.Status.NOT_FOUND);
            }
        };
    }

    @ApiOperation(
            value = "Download keystore",
            produces = "application/octet-stream",
            response = Void.class
    )
    @Metered
    @GET
    @Path("/keystore/{userId}/{appId}")
    public StreamingOutput fetchKeystore(/*@Auth() JwtClaim claim,*/
                                         @PathParam("userId") @ApiParam(required = true) String userId,
                                         @PathParam("appId") @ApiParam(required = true) String appId) {

        return output -> {
            // FIXME стоит запилить название хранилища ключей с использованием userId и appId?
            File keystore = new File(
                    downloadsHome + String.format("%s_%s", userId, appId) + File.separator + "keystore.keystore"
            );

            try {
                FileInputStream stream = new FileInputStream(keystore);
                int nextByte = 0;
                while ((nextByte = stream.read()) != -1) {
                    output.write(nextByte);
                }
                output.flush();
                output.close();
            } catch (FileNotFoundException e) {
                logger.warn(
                        "Requested keystore not found. user ID:{} app ID:{}",
                        userId,
                        appId,
                        e.fillInStackTrace()
                );
                throw new WebApplicationException(Response.Status.NOT_FOUND);
            }
        };
    }
}
