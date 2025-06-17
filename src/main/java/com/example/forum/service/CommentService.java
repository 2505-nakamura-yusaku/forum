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
import java.util.Optional;


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

    /*
     * レコード追加
     */
    public void saveComment(CommentForm reqComment) {
        Comment saveComment = setCommentEntity(reqComment);
        commentRepository.save(saveComment);
    }

    /*
     * リクエストから取得した情報をEntityに設定
     */
    private Comment setCommentEntity(CommentForm reqComment) {
        Comment comment = new Comment();
        comment.setId(reqComment.getId());
        comment.setReportId(reqComment.getReportId());
        comment.setContent(reqComment.getContent());
        return comment;
    }

    /*
     * 指定レコード取得処理
     */
    public CommentForm editComment(Integer id) {
        Optional<Comment> results = commentRepository.findById(id);
        if(results.isEmpty()) {
            return null;
        }
        Comment result = results.get();
        CommentForm comment = setCommentForm(result);
        return comment;
    }

    /*
     * DBから取得したデータをFormに設定
     */
    private CommentForm setCommentForm(Comment result) {
        CommentForm comment = new CommentForm();
        comment.setId(result.getId());
        comment.setReportId(result.getReportId());
        comment.setContent(result.getContent());

        return comment;
    }

    /*
     * レコード削除
     */
    public void deleteComment(int id) {
        Integer delId = id;
        commentRepository.deleteById(delId);
    }
}