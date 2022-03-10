package com.sellics.casestudy.cache;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.sellics.casestudy.exception.server.CsvReadException;
import com.sellics.casestudy.util.LoaderUtil;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Component
@Getter
@Log4j2
public class KeywordLoader {
    private static final String CSV_SEPARATOR = ";";

    @Value("${accessKey}")
    private String accessKey;

    @Value("${secretKey}")
    private String secretKey;

    @Value("${caseStudyBucketName}")
    private String caseStudyBucketName;

    private Map<String, List<KeywordAsinValue>> keywordAsinPairValues = new HashMap<>();
    private Map<String, Map<Long, List<Integer>>> keywordValues = new HashMap<>();
    private Map<String, Map<Long, List<Integer>>> asinValues = new HashMap<>();

    @PostConstruct
    private void initializeCache() {
        AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);

        AmazonS3 s3client = AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(Regions.EU_CENTRAL_1)
                .build();

        S3Object s3object = s3client.getObject(caseStudyBucketName, "public/case-keywords.csv");
        S3ObjectInputStream inputStream = s3object.getObjectContent();

        try (BufferedReader csvReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {

            String csvRecord = csvReader.readLine(); // skip header
            while ((csvRecord = csvReader.readLine()) != null) {
                String[] line = csvRecord.split(CSV_SEPARATOR);
                // timestamp seems to be in seconds, convert it into milliseconds
                long timestamp = Long.parseLong(line[0]) * 1000;
                String asin = line[1];
                String[] keywords = line[2].split(" ");
                int rank = Integer.parseInt(line[3]);

                fillKeywordAsinMap(timestamp, asin, keywords, rank);
                fillKeywordMap(timestamp, keywords, rank);
                fillAsinValues(asin, timestamp, rank);

            }
        } catch (IOException e) {
            throw new CsvReadException();
        }
        log.info("Loading cache for keyword file is finished !");
    }

    private void fillAsinValues(String asin, long timestamp, int rank) {
        fillTimestampAndRankPair(asin, timestamp, rank, asinValues);
    }

    private void fillKeywordMap(long timestamp, String[] keywords, int rank) {
        for (String keyword : keywords) {
            fillTimestampAndRankPair(keyword, timestamp, rank, keywordValues);
        }
    }

    private void fillKeywordAsinMap(long timestamp, String asin, String[] keywords, int rank) {
        for (String keyword : keywords) {
            String key = LoaderUtil.getKey(keyword, asin); // ASIN is ten-digit alphanumeric code so two different keyword + asin should not have same value
            if (keywordAsinPairValues.containsKey(key)) {
                keywordAsinPairValues.get(key).add(new KeywordAsinValue(timestamp, rank));
            } else {
                keywordAsinPairValues.put(key, new ArrayList<>(Collections.singletonList(new KeywordAsinValue(timestamp, rank))));
            }
        }
    }

    private void fillTimestampAndRankPair(String key, long timestamp, int rank, Map<String, Map<Long, List<Integer>>> map) {
        if (map.containsKey(key)) {
            if (map.get(key).containsKey(timestamp)) {
                map.get(key).get(timestamp).add(rank);
            } else {
                map.get(key).put(timestamp, new ArrayList<>(Collections.singletonList(rank)));
            }


        } else {
            map.put(key, new HashMap<>());
            map.get(key).put(timestamp, new ArrayList<>(Collections.singletonList(rank)));
        }
    }
}
