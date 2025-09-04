package com.example.forum.controller.form;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class CommentForm {
    private int id;
    @NotBlank(message = "コメントを入力してください")
    private String content;
    private int reportId;
    private Timestamp createdDate;
    private Timestamp updatedDate;
}
