package com.chaoqunhuang;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import lombok.extern.log4j.Log4j2;
import com.chaoqunhuang.services.GithubCrawler;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Log4j2
public class Server extends AbstractVerticle {

    @Override
    public void start() {
        log.info("com.chaoqunhuang.Server Starts");
        HttpServer server = vertx.createHttpServer();
        GithubCrawler githubCrawler = new GithubCrawler();
        List<String> users = new LinkedList<>();
        users.add("jingyue00");
        users.add("Hunter6");
        users.add("JerryMXB");
        // Bypassing final, obviously not thread safe
        final AtomicReference<Boolean> lastChange = new AtomicReference<>();
        lastChange.set(false);
        final AtomicReference<String> cacheResult = new AtomicReference<>();
        cacheResult.set(githubCrawler.getRankings(users));

        server.requestHandler(request -> {
            // This handler gets called for each request that arrives on the server
            HttpServerResponse response = request.response();
            final String action = request.getParam("action");

            if ("add".equals(action)) {

                String user = request.getParam("user");
                response.putHeader("content-type", "text/plain");
                response.putHeader("Access-Control-Allow-Origin", "*");

                if (githubCrawler.testUser(user)) {
                    users.add(user);
                    lastChange.set(true);
                    response.end("Success");
                } else {
                    response.end("Fail");
                }

            } else {
                response.putHeader("content-type", "text/plain");
                response.putHeader("Access-Control-Allow-Origin", "*");

                // Write to the response and end it
                if (!lastChange.get()) {
                    response.end(cacheResult.get());
                } else {
                    lastChange.set(false);
                    cacheResult.set(githubCrawler.getRankings(users));
                    response.end(cacheResult.get());
                }
            }
        });
        server.listen(8787);
    }
}
