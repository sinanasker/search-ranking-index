package com.sellics.casestudy.controller;


import com.sellics.casestudy.exception.client.ResourceNotFoundException;
import com.sellics.casestudy.model.AsinValueDto;
import com.sellics.casestudy.model.KeywordAsinValueDto;
import com.sellics.casestudy.model.KeywordValueDto;
import com.sellics.casestudy.service.RankingIndexService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(value = RankingIndexController.class)
class RankingIndexControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private RankingIndexService rankingIndexService;

    @Test
    void getByKeywordAndAsin() throws Exception {
        List<KeywordAsinValueDto> list = new ArrayList<>();
        when(rankingIndexService.getByKeywordAndAsin(any(), any())).thenReturn(list);
        RequestBuilder requestBuilder = get("/api/v1/ranking/keyword-asin/f250/B0928G4K6L").accept(MediaType.APPLICATION_JSON);
        MvcResult result = mvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    void getByKeywordAndAsin_whenResourceNotFound_thenReturn404() throws Exception {
        when(rankingIndexService.getByKeywordAndAsin(any(), any())).thenThrow(ResourceNotFoundException.class);
        RequestBuilder requestBuilder = get("/api/v1/ranking/keyword-asin/f250/B0928G4K6L").accept(MediaType.APPLICATION_JSON);
        MvcResult result = mvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }

    @Test
    void getByKeyword() throws Exception {
        KeywordValueDto keywordValue = new KeywordValueDto("f250", new ArrayList<>());
        when(rankingIndexService.getByKeyword(any())).thenReturn(keywordValue);
        RequestBuilder requestBuilder = get("/api/v1/ranking/keyword/f250").accept(MediaType.APPLICATION_JSON);
        MvcResult result = mvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    void getByAsin() throws Exception {
        AsinValueDto asinValue = new AsinValueDto("B0928G4K6L", new ArrayList<>());
        when(rankingIndexService.getByAsin(any())).thenReturn(asinValue);
        RequestBuilder requestBuilder = get("/api/v1/ranking/asin/B0928G4K6L").accept(MediaType.APPLICATION_JSON);
        MvcResult result = mvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

}