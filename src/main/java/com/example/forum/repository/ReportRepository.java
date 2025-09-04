package com.example.forum.repository;

import com.example.forum.repository.entity.Report;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Integer> {
    public List<Report> findAllByOrderByUpdatedDateDesc();
    public List<Report> findByCreatedDateBetweenOrderByUpdatedDateDesc(Timestamp start, Timestamp end);

    @Modifying
    @Transactional
    @Query("UPDATE Report r SET r.updatedDate = :updatedDate WHERE r.id = :id")
    public void updateUpdatedDate(@Param("updatedDate")Timestamp ts, @Param("id")Integer id);
}
