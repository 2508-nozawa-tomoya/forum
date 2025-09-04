package com.example.forum.repository;

import com.example.forum.repository.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Integer> {
    List<Report> findAllByOrderByUpdatedDateDesc();
    List<Report> findByCreatedDateBetweenOrderByUpdatedDateDesc(Timestamp start, Timestamp end);
}
