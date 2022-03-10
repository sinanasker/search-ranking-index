package com.sellics.casestudy.cache;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
@AllArgsConstructor
public class AsinValue {
    private Timestamp timestamp;
    private int rank;
    private String keyword;
}
