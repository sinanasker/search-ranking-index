package com.sellics.casestudy.exception.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class ExceptionResponse {
    private Throwable throwable;
    private String errMessage;
}
