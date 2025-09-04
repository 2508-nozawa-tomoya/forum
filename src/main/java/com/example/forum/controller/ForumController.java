package com.example.forum.controller;

import com.example.forum.controller.form.CommentForm;
import com.example.forum.controller.form.ReportForm;
import com.example.forum.service.CommentService;
import com.example.forum.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.sql.Timestamp;
import java.util.List;

@Controller
public class ForumController {
    @Autowired
    ReportService reportService;

    @Autowired
    CommentService commentService;

    /*
     *投稿内容表示処理
     */
    @GetMapping
    public ModelAndView top(){
        ModelAndView mav = new ModelAndView();
        //投稿を全件取得
        List<ReportForm> contentData = reportService.findAllReport();
        // 返信を全件取得
        List<CommentForm> comments = commentService.findAllComment();
        //画面遷移先を指定
        mav.setViewName("/top");
        //投稿データオブジェクトを保管
        mav.addObject("contents", contentData);
        //返信データオブジェクトを保管
        mav.addObject("comments", comments);

        // 返信用の空のFormを準備し保管
        CommentForm commentForm = new CommentForm();
        mav.addObject("formModel", commentForm);
        return mav;
    }

    /*
     * 日付で投稿の絞込をしてTop画面を表示
     */
    @GetMapping("/date")
    public ModelAndView sortTop(@RequestParam("start")String start, @RequestParam("end")String end){
        ModelAndView mav = new ModelAndView();
        //投稿を全件取得
        List<ReportForm> contentData = reportService.findAllReport(start, end);
        // 返信を全件取得
        List<CommentForm> comments = commentService.findAllComment();
        //画面遷移先を指定
        mav.setViewName("/top");
        //投稿データオブジェクトを保管
        mav.addObject("contents", contentData);
        //返信データオブジェクトを保管
        mav.addObject("comments", comments);

        //開始日時を保管
        mav.addObject("start", start);
        //終了日時を保管
        mav.addObject("end", end);

        // 返信用の空のFormを準備し保管
        CommentForm commentForm = new CommentForm();
        mav.addObject("formModel", commentForm);
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
    public ModelAndView addContent(@ModelAttribute("formModel") @Validated ReportForm reportForm, BindingResult result){
       if(result.hasErrors()){
           return new ModelAndView("redirect:/new");
       }
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
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        reportForm.setId(id);
        reportForm.setUpdatedDate(ts);
        reportService.saveReport(reportForm);
        return new ModelAndView("redirect:/");
    }

    /*
     * 返信機能
     */
    @PostMapping("/comment/{reportId}")
    public ModelAndView addComment(@ModelAttribute("formModel") CommentForm commentForm, @PathVariable Integer reportId){
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        commentForm.setReportId(reportId);
        commentService.saveComment(commentForm, ts);
        return  new ModelAndView("redirect:/");
    }

    /*
     * 返信編集画面表示
     */
    @GetMapping("/comment/edit/{id}")
    public ModelAndView editComment(@PathVariable Integer id){
        ModelAndView mav = new ModelAndView();

        //idでコメント情報を取得
        CommentForm commentForm = commentService.editComment(id);

        mav.setViewName("/edit-comment");
        mav.addObject("formModel", commentForm);

        return mav;
    }

    /*
     * 返信編集処理
     */
    @PutMapping("/comment/update/{id}")
    public ModelAndView updateComment(@ModelAttribute("formModel") CommentForm commentForm, @PathVariable Integer id) {
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        commentForm.setId(id);
        commentForm.setUpdatedDate(ts);
        commentService.saveComment(commentForm, ts);
        return new ModelAndView("redirect:/");
    }

    /*
     * 返信削除機能
     */
    @DeleteMapping("/comment/delete/{id}")
    public ModelAndView deleteComment(@PathVariable Integer id){
        commentService.deleteComment(id);
        return  new ModelAndView("redirect:/");
    }
}
