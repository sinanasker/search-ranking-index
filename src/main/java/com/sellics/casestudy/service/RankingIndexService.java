package com.sellics.casestudy.service;

import com.sellics.casestudy.cache.KeywordAsinValue;
import com.sellics.casestudy.cache.KeywordLoader;
import com.sellics.casestudy.exception.client.ResourceNotFoundException;
import com.sellics.casestudy.model.AsinValueDto;
import com.sellics.casestudy.model.KeywordAsinValueDto;
import com.sellics.casestudy.model.KeywordValueDto;
import com.sellics.casestudy.model.TimestampRankDto;
import com.sellics.casestudy.util.LoaderUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RankingIndexService {

    private final KeywordLoader loader;

    public List<KeywordAsinValueDto> getByKeywordAndAsin(String keyword, String asin) {
        Map<String, List<KeywordAsinValue>> keywordMap = loader.getKeywordAsinPairValues();
        String key = LoaderUtil.getKey(keyword, asin);
        if (keywordMap.containsKey(key)) {
            return keywordMap.get(key)
                    .stream()
                    .map(value -> new KeywordAsinValueDto(new Date(value.getTimestamp()), value.getRank()))
                    .collect(Collectors.toList());
        } else {
            throw new ResourceNotFoundException();
        }
    }

    public KeywordValueDto getByKeyword(String keyword) {
        var keywordMap = loader.getKeywordValues();
        List<TimestampRankDto> values = new ArrayList<>();

        if(keywordMap.containsKey(keyword)){
            for (var entry : keywordMap.get(keyword).entrySet()) {
                values.add(new TimestampRankDto(new Date(entry.getKey()), entry.getValue()));
            }
        }else{
            throw new ResourceNotFoundException();
        }

        return new KeywordValueDto(keyword, values);
    }

    public AsinValueDto getByAsin(String asin) {
        var asinMap = loader.getAsinValues();
        List<TimestampRankDto> values = new ArrayList<>();

        if(asinMap.containsKey(asin)){
            for (var entry : asinMap.get(asin).entrySet()) {
                values.add(new TimestampRankDto(new Date(entry.getKey()), entry.getValue()));
            }
        }else{
            throw new ResourceNotFoundException();
        }

        return new AsinValueDto(asin, values);
    }
}
