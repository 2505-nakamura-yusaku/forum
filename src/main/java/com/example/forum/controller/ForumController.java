package com.example.forum.controller;

import com.example.forum.controller.form.CommentForm;
import com.example.forum.controller.form.ReportForm;
import com.example.forum.repository.entity.Report;
import com.example.forum.service.CommentService;
import com.example.forum.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.sql.Timestamp;
import java.text.ParseException;
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
    public ModelAndView top(@ModelAttribute("start") String start,
                            @ModelAttribute("end") String end,
                            @ModelAttribute("errorMessages") String errorMassages) throws ParseException {
        ModelAndView mav = new ModelAndView();
        List<ReportForm> contentData = null;
        if(start.isBlank() && end.isBlank()) {
            // 投稿を全件取得
            contentData = reportService.findAllReport();
        }else {
            // 投稿を絞り込んで取得
            contentData = reportService.findDaysReport(start, end);
            if(!start.isEmpty()) {
                mav.addObject("start", start);
            }
            if(!end.isEmpty()) {
                mav.addObject("end", end);
            }
        }
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
        mav.addObject("errorMessages", errorMassages);
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
    public ModelAndView addContent(@ModelAttribute("formModel") @Validated ReportForm reportForm, BindingResult result){
        if(result.hasErrors()) {
            //エラー処理
            ModelAndView mav = new ModelAndView();
            //新規投稿の画面に戻る
            mav.setViewName("/new");
            //引数のレポートをそのまま戻す
            mav.addObject("formModel", reportForm);
            return mav;
        }

        // 投稿をテーブルに格納
        reportService.saveReport(reportForm);
        // rootへリダイレクト
        return new ModelAndView("redirect:/");
    }

    /*
     * 投稿編集画面表示
     */
    @PutMapping("/update/{id}")
    public ModelAndView updateContent (@PathVariable Integer id,
                                       @ModelAttribute("formModel") @Validated ReportForm report,BindingResult result){
        if(result.hasErrors()) {
            //エラー処理
            ModelAndView mav = new ModelAndView();
            //新規投稿の画面に戻る
            mav.setViewName("/edit");
            //引数のレポートをそのまま戻す
            mav.addObject("formModel", report);
            return mav;
        }

        // UrlParameterのidを更新するentityにセット
        report.setId(id);

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        report.setUpdateDate(timestamp);

        // 編集した投稿を更新
        reportService.saveReport(report);
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
                                       @ModelAttribute("formModel") @Validated CommentForm comment,BindingResult result) {
        if(result.hasErrors()) {
            //エラー処理
            ModelAndView mav = new ModelAndView();
            //新規投稿の画面に戻る
            mav.setViewName("/edit_comment");
            //引数のレポートをそのまま戻す
            mav.addObject("formModel", comment);
            return mav;
        }

        // UrlParameterのidを更新するentityにセット
        comment.setId(id);

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        comment.setUpdateDate(timestamp);

        // 編集したコメントを更新
        commentService.saveComment(comment);

        // 更新に合わせて投稿の更新時間を更新
        ReportForm report = reportService.editReport(comment.getReportId());
        report.setUpdateDate(timestamp);
        reportService.saveReport(report);

        // rootへリダイレクト
        return new ModelAndView("redirect:/");
    }

    /*
     * 新規返信処理
     */
    @PostMapping("/add_comment/{id}")
    public ModelAndView addComment(@ModelAttribute("formModel") @Validated CommentForm commentForm,
                                   BindingResult result,
                                   @PathVariable Integer id){
        if(result.hasErrors()) {
            //エラー処理
            ModelAndView mav = new ModelAndView();
            //新規投稿の画面に戻る
            mav.setViewName("redirect:/");
            //引数のレポートをそのまま戻す
            mav.addObject("formModel", commentForm);

            String errorMessages = "";
            for (ObjectError error : result.getAllErrors()) {
                // ここでメッセージを取得する。
                errorMessages += error.getDefaultMessage();
            }
            mav.addObject("errorMessages", errorMessages);
            return mav;
        }

        // UrlParameterのidを更新するentityにセット
        commentForm.setReportId(id);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        commentForm.setUpdateDate(timestamp);
        // 投稿をテーブルに格納
        commentService.saveComment(commentForm);

        // 更新に合わせて投稿の更新時間を更新
        ReportForm report = reportService.editReport(commentForm.getReportId());
        report.setUpdateDate(timestamp);
        reportService.saveReport(report);

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
