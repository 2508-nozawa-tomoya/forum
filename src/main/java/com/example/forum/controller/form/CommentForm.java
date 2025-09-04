package com.example.forum.controller.form;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Date;


@Getter
@Setter
public class CommentForm {
    private int id;
    private String content;
    private int reportId;
    private Timestamp createdDate;
    private Timestamp updatedDate;
}
