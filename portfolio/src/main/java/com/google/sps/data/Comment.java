package com.google.sps.data;

public final class Comment {

  private final String key;
  private final long id;
  private final String text;
  private final String name;
  private final int emoji;
  private final long timestamp;

  public Comment(String key, long id, String text, String name, int emoji, long timestamp) {
    this.key = key;
    this.id = id;
    this.text = text;
    this.name = name;
    this.emoji = emoji;
    this.timestamp = timestamp;
  }
}