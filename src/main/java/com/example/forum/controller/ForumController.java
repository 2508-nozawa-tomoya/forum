package com.example.forum.controller;


import com.example.forum.controller.form.CommentForm;
import com.example.forum.controller.form.ReportForm;
import com.example.forum.service.CommentService;
import com.example.forum.service.ReportService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public ModelAndView top(HttpServletRequest request){

        String startDate = request.getParameter("start");
        String endDate = request.getParameter("end");

        ModelAndView mav = new ModelAndView();
        //投稿を全件取得
        List<ReportForm> contentData = reportService.findAllReport(startDate, endDate);
        // 返信を全件取得
        List<CommentForm> comments = commentService.findAllComment();
        //画面遷移先を指定
        mav.setViewName("/top");
        //投稿データオブジェクトを保管
        mav.addObject("contents", contentData);
        //返信データオブジェクトを保管
        mav.addObject("comments", comments);

        mav.addObject("start", startDate);
        mav.addObject("end", endDate);

        // 返信用の空のFormを準備し保管
        CommentForm commentForm = new CommentForm();
        mav.addObject("formModel", commentForm);
        return mav;
    }

    /*
     *新規投稿画面表示
     */
    @GetMapping("/new")
    public ModelAndView newContent(Model model){

        ModelAndView mav = new ModelAndView();
        //form用のからのentityを用意
        if(!model.containsAttribute("formModel")){
            ReportForm reportForm = new ReportForm();
            mav.addObject("formModel", reportForm);
        }
        //画面遷移先を指定
        mav.setViewName("/new");
        //準備した空のFormを保管

        return mav;
    }

    /*
     * 新規投稿処理
     */
    @PostMapping("/add")
    public ModelAndView addContent(@ModelAttribute("formModel") @Validated ReportForm reportForm, BindingResult result, RedirectAttributes redirectAttributes){
       if(result.hasErrors()){
//           bindingresultからメッセージを取り出す方法
//           String errorMessage = "";
//           for(ObjectError error : result.getAllErrors()){
//               errorMessage += error.getDefaultMessage();
//           }
//           redirectAttributes.addFlashAttribute("errorMessage", errorMessage);

           //bindingresultをそのままリダイレクト先へ渡す方法
           redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.formModel", result);
           redirectAttributes.addFlashAttribute("formModel", reportForm);
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
    public ModelAndView updateContent(@ModelAttribute("formModel") @Validated ReportForm reportForm, BindingResult result, @PathVariable Integer id, RedirectAttributes redirectAttributes){
        if(result.hasErrors()){
            String errorMessage = "";
            for(ObjectError error : result.getAllErrors()){
                errorMessage += error.getDefaultMessage();
            }
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            return new ModelAndView("redirect:/edit/{id}");
        }

        Timestamp ts = new Timestamp(System.currentTimeMillis());
        reportForm.setId(id);
        reportForm.setUpdatedDate(ts);
        reportService.updateReport(reportForm);
        return new ModelAndView("redirect:/");
    }

    /*
     * 返信機能
     */
    @PostMapping("/comment/{reportId}")
    public ModelAndView addComment(@ModelAttribute("formModel") @Validated CommentForm commentForm, BindingResult result, @PathVariable Integer reportId, RedirectAttributes redirectAttributes){
        if(result.hasErrors()){
            String errorMessage = "";
            for(ObjectError error : result.getAllErrors()){
                errorMessage += error.getDefaultMessage();
            }
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            redirectAttributes.addFlashAttribute("errorId", reportId);
            return new ModelAndView("redirect:/");
        }
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
    public ModelAndView updateComment(@ModelAttribute("formModel") @Validated CommentForm commentForm, BindingResult result, @PathVariable Integer id, RedirectAttributes redirectAttributes) {
        if(result.hasErrors()){
            String errorMessage = "";
            for(ObjectError error : result.getAllErrors()){
                errorMessage += error.getDefaultMessage();
            }
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            return  new ModelAndView("redirect:/comment/edit/{id}");
        }
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        commentForm.setId(id);
        commentForm.setUpdatedDate(ts);
        commentService.updateComment(commentForm, ts);
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
