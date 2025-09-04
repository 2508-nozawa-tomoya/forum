package com.example.forum.service;

import com.example.forum.controller.form.CommentForm;
import com.example.forum.repository.CommentRepository;
import com.example.forum.repository.ReportRepository;
import com.example.forum.repository.entity.Comment;
import com.example.forum.repository.entity.Report;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
public class CommentService {
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    ReportRepository reportRepository;

    /*
     * コメント追加
     */
    public void saveComment(CommentForm commentForm, Timestamp ts){
//        回りくどいやり方
//        List<Report> results = new ArrayList<>();
//        results.add((Report)reportRepository.findById(commentForm.getReportId()).orElse(null));
//        Report report = results.get(0);
//        report.setUpdatedDate(ts);
//        reportRepository.save(report);

        //JPQLを使ったやり方
        reportRepository.updateUpdatedDate(ts, commentForm.getReportId());

        Comment saveComment = setCommentEntity(commentForm);
        commentRepository.save(saveComment);
    }

    /*
     *　返信全件取得
     */
    public List<CommentForm> findAllComment(){
        List<Comment> results = commentRepository.findAllByOrderByUpdatedDateDesc();
        List<CommentForm> commnets = setCommentForm(results);
        return commnets;
    }

    /*
     * idで返信情報を取得
     */
    public CommentForm editComment(Integer id){
        List<Comment> results = new ArrayList<>();
        results.add((Comment)commentRepository.findById(id).orElse(null));
        List<CommentForm> comments = setCommentForm(results);
        return comments.get(0);
    }

    /*
     * コメント削除
     */
    public void deleteComment(Integer id){
        commentRepository.deleteById(id);
    }

    /*
     * リクエストから取得した情報（Form）をEntityに格納（詰め替え）
     */
    private Comment setCommentEntity(CommentForm commentForm){
        Comment comment = new Comment();
        comment.setId(commentForm.getId());
        comment.setContent(commentForm.getContent());
        comment.setReportId(commentForm.getReportId());
        if(commentForm.getUpdatedDate() != null){
            comment.setUpdatedDate(commentForm.getUpdatedDate());
        }
        return comment;
    }

    /*
     * DBから取得した情報（Entity）をFormに詰め替え
     */
    private List<CommentForm> setCommentForm(List<Comment> results){
        List<CommentForm> comments = new ArrayList<>();

        for (Comment result : results) {
            CommentForm comment = new CommentForm();
            comment.setId(result.getId());
            comment.setContent(result.getContent());
            comment.setReportId(result.getReportId());
            comment.setCreatedDate(result.getCreatedDate());
            comment.setUpdatedDate(result.getUpdatedDate());
            comments.add(comment);
        }
        return comments;
    }
}
