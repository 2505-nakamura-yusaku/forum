package com.example.forum.controller.form;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import jakarta.validation.constraints.NotNull;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class ReportForm {

    private int id;

    @NotEmpty
    private String content;
    private Timestamp createDate;
    private Timestamp updateDate;
}

