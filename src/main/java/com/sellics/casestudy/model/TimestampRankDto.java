package com.sellics.casestudy.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;
import java.util.List;

@Getter
@AllArgsConstructor
public class TimestampRankDto {
    private Date timestamp;
    private List<Integer> ranks;
}
