package com.example.forum.service;

import com.example.forum.controller.form.ReportForm;
import com.example.forum.repository.ReportRepository;
import com.example.forum.repository.entity.Report;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ReportService {
    @Autowired
    ReportRepository reportRepository;

    /*
     * レコード全件取得
     */
    public List<ReportForm> findAllReport(){
        List<Report> results = reportRepository.findAllByOrderByIdDesc();
        List<ReportForm> reports = setReportForm(results);
        return reports;
    }

    /*
     * 日付で絞込
     */
    public List<ReportForm> findAllReport(String startDate, String endDate){
        List<Report> results;
        Date date = new Date();
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String stringStart  = null;
        String stringEnd = null;
        if(!StringUtils.isEmpty(startDate)){
            stringStart = startDate + " 00:00:00";
        } else {
            stringStart = "2000-01-01 00:00:00";
        }
        Timestamp start = Timestamp.valueOf(stringStart);

        if(!StringUtils.isEmpty(endDate)){
            stringEnd = endDate + " 23:59:59";
        } else{
            stringEnd = formatter.format(date);
        }
        Timestamp end = Timestamp.valueOf(stringEnd);

            results = reportRepository.findByCreatedDateBetween(start, end);

        List<ReportForm> reports = setReportForm(results);
        return reports;
    }

    /*
     * レコード追加
     */
    public void saveReport(ReportForm reqReport){
        Report saveReport = setReportEntity(reqReport);
        reportRepository.save(saveReport);
    }

    /*
     *　投稿削除
     */
    public void deleteReport(Integer id){
        reportRepository.deleteById(id);
    }

    /*
     * idでレコードを取得
     */
    public ReportForm editReport(Integer id){
        List<Report> results = new ArrayList<>();
        results.add((Report)reportRepository.findById(id).orElse(null));
        List<ReportForm> reports = setReportForm(results);
        return reports.get(0);
    }

    /*
     * DBから取得したデータ（List<Report>に格納）をFormに設定
     */
    private List<ReportForm> setReportForm(List<Report> results){
        List<ReportForm> reports = new ArrayList<>();

        for(int i = 0; i < results.size(); i++){
            ReportForm report = new ReportForm();
            Report result = results.get(i);
            report.setId(result.getId());
            report.setContent(result.getContent());
            report.setCreatedDate(result.getCreatedDate());
            reports.add(report);
        }
        return reports;
    }

    /*
     * リクエストから取得した情報をEntityに設定
     */
    private Report setReportEntity(ReportForm reqReport){
        Report report = new Report();
        report.setId(reqReport.getId());
        report.setContent(reqReport.getContent());
        if(reqReport.getUpdatedDate() != null){
            report.setUpdatedDate(reqReport.getUpdatedDate());
        }
        return report;
    }
}
