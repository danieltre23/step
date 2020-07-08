// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.servlets;

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import com.google.gson.Gson;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.sps.data.Comment;
import com.google.sps.data.ServerResponse;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.cloud.language.v1.Document;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.cloud.language.v1.Sentiment;

/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/data")
public class DataServlet extends HttpServlet {

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String newComment = request.getParameter("comment");
    String name = request.getParameter("name");
    int emoji = Integer.parseInt(request.getParameter("emoji"));
    long timestamp = System.currentTimeMillis();
    Entity commentEntity = new Entity("Comment");
    commentEntity.setProperty("text", newComment);
    commentEntity.setProperty("name", name);
    commentEntity.setProperty("emoji", emoji);
    commentEntity.setProperty("timestamp", timestamp);

    Document doc = Document.newBuilder().setContent(newComment).setType(Document.Type.PLAIN_TEXT).build();
    LanguageServiceClient languageService = LanguageServiceClient.create();
    Sentiment sentiment = languageService.analyzeSentiment(doc).getDocumentSentiment();
    float score = sentiment.getScore();
    languageService.close();
    commentEntity.setProperty("sentimentScore", score);

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    datastore.put(commentEntity);

    response.sendRedirect("/index.html");
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Query query = new Query("Comment").addSort("timestamp", SortDirection.DESCENDING);

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery results = datastore.prepare(query);

    ArrayList<Comment> comments = new ArrayList<>();
    for (Entity entity : results.asIterable()) {
      String key = KeyFactory.keyToString(entity.getKey());
      long id = entity.getKey().getId();
      String title = (String) entity.getProperty("text");
      String name = (String) entity.getProperty("name");
      int emoji = Math.toIntExact( (long) entity.getProperty("emoji"));
      long timestamp = (long) entity.getProperty("timestamp");
      double sentimentScore = (double) entity.getProperty("sentimentScore");

      Comment newComment = new Comment(key, id, title, name, emoji, timestamp, sentimentScore);
      comments.add(newComment);
    }

    UserService userService = UserServiceFactory.getUserService();
    String userEmail = null;
    String url = null;
    if (userService.isUserLoggedIn()) {
      userEmail = userService.getCurrentUser().getEmail();
      url = userService.createLogoutURL("/");
    } else {
      url = userService.createLoginURL("/");
    }
    
    ServerResponse resp = new ServerResponse(comments, userEmail, url);

    response.setContentType("text/json;");
    response.getWriter().println(convertToJsonUsingGson(resp));
  }

  private String convertToJsonUsingGson(ServerResponse list) {
    Gson gson = new Gson();
    return gson.toJson(list);
  }
}
