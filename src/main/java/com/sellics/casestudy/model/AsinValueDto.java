package com.sellics.casestudy.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class AsinValueDto {
    private String asin;
    private List<TimestampRankDto> timestampRanks;
}
