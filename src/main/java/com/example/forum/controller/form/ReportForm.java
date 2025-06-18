package com.example.forum.controller.form;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class ReportForm {

    private int id;
    private String content;
    private Timestamp createDate;
    private Timestamp updateDate;
}

