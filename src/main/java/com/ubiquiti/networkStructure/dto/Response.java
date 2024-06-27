package com.ubiquiti.networkStructure.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.With;

@EqualsAndHashCode(callSuper = true)
@With
@Getter
@Setter
public class Response<T> extends Status {
    private T body;

    public Response(T body) {
        this.body = body;
    }

    public Response<T> statusDescription(String statusDescription) {
        super.setStatusDescription(statusDescription);
        return this;
    }

    public Response<T> statusCode(StatusCode statusCode) {
        super.setStatusCode(statusCode);
        return this;
    }
}
