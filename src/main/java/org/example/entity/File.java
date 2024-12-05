package org.example.entity;

import java.sql.Date;

public record File(
    String fileName,
    String created_by,
    String updated_by,
    Date created_datetime,
    Date updated_datetime) {}
