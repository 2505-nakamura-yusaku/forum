package com.example.forum.controller.form;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class CommentForm {

    private int id;
    private int reportId;
    private String content;
    private Timestamp createDate;
    private Timestamp updateDate;
}
