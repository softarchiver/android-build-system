package com.mo_apps.abs.resources;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * JSON response entity
 */
@ApiModel
public class BaseResult {

    // Fields -------------------------------------------------------------------------------------

    /**
     * Error flag
     */
    @ApiModelProperty
    private boolean err;

    /**
     * Response code
     */
    @ApiModelProperty
    private int code;

    /**
     * Response description
     */
    @ApiModelProperty
    private String message;

    // --------------------------------------------------------------------------------------------


    // Constructors -------------------------------------------------------------------------------

    @JsonCreator
    public BaseResult(@JsonProperty boolean err,
                      @JsonProperty int code,
                      @JsonProperty String message) {
        this.err = err;
        this.code = code;
        this.message = message;
    }

    public BaseResult(int code, String message) {
        this.err = false;
        this.code = code;
        this.message = message;
    }

    // --------------------------------------------------------------------------------------------


    // Getters and setters ------------------------------------------------------------------------

    @JsonProperty
    public boolean isErr() {
        return err;
    }

    @JsonProperty
    public int getCode() {
        return code;
    }

    @JsonProperty("result") // TODO: rename to message
    public String getMessage() {
        return message;
    }

    // --------------------------------------------------------------------------------------------
}
