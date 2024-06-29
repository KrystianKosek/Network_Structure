package com.ubiquiti.networkStructure.dto;

import lombok.*;

/**
 * Base class storing the status of the rest request.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@With
public class Status {
    /**
     * Status message.
     */
    private String statusDescription;

    /**
     * Status code.
     */
    private StatusCode statusCode;
}
