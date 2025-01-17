package org.example;

public record File(
    String owner,
    String bucketName,
    String name,
    String createdDate,
    String lastUpdate,
    String tags,
    String accessRights) {}
