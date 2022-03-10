package com.sellics.casestudy.service;


import com.sellics.casestudy.cache.KeywordLoader;
import com.sellics.casestudy.exception.client.ResourceNotFoundException;
import com.sellics.casestudy.cache.KeywordAsinValue;
import com.sellics.casestudy.util.LoaderUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RankingIndexServiceTest {

    @InjectMocks
    private RankingIndexService rankingIndexService;

    @Mock
    private KeywordLoader keywordLoader;

    @Test
    void testGetByKeywordAndAsin() {
        Map<String, List<KeywordAsinValue>> keywordMap = new HashMap<>();
        String keyword = "f250";
        String asin = "B0928G4K6L";
        int rank = 93;
        long timestamp = 1637024931000L;
        String key = LoaderUtil.getKey(keyword, asin);
        keywordMap.put(key, new ArrayList<>() {
            {
                add(new KeywordAsinValue(timestamp, rank));
            }
        });
        when(keywordLoader.getKeywordAsinPairValues()).thenReturn(keywordMap);

        var result = rankingIndexService.getByKeywordAndAsin(keyword, asin);
        assertEquals(1, result.size());
        assertEquals(rank, result.get(0).getRank());
        assertEquals(new Date(timestamp), result.get(0).getTimestamp());
    }

    @Test
    void testGetByKeywordAndAsin_whenResourceNotFound_ThrowException() {
        Map<String, List<KeywordAsinValue>> keywordMap = new HashMap<>();
        String keyword = "f250";
        String asin = "B0928G4K6L";
        when(keywordLoader.getKeywordAsinPairValues()).thenReturn(keywordMap);

        Assertions.assertThrows(ResourceNotFoundException.class, () -> rankingIndexService.getByKeywordAndAsin(keyword, asin));
    }

    @Test
    void testGetByKeyword() {
        String keyword = "f250";
        int rank = 93;
        long timestamp = 1637024931000L;
        Map<String, Map<Long, List<Integer>>> map = new HashMap<>();
        map.put(keyword, new HashMap<>());
        map.get(keyword).put(timestamp, new ArrayList<>() {
            {
                add(rank);
            }
        });

        when(keywordLoader.getKeywordValues()).thenReturn(map);


        var result = rankingIndexService.getByKeyword(keyword);
        assertEquals(keyword, result.getKeyword());
        assertEquals(1, result.getTimestampRanks().size());
        assertEquals(new Date(timestamp), result.getTimestampRanks().get(0).getTimestamp());
        assertEquals(Optional.of(rank).get(), result.getTimestampRanks().get(0).getRanks().get(0));
    }

    @Test
    void testGetByAsin() {
        String asin = "B0928G4K6L";
        int rank = 93;
        long timestamp = 1637024931000L;
        Map<String, Map<Long, List<Integer>>> map = new HashMap<>();
        map.put(asin, new HashMap<>());
        map.get(asin).put(timestamp, new ArrayList<>() {
            {
                add(rank);
            }
        });

        when(keywordLoader.getAsinValues()).thenReturn(map);

        var result = rankingIndexService.getByAsin(asin);
        assertEquals(asin, result.getAsin());
        assertEquals(1, result.getTimestampRanks().size());
        assertEquals(new Date(timestamp), result.getTimestampRanks().get(0).getTimestamp());
        assertEquals(Optional.of(rank).get(), result.getTimestampRanks().get(0).getRanks().get(0));
    }
}