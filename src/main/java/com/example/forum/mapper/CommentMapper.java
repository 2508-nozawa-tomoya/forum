package com.example.forum.mapper;

import com.example.forum.repository.CommentRepository;
import com.example.forum.repository.entity.Comment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface CommentMapper {
    //全件取得（updatedDateDesc）
    List<Comment> getAll();

    //idでコメントを取得
    List<Comment> getById(Integer id);

    //コメント登録
    void insertComment(Comment comment);

    //コメント編集
    void updateComment(Comment comment);

    //コメント削除
    void deleteComment(Integer id);
}
