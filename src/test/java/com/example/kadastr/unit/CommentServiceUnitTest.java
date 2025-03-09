package com.example.kadastr.unit;

import com.example.kadastr.dao.CommentDAO;
import com.example.kadastr.dao.NewsDAO;
import com.example.kadastr.dto.CommentDto;
import com.example.kadastr.exception.AuthException;
import com.example.kadastr.exception.IllegalControlException;
import com.example.kadastr.exception.NoSuchIdException;
import com.example.kadastr.model.Comment;
import com.example.kadastr.model.News;
import com.example.kadastr.security.util.AuthHelper;
import com.example.kadastr.service.impl.CommentServiceImpl;
import com.example.kadastr.util.Mapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CommentServiceUnitTest {

    @Mock
    private CommentDAO commentDAO;

    @Mock
    private NewsDAO newsDAO;

    @Mock
    private Mapper<Comment, CommentDto> commentMapper;

    @Mock
    private AuthHelper authHelper;

    @InjectMocks
    private CommentServiceImpl commentService;

    private CommentDto commentDto;
    private Comment comment;
    private News news;
    private UUID commentId;
    private UUID newsId;

    @BeforeEach
    public void setUp() {
        commentId = UUID.randomUUID();
        newsId = UUID.randomUUID();
        commentDto = new CommentDto();
        commentDto.setText("Test comment");

        comment = new Comment();
        comment.setUuid(commentId);
        comment.setText("Test comment");

        news = new News();
        news.setUuid(newsId);
        Set<Comment> comments = new HashSet<>();
        comments.add(comment);
        news.setComments(comments);
    }

    @Test
    public void testGetCommentById_Success() throws NoSuchIdException {
        when(commentDAO.findById(commentId)).thenReturn(Optional.of(comment));
        when(commentMapper.mapToDto(comment)).thenReturn(commentDto);

        CommentDto result = commentService.getCommentById(commentId);

        assertNotNull(result);
        assertEquals(commentDto.getText(), result.getText());
    }

    @Test
    public void testGetCommentById_NotFound() {
        when(commentDAO.findById(commentId)).thenReturn(Optional.empty());

        assertThrows(NoSuchIdException.class, () -> commentService.getCommentById(commentId));
    }

    @Test
    public void testGetCommentsList_Success() {
        when(commentDAO.findAll()).thenReturn(Arrays.asList(comment));
        when(commentMapper.mapToDto(comment)).thenReturn(commentDto);

        List<CommentDto> result = commentService.getCommentsList();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(commentDto.getText(), result.get(0).getText());
    }

    @Test
    public void testCreateComment_Success() throws NoSuchIdException, AuthException {
        when(newsDAO.findById(newsId)).thenReturn(Optional.of(news));
        when(commentMapper.mapToModel(commentDto)).thenReturn(comment);
        when(authHelper.getCurrentUserUUID()).thenReturn(UUID.randomUUID());

        commentService.createComment(newsId, commentDto);

        verify(newsDAO, times(1)).save(news);
    }

    @Test
    public void testCreateComment_NewsNotFound() {
        when(newsDAO.findById(newsId)).thenReturn(Optional.empty());

        assertThrows(NoSuchIdException.class, () -> commentService.createComment(newsId, commentDto));
    }

    @Test
    public void testUpdateComment_Success() throws NoSuchIdException, IllegalControlException {
        when(commentDAO.findById(commentId)).thenReturn(Optional.of(comment));
        when(authHelper.checkUserControlOnEntity(comment)).thenReturn(true);

        commentService.updateComment(commentId, commentDto);

        verify(commentDAO, times(1)).save(comment);
    }

    @Test
    public void testDeleteComment_Success() throws NoSuchIdException, IllegalControlException {
        when(commentDAO.findById(commentId)).thenReturn(Optional.of(comment));
        when(authHelper.checkUserControlOnEntity(comment)).thenReturn(true);

        commentService.deleteComment(commentId);

        verify(commentDAO, times(1)).deleteById(commentId);
    }

}
