package org.deco.gachicoding.comment.application;

import lombok.RequiredArgsConstructor;
import org.deco.gachicoding.comment.domain.Comment;
import org.deco.gachicoding.comment.domain.repository.CommentRepository;
import org.deco.gachicoding.user.domain.User;
import org.deco.gachicoding.user.domain.repository.UserRepository;
import org.deco.gachicoding.comment.dto.response.CommentResponseDto;
import org.deco.gachicoding.comment.dto.request.CommentSaveRequestDto;
import org.deco.gachicoding.comment.dto.request.CommentUpdateRequestDto;
import org.deco.gachicoding.exception.ApplicationException;
import org.deco.gachicoding.exception.ResponseState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.deco.gachicoding.exception.StatusEnum.*;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    public Long registerComment(CommentSaveRequestDto dto) {
        User writer = userRepository.findByUserEmail(dto.getUserEmail())
                .orElseThrow(() -> new ApplicationException(USER_NOT_FOUND));

        return commentRepository.save(dto.toEntity(writer)).getCommIdx();
    }

    public Page<CommentResponseDto> getCommentList(String articleCategory, Long articleIdx, Pageable pageable) {
        Page<Comment> comments = commentRepository.findByArticleCategoryAndArticleIdx(articleCategory, articleIdx, pageable);

        Page<CommentResponseDto> commentList = comments.map(
                entity -> new CommentResponseDto(entity)
        );

        return commentList;
    }

    @Transactional
    public CommentResponseDto modifyComment(CommentUpdateRequestDto dto) {
        Comment comment = commentRepository.findById(dto.getCommIdx())
                .orElseThrow(() -> new ApplicationException(DATA_NOT_EXIST));

        User user = userRepository.findByUserEmail(dto.getUserEmail())
                .orElseThrow(() -> new ApplicationException(USER_NOT_FOUND));

        if (!isSameWriter(comment, user)) {
            throw new ApplicationException(INVALID_AUTH_USER);
        }

        comment = comment.update(dto.getCommContent());

        CommentResponseDto commentDetail = CommentResponseDto.builder()
                .comment(comment)
                .build();

        return commentDetail;
    }

    @Transactional
    public ResponseEntity<ResponseState> disableComment(Long commentIdx) {
        Comment comment = commentRepository.findById(commentIdx)
                .orElseThrow(() -> new ApplicationException(DATA_NOT_EXIST));

        comment.disableBoard();

        return ResponseState.toResponseEntity(DISABLE_SUCCESS);
    }

    @Transactional
    public ResponseEntity<ResponseState> enableComment(Long commentIdx) {
        Comment comment = commentRepository.findById(commentIdx)
                .orElseThrow(() -> new ApplicationException(DATA_NOT_EXIST));

        comment.enableBoard();

        return ResponseState.toResponseEntity(ENABLE_SUCCESS);
    }

    @Transactional
    public ResponseEntity<ResponseState> removeComment(Long commentIdx) {
        Comment comment = commentRepository.findById(commentIdx)
                .orElseThrow(() -> new ApplicationException(DATA_NOT_EXIST));

        commentRepository.delete(comment);

        return ResponseState.toResponseEntity(REMOVE_SUCCESS);
    }

    private Boolean isSameWriter(Comment comment, User user) {
        String writerEmail = comment.getWriter().getUserEmail();
        String userEmail = user.getUserEmail();

        return (writerEmail.equals(userEmail)) ? true : false;
    }
}
