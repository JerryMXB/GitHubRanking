package com.chaoqunhuang.services;

import com.chaoqunhuang.model.Ranking;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.MinMaxPriorityQueue;
import lombok.extern.log4j.Log4j2;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import java.io.IOException;

/**
 *
 * Created by chaoqunhuang on 6/14/18.
 */
@Log4j2
public class GithubCrawler {

    /**
     * Crawl the contributes from github
     * @param users users' whose contributes to crawl
     * @return String of rankings
     */
    public String getRankings(List<String> users) {
        List<Ranking> rankings = new LinkedList<>();
        for (String user : users) {
            try {
                Document doc = Jsoup.connect("https://github.com/" + user)
                        .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 FFirefox/3.0.7")
                        .referrer("http://www.google.com")
                        .get();
                Elements elements = doc.select("h2.f4.text-normal.mb-2");
                rankings.add(new Ranking(user, Integer.valueOf(elements.get(1).text().split(" ")[0])));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return serializer(rankResults(rankings));
    }

    /**
     * Test if a user exist in github
     * @param userName user to test
     * @return if a user exist
     */
    public boolean testUser(String userName) {
        try {
            Document doc = Jsoup.connect("https://github.com/" + userName)
                    .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 FFirefox/3.0.7")
                    .referrer("http://www.google.com")
                    .get();
            Elements elements = doc.select("h2.f4.text-normal.mb-2");
            Integer.valueOf(elements.get(1).text().split(" ")[0]);
            return true;
        } catch (Exception e) {
            // Using exception to conduct business logic - bad bad practice :)
            log.error(e.getMessage());
            return false;
        }
     }

    /**
     * Order the results
     * @param rankings contributes to order
     * @return ordered contributes
     */
    private List<Ranking> rankResults(List<Ranking> rankings) {
        MinMaxPriorityQueue<Ranking> queue =
                MinMaxPriorityQueue.orderedBy((Ranking r1, Ranking r2) -> (r2.getContributes() - r1.getContributes()))
                .maximumSize(5)
                .create();
        queue.addAll(rankings);
        List<Ranking> results = new ArrayList<>(queue);
        results.sort((Ranking r1, Ranking r2) -> (r2.getContributes() - r1.getContributes()));
        log.info(results.toString());
        return results;
    }

    /**
     * Serialize the rankings to json
     * @param rankings rankings to serialize
     * @return json result
     */
    private String serializer(List<Ranking> rankings) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(rankings);
        } catch (IOException ioe){
            log.error(ioe.getMessage());
            throw new RuntimeException(ioe);
        }
    }
}
