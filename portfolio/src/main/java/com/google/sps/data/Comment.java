package com.google.sps.data;

public final class Comment {

  private final String key;
  private final long id;
  private final String text;
  private final String name;
  private final long timestamp;
  private final double sentimentScore;
  private final String imageUrl;

  public Comment(String key, long id, String text, String name, long timestamp, double sentimentScore, String imageUrl) {
    this.key = key;
    this.id = id;
    this.text = text;
    this.name = name;
    this.timestamp = timestamp;
    this.sentimentScore = sentimentScore;
    this.imageUrl = imageUrl;
  }
}