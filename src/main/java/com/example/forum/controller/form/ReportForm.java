package com.example.forum.controller.form;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReportForm {

    private int id;
    private String content;
}
