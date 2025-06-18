package com.example.forum.service;

import com.example.forum.controller.form.ReportForm;
import com.example.forum.repository.ReportRepository;
import com.example.forum.repository.entity.Report;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.ParseException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ReportService {
    @Autowired
    ReportRepository reportRepository;

    /*
     * レコード全件取得処理
     */
    public List<ReportForm> findAllReport() {
        List<Report> results = reportRepository.findAllByOrderByUpdateDateDesc();
        List<ReportForm> reports = setReportForm(results);
        return reports;
    }

    /*
     * レコード日付で全件取得処理 ★明日修正：TIMESTAMP型に直して再度確認
     */
    public List<ReportForm> findDaysReport(String start,String end) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Timestamp tsStart = null;
        Timestamp tsEnd = null;
        if(!start.isEmpty()) {
            Date date = sdf.parse(start + " 00:00:00");
            tsStart = new Timestamp(date.getTime());
        }else {
            Date date = sdf.parse("2000-01-01 00:00:00");
            tsStart = new Timestamp(date.getTime());
        }
        if(!end.isEmpty()) {
            Date date = sdf.parse(end + " 23:59:59");
            tsEnd = new Timestamp(date.getTime());
        }else {
            Date date = sdf.parse("2300-12-31 23:59:59");
            tsEnd = new Timestamp(date.getTime());
        }
        List<Report> results = reportRepository.findAllByUpdateDateBetweenOrderByUpdateDateDesc(tsStart, tsEnd);
        List<ReportForm> reports = setReportForm(results);
        return reports;
    }

    /*
     * DBから取得したデータをFormに設定
     */
    private List<ReportForm> setReportForm(List<Report> results) {
        List<ReportForm> reports = new ArrayList<>();

        for (int i = 0; i < results.size(); i++) {
            ReportForm report = new ReportForm();
            Report result = results.get(i);
            report.setId(result.getId());
            report.setContent(result.getContent());
            reports.add(report);
        }
        return reports;
    }

    /*
     * レコード追加
     */
    public void saveReport(ReportForm reqReport) {
        Report saveReport = setReportEntity(reqReport);
        reportRepository.save(saveReport);
    }

    /*
     * リクエストから取得した情報をEntityに設定
     */
    private Report setReportEntity(ReportForm reqReport) {
        Report report = new Report();
        report.setId(reqReport.getId());
        report.setUpdateDate(reqReport.getUpdateDate());
        report.setContent(reqReport.getContent());
        return report;
    }

    /*
     * レコード削除
     */
    public void deleteReport(int id) {
        Integer delId = id;
        reportRepository.deleteById(delId);
    }

    /*
     * 指定レコード取得処理
     */
    public ReportForm editReport(Integer id) {
        Optional<Report> results = reportRepository.findById(id);
        if(results.isEmpty()) {
            return null;
        }
        Report result = results.get();
        ReportForm report = setReportForm(result);
        return report;
    }

    /*
     * DBから取得したデータをFormに設定
     */
    private ReportForm setReportForm(Report result) {
            ReportForm report = new ReportForm();
            report.setId(result.getId());
            report.setContent(result.getContent());

        return report;
    }
}

