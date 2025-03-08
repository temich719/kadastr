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

    /**
     * finds news where text and title like input params
     * @param text text of news
     * @param title title of news
     * @return list of news where text and title like input params
     */
    @Query(value = "SELECT * FROM news WHERE \"text\" LIKE CONCAT('%', :text, '%') OR title LIKE CONCAT('%', :title, '%')", nativeQuery = true)
    List<News> findByTextAndTitle(@Param("text") String text, @Param("title") String title);

}
