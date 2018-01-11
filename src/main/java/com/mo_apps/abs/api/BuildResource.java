package com.mo_apps.abs.api;

import com.codahale.metrics.annotation.Metered;
import com.mo_apps.abs.AndroidBuildSystemConfiguration;
import com.mo_apps.abs.core.AppCreator;
import com.mo_apps.abs.resources.BaseResult;
import com.mo_apps.abs.resources.BuildData;
import com.mo_apps.abs.resources.StatusResult;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.apache.http.client.HttpClient;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.concurrent.ExecutorService;

/**
 * API endpoint для запросов на сборку .apk.
 */
@Api("Build requests")
@Path("/build")
public class BuildResource {

    private final AndroidBuildSystemConfiguration configuration;
    private final HttpClient httpClient;
    private final ExecutorService executorService;

    public BuildResource(AndroidBuildSystemConfiguration configuration,
                         ExecutorService executorService,
                         HttpClient httpClient) {
        this.configuration = configuration;
        this.httpClient = httpClient;
        this.executorService = executorService;
    }

    /**
     * Принимает запрос на сборку .apk.
     *
     * @param data JSON объект
     * @return http response
     */
    @ApiOperation(
            value = "Starts application build",
            response = BaseResult.class
    )
    @Metered
    @POST
    @Path("/apk")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
    public StatusResult acceptBuildRequest(//@Auth JwtClaim claim,
                                           @Valid @ApiParam(required = true) BuildData data) {
        AppCreator appCreator = new AppCreator(configuration, httpClient, data);
        executorService.execute(appCreator);
        //return new BaseResult(200, "OK");
        return new StatusResult("START",1);
    }

    /*public BaseResult startBuildRequest(@Body String ansver) {
        return new BaseResult(200, "OK");
    }*/

    /*@ApiOperation(
            value = "Starts application build",
            response = StatusResult.class
    )
    @Metered
    @POST
    @Path("/BuildAndPublish/BuildProgress")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
    public StatusResult finishBuildRequest(//@Auth JwtClaim claim,
                                           @Valid @ApiParam(required = true) BuildData data,
                                           @Valid @ApiParam(required = true) String url) {
        return new StatusResult(100,url);
    }*/

}
