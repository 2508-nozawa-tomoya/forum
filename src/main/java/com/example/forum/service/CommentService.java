package com.example.forum.service;

import com.example.forum.controller.form.CommentForm;
import com.example.forum.mapper.CommentMapper;
import com.example.forum.mapper.ReportMapper;
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
    CommentMapper commentMapper;
    @Autowired
    ReportMapper reportMapper;

    /*
     * コメント追加 コメント追加はMybatis移行、投稿のupdatedDateはまだJPA
     */
    public void saveComment(CommentForm commentForm, Timestamp ts){
//        回りくどいやり方
//        List<Report> results = new ArrayList<>();
//        results.add((Report)reportRepository.findById(commentForm.getReportId()).orElse(null));
//        Report report = results.get(0);
//        report.setUpdatedDate(ts);
//        reportRepository.save(report);

//        JPQLを使ったやり方
//        reportRepository.updateUpdatedDate(ts, commentForm.getReportId());

        Report report = new Report();
        report.setId(commentForm.getReportId());
        report.setUpdatedDate(ts);
        reportMapper.updateUpdatedDate(report);

        Comment saveComment = setCommentEntity(commentForm);
        commentMapper.insertComment(saveComment);
    }

    /*
     *　返信全件取得 Mybatisへ移行済み
     */
    public List<CommentForm> findAllComment(){
        List<Comment> results = commentMapper.getAll();
        List<CommentForm> commnets = setCommentForm(results);
        return commnets;
    }

    /*
     * idで返信情報を取得(コメント編集画面表示用) MyBatisへ移行済み
     */
    public CommentForm editComment(Integer id){
        List<Comment> results = commentMapper.getById(id);
        List<CommentForm> comments = setCommentForm(results);
        return comments.get(0);
    }

    /*
     *　コメント編集 MyBatisへ移行済み　投稿のupdatedDateはまだJPA
     */
    public void updateComment(CommentForm commentForm, Timestamp ts){

        Report report = new Report();
        report.setId(commentForm.getReportId());
        report.setUpdatedDate(ts);
        reportMapper.updateUpdatedDate(report);

        Comment saveComment = setCommentEntity(commentForm);
        commentMapper.updateComment(saveComment);

    }

    /*
     * コメント削除 MyBatis移行済み
     */
    public void deleteComment(Integer id){
        commentMapper.deleteComment(id);
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
