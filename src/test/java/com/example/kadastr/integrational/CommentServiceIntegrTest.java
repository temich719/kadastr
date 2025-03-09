package com.example.kadastr.integrational;

import com.example.kadastr.config.TestConfig;
import com.example.kadastr.dao.CommentDAO;
import com.example.kadastr.dao.NewsDAO;
import com.example.kadastr.dto.CommentDto;
import com.example.kadastr.exception.AuthException;
import com.example.kadastr.exception.NoSuchIdException;
import com.example.kadastr.model.Comment;
import com.example.kadastr.model.News;
import com.example.kadastr.model.Role;
import com.example.kadastr.model.User;
import com.example.kadastr.service.CommentService;
import com.example.kadastr.util.Mapper;
import com.example.kadastr.util.impl.CommentMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
@ContextConfiguration(classes = TestConfig.class)
@ActiveProfiles("test")
public class CommentServiceIntegrTest {

    @Autowired
    private CommentService commentService;

    @Autowired
    private CommentDAO commentDAO;

    @Autowired
    private NewsDAO newsDAO;

    private UUID newsId;
    private UUID commentId;

    @BeforeEach
    void setUp() {
        News news = new News();
        news.setTitle("Test News");
        news.setText("This is a test news content.");
        newsDAO.save(news);
        newsId = news.getUuid();

        Comment comment = new Comment();
        comment.setText("Test Comment");
        comment.setNews(news);
        comment.setInsertedById(UUID.fromString("5cce18df-81a1-46b5-943e-f691dd59d806"));
        commentDAO.save(comment);
        commentId = comment.getUuid();

        User user = new User();
        Role role = new Role();
        role.setName("ROLE_ADMIN");
        user.setRole(role);
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                new User(), null, user.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(authToken);
    }

    @Test
    void testGetCommentById_Success() throws NoSuchIdException {
        CommentDto commentDto = commentService.getCommentById(commentId);
        assertNotNull(commentDto);
        assertEquals("Test Comment", commentDto.getText());
    }

    @Test
    void testGetCommentById_NotFound() {
        UUID fakeId = UUID.randomUUID();
        assertThrows(NoSuchIdException.class, () -> commentService.getCommentById(fakeId));
    }

    @Test
    void testGetCommentsList() {
        List<CommentDto> comments = commentService.getCommentsList();
        assertFalse(comments.isEmpty());
        assertEquals(201, comments.size());//200(10 per news) from init script and 1 now
    }

    @Test
    @Transactional
    void testCreateComment_Success() throws NoSuchIdException, AuthException {
        CommentDto newComment = new CommentDto();
        newComment.setText("New Comment for News");
        commentService.createComment(newsId, newComment);

        List<CommentDto> comments = commentService.getCommentsList();
        assertEquals(202, comments.size());
    }

    @Test
    void testCreateComment_NewsNotFound() {
        CommentDto newComment = new CommentDto();
        newComment.setText("Comment on non-existent news");

        UUID fakeNewsId = UUID.randomUUID();
        assertThrows(NoSuchIdException.class, () -> commentService.createComment(fakeNewsId, newComment));
    }

    @Test
    @Transactional
    void testUpdateComment_Success() {
        CommentDto updatedComment = new CommentDto();
        updatedComment.setText("Updated Comment");
        Mapper<Comment, CommentDto> commentCommentDtoMapper = new CommentMapper();
        Comment comment = commentCommentDtoMapper.mapToModel(updatedComment);
        comment.setUuid(commentId);
        commentDAO.save(comment);
        Optional<Comment> updated = commentDAO.findById(commentId);
        assertTrue(updated.isPresent());
        assertEquals("Updated Comment", updated.get().getText());
    }

    @Test
    void testUpdateComment_NotFound() {
        CommentDto updatedComment = new CommentDto();
        updatedComment.setText("Should not update");

        UUID fakeCommentId = UUID.randomUUID();
        assertThrows(NoSuchIdException.class, () -> commentService.updateComment(fakeCommentId, updatedComment));
    }

    @Test
    @Transactional
    void testDeleteComment_Success() {
        CommentDto updatedComment = new CommentDto();
        updatedComment.setText("Updated Comment");
        Mapper<Comment, CommentDto> commentCommentDtoMapper = new CommentMapper();
        Comment comment = commentCommentDtoMapper.mapToModel(updatedComment);
        comment.setUuid(commentId);
        commentDAO.save(comment);
        commentDAO.deleteById(commentId);
        assertFalse(commentDAO.findById(commentId).isPresent());
    }

    @Test
    void testDeleteComment_NotFound() {
        UUID fakeCommentId = UUID.randomUUID();
        assertThrows(NoSuchIdException.class, () -> commentService.deleteComment(fakeCommentId));
    }

}
