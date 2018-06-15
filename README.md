# GitHubRanking

This repo serves as the backend for MXB GitHubRanking. This services crawls github users' contributions from last year, rank and display
them in a bar chart. [GitHubRanking](http://www.chaoqunhuang.com/githubranking.html)

# Install
```
mvn clean package
```

# Deploy
```
java -jar target/GithubRanking-1.0-SNAPSHOT-fat.jar
```

The service will start to listen to port 8787. [Change here](https://github.com/JerryMXB/GitHubRanking/blob/master/src/main/java/com/chaoqunhuang/Server.java#L64)

# Play with it

The service can response to two kind of operations:

- Add user to ranking list
```
http://localhost:8787/?action=add&user=<github username>
```

- Get the top-5 ranking
```
http://localhost:8787
```
