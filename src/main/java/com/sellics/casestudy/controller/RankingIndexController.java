package com.sellics.casestudy.controller;

import com.sellics.casestudy.model.AsinValueDto;
import com.sellics.casestudy.model.KeywordAsinValueDto;
import com.sellics.casestudy.model.KeywordValueDto;
import com.sellics.casestudy.service.RankingIndexService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/v1/ranking")
@RestController
public class RankingIndexController {

    private final RankingIndexService rankingIndexService;

    @GetMapping("keyword-asin/{keyword}/{asin}")
    public ResponseEntity<List<KeywordAsinValueDto>> getByKeywordAndAsin(@PathVariable("keyword") String keyword,
                                                                         @PathVariable("asin") String asin){
        return new ResponseEntity<>(rankingIndexService.getByKeywordAndAsin(keyword, asin), HttpStatus.OK);
    }

    @GetMapping("keyword/{keyword}")
    public ResponseEntity<KeywordValueDto> getByKeyword(@PathVariable("keyword") String keyword){
        return new ResponseEntity<>(rankingIndexService.getByKeyword(keyword), HttpStatus.OK);
    }

    @GetMapping("asin/{asin}")
    public ResponseEntity<AsinValueDto> getByAsin(@PathVariable("asin") String asin){
        return new ResponseEntity<>(rankingIndexService.getByAsin(asin), HttpStatus.OK);
    }
}
