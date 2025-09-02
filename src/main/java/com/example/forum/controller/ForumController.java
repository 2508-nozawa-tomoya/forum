package com.example.forum.controller;

import com.example.forum.controller.form.ReportForm;
import com.example.forum.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class ForumController {
    @Autowired
    ReportService reportService;

    /*
     *投稿内容表示処理
     */
    @GetMapping
    public ModelAndView top(){
        ModelAndView mav = new ModelAndView();
        //投稿を全件取得
        List<ReportForm> contentData = reportService.findAllReport();
        //画面遷移先を指定
        mav.setViewName("/top");
        //投稿データオブジェクトを保管
        mav.addObject("contents", contentData);
        return mav;
    }

    /*
     *新規投稿画面表示
     */
    @GetMapping("/new")
    public ModelAndView newContent(){
        ModelAndView mav = new ModelAndView();
        //form用のからのentityを用意
        ReportForm reportForm = new ReportForm();
        //画面遷移先を指定
        mav.setViewName("/new");
        //準備した空のFormを保管
        mav.addObject("formModel", reportForm);
        return mav;
    }

    /*
     * 新規投稿処理
     */
    @PostMapping("/add")
    public ModelAndView addContent(@ModelAttribute("formModel") ReportForm reportForm){
        //投稿テーブルに格納
        reportService.saveReport(reportForm);
        //rootへリダイレクト
        return new ModelAndView("redirect:/");
    }

    /*
     * 投稿の削除
     */
    @PostMapping("/delete/{id}")
    public ModelAndView deleteContent(@PathVariable Integer id){
        reportService.deleteReport(id);
        return new ModelAndView("redirect:/");
    }

    /*
     * 投稿編集画面表示
     */
    @GetMapping("/edit/{id}")
    public ModelAndView editContent(@PathVariable Integer id) {
        ModelAndView mav = new ModelAndView();

        //idでレコードを取得
        ReportForm reportForm = reportService.editReport(id);

        mav.setViewName("/edit");
        mav.addObject("formModel", reportForm);

        return mav;
    }

    /*
     * 投稿編集処理
     */
    @PutMapping("/update/{id}")
    public ModelAndView updateContent(@ModelAttribute("formModel") ReportForm reportForm, @PathVariable Integer id){
        reportForm.setId(id);
        reportService.saveReport(reportForm);
        return new ModelAndView("redirect:/");
    }
}
