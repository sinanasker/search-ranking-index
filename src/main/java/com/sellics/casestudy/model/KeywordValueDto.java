package com.sellics.casestudy.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class KeywordValueDto {
    private String keyword;
    private List<TimestampRankDto> timestampRanks;
}
