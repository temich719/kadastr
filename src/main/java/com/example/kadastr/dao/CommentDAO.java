package com.example.kadastr.dao;

import com.example.kadastr.model.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CommentDAO extends JpaRepository<Comment, UUID> {

    /**
     * find comments that attached to news
     * @param idNews id of news
     * @param pageable param for pagination(page number and size)
     * @return page with comments that attached to news with id = idNews
     */
    Page<Comment> findCommentByIdNews(UUID idNews, Pageable pageable);

}
