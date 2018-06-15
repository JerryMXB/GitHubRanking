package com.chaoqunhuang;

import com.chaoqunhuang.services.GithubCrawler;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by chaoqunhuang on 6/14/18.
 */
public class BenchTest {
    public static void main(String[] args) {
        GithubCrawler githubCrawler = new GithubCrawler();
        List<String> users = new LinkedList<>();
        users.add("jingyue00");
        users.add("Hunter6");
        users.add("JerryMXB");

        System.out.println(githubCrawler.getRankings(users).toString());
    }
}
