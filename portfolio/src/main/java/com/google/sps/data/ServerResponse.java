package com.google.sps.data;

import com.google.sps.data.Comment;
import java.util.ArrayList;

public final class ServerResponse {

  private final ArrayList<Comment> comments;
  private final String user;
  private final String url;
  private final String uploadImageUrl;

  public ServerResponse(ArrayList<Comment> comments, String user, String url, String uploadImageUrl) {
    this.comments = comments;
    this.user = user;
    this.url = url;
    this.uploadImageUrl = uploadImageUrl;
  }
}