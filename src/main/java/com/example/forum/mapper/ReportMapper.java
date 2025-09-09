package com.example.forum.mapper;

import com.example.forum.repository.entity.Report;
import org.apache.ibatis.annotations.Mapper;

import java.sql.Timestamp;
import java.util.List;

@Mapper
public interface ReportMapper {

    //全件取得(日付で絞込、updatedDateDesc)
    List<Report> getAll(Timestamp start, Timestamp end);

    //idで投稿を取得
    List<Report> getById(Integer id);

    //投稿登録
    void insertReport(Report report);

    //投稿編集
    void updateReport(Report report);

    //コメント時orコメント編集時に投稿のupdated_dateを更新
    void updateUpdatedDate(Report report);

    //投稿削除
    void deleteReport(Integer id);
}
