package com.ubiquiti.networkStructure.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@With
public class Status {

    private String statusDescription;
    private StatusCode statusCode;
}
