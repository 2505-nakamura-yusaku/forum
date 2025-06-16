package com.example.forum.service;

import com.example.forum.controller.form.CommentForm;
import com.example.forum.controller.form.ReportForm;
import com.example.forum.repository.CommentRepository;
import com.example.forum.repository.entity.Comment;
import com.example.forum.repository.entity.Report;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class CommentService {
    @Autowired
    CommentRepository commentRepository;

    /*
     * レコード全件取得処理
     */
    public List<CommentForm> findAllReport() {
        List<Comment> results = commentRepository.findAllByOrderByIdDesc();
        List<CommentForm> reports = setCommentForm(results);
        return reports;
    }

    /*
     * DBから取得したデータをCommentに設定
     */
    private List<CommentForm> setCommentForm(List<Comment> results) {
        List<CommentForm> reports = new ArrayList<>();

        for (int i = 0; i < results.size(); i++) {
            CommentForm report = new CommentForm();
            Comment result = results.get(i);
            report.setId(result.getId());
            report.setReportId(result.getReportId());
            report.setContent(result.getContent());
            reports.add(report);
        }
        return reports;
    }
}