package com.google.sps.data;

public final class Comment {

  private final long id;
  private final String text;
  private final String name;
  private final int emoji;
  private final long timestamp;

  public Comment(long id, String text, String name, int emoji, long timestamp) {
    this.id = id;
    this.text = text;
    this.name = name;
    this.emoji = emoji;
    this.timestamp = timestamp;
  }
}