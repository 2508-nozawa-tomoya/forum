package com.example.forum.controller.form;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Date;

@Getter
@Setter
public class ReportForm {

    private int id;
    private String content;
    private Timestamp createdDate;
    private Timestamp updatedDate;
}
