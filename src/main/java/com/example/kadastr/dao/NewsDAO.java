package com.example.kadastr.dao;

import com.example.kadastr.model.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface NewsDAO extends JpaRepository<News, UUID> {

    List<News> findByTextAndTitle(String text, String title);

}
