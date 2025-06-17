package com.example.forum.controller;

import com.example.forum.controller.form.CommentForm;
import com.example.forum.controller.form.ReportForm;
import com.example.forum.repository.entity.Report;
import com.example.forum.service.CommentService;
import com.example.forum.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;

@Controller
public class ForumController {
    @Autowired
    ReportService reportService;

    @Autowired
    CommentService commentService;

    /*
     * 投稿内容表示処理
     */
    @GetMapping
    public ModelAndView top() {
        ModelAndView mav = new ModelAndView();
        // 投稿を全件取得
        List<ReportForm> contentData = reportService.findAllReport();
        // コメントを全件取得
        List<CommentForm> commentData = commentService.findAllReport();
        // form用の空のentityを準備
        CommentForm commentForm = new CommentForm();
        // 準備した空のFormを保管
        mav.addObject("formModel", commentForm);
        // 画面遷移先を指定
        mav.setViewName("/top");
        // 投稿データオブジェクトを保管
        mav.addObject("contents", contentData);
        // 返信データオブジェクトを保管
        mav.addObject("comments", commentData);
        return mav;
    }

    /*
     * 新規投稿画面表示
     */
    @GetMapping("/new")
    public ModelAndView newContent() {
        ModelAndView mav = new ModelAndView();
        // form用の空のentityを準備
        ReportForm reportForm = new ReportForm();
        // 画面遷移先を指定
        mav.setViewName("/new");
        // 準備した空のFormを保管
        mav.addObject("formModel", reportForm);
        return mav;
    }

    /*
     * 新規投稿処理
     */
    @PostMapping("/add")
    public ModelAndView addContent(@ModelAttribute("formModel") ReportForm reportForm){
        // 投稿をテーブルに格納
        reportService.saveReport(reportForm);
        // rootへリダイレクト
        return new ModelAndView("redirect:/");
    }

    /*
     * 投稿削除処理
     */
    @DeleteMapping("/delete/{id}")
    public ModelAndView deleteContent(@PathVariable Integer id) {
        // 投稿をテーブルに格納
        reportService.deleteReport(id);
        // rootへリダイレクト
        return new ModelAndView("redirect:/");
    }

    @GetMapping("/edit/{id}")
    public ModelAndView editContent(@PathVariable Integer id) {
        ModelAndView mav = new ModelAndView();
        // 編集する投稿を取得
        ReportForm report = reportService.editReport(id);
        // 編集する投稿をセット
        mav.addObject("formModel", report);
        // 画面遷移先を指定
        mav.setViewName("/edit");
        return mav;
    }

    /*
     * 返信編集画面表示
     */
    @PutMapping("/update_comment/{id}")
    public ModelAndView updateContent (@PathVariable Integer id,
                                       @ModelAttribute("formModel") CommentForm comment) {
        // UrlParameterのidを更新するentityにセット
        comment.setId(id);
        // 編集した投稿を更新
        commentService.saveComment(comment);
        // rootへリダイレクト
        return new ModelAndView("redirect:/");
    }

    /*
     * 新規返信処理
     */
    @PostMapping("/add_comment/{id}")
    public ModelAndView addComment(@ModelAttribute("formModel") CommentForm commentForm,
                                   @PathVariable Integer id){
        // UrlParameterのidを更新するentityにセット
        commentForm.setReportId(id);
        // 投稿をテーブルに格納
        commentService.saveComment(commentForm);
        // rootへリダイレクト
        return new ModelAndView("redirect:/");
    }

    @GetMapping("/edit_comment/{id}")
    public ModelAndView editComment(@PathVariable Integer id) {
        ModelAndView mav = new ModelAndView();
        // 編集するコメントを取得
        CommentForm comment = commentService.editComment(id);
        // 編集するコメントをセット
        mav.addObject("formModel", comment);
        // 画面遷移先を指定
        mav.setViewName("/edit_comment");
        return mav;
    }

    /*
     * 投稿削除処理
     */
    @DeleteMapping("/delete_comment/{id}")
    public ModelAndView deleteComment(@PathVariable Integer id) {
        // 投稿をテーブルに格納
        commentService.deleteComment(id);
        // rootへリダイレクト
        return new ModelAndView("redirect:/");
    }
}
