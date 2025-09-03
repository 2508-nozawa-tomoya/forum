package com.example.forum.service;

import com.example.forum.controller.form.CommentForm;
import com.example.forum.repository.CommentRepository;
import com.example.forum.repository.entity.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentService {
    @Autowired
    CommentRepository commentRepository;

    /*
     * コメント追加
     */
    public void saveComment(CommentForm commentForm){
        Comment saveComment = setCommentEntity(commentForm);
        commentRepository.save(saveComment);
    }

    /*
     *　返信全件取得
     */
    public List<CommentForm> findAllComment(){
        List<Comment> results = commentRepository.findAll();
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
            comments.add(comment);
        }
        return comments;
    }
}
