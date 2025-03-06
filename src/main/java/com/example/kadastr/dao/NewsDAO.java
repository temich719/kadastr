package com.example.kadastr.dao;

import com.example.kadastr.model.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface NewsDAO extends JpaRepository<News, UUID> {

    @Query(value = "SELECT * FROM news WHERE \"text\" LIKE CONCAT('%', :text, '%') OR title LIKE CONCAT('%', :title, '%')", nativeQuery = true)
    List<News> findByTextAndTitle(@Param("text") String text, @Param("title") String title);

}
