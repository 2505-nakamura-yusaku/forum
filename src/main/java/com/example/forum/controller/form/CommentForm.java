package com.example.forum.controller.form;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class CommentForm {

    private int id;
    private int reportId;

    @NotEmpty
    private String content;
    private Timestamp createDate;
    private Timestamp updateDate;
}
