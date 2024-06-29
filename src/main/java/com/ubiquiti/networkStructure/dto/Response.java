package com.ubiquiti.networkStructure.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.With;

/**
 * Class storing the enhanced response of the rest request.
 *
 * @param <T> type of the response body
 */
@EqualsAndHashCode(callSuper = true)
@With
@Getter
@Setter
public class Response<T> extends Status {

    /**
     * Response body.
     */
    private T body;

    /**
     * Default constructor.
     *
     * @param body body
     */
    public Response(T body) {
        this.body = body;
    }

    /**
     * Sets response status description and returns instance of object.
     *
     * @param statusDescription response status description
     * @return instance of object
     */
    public Response<T> statusDescription(String statusDescription) {
        super.setStatusDescription(statusDescription);
        return this;
    }

    /**
     * Sets response status code and returns instance of object.
     *
     * @param statusCode response status code
     * @return instance of object
     */
    public Response<T> statusCode(StatusCode statusCode) {
        super.setStatusCode(statusCode);
        return this;
    }
}
