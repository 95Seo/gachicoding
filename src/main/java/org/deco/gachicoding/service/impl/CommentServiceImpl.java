package org.deco.gachicoding.service.impl;

import lombok.RequiredArgsConstructor;
import org.deco.gachicoding.domain.board.Board;
import org.deco.gachicoding.domain.comment.Comment;
import org.deco.gachicoding.domain.comment.CommentRepository;
import org.deco.gachicoding.domain.user.User;
import org.deco.gachicoding.domain.user.UserRepository;
import org.deco.gachicoding.dto.board.BoardResponseDto;
import org.deco.gachicoding.dto.board.BoardUpdateRequestDto;
import org.deco.gachicoding.dto.comment.CommentResponseDto;
import org.deco.gachicoding.dto.comment.CommentSaveRequestDto;
import org.deco.gachicoding.dto.comment.CommentUpdateRequestDto;
import org.deco.gachicoding.dto.response.CustomException;
import org.deco.gachicoding.dto.response.ResponseState;
import org.deco.gachicoding.service.CommentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.deco.gachicoding.dto.response.StatusEnum.*;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;


    @Override
    public Long registerComment(CommentSaveRequestDto dto) {
        User writer = userRepository.findByUserEmail(dto.getUserEmail())
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        return commentRepository.save(dto.toEntity(writer)).getCommIdx();
    }

    @Override
    public Page<CommentResponseDto> getCommentList(String articleCategory, Long articleIdx, Pageable pageable) {
        Page<Comment> comments = commentRepository.findByArticleCategoryAndArticleIdx(articleCategory, articleIdx, pageable);

        Page<CommentResponseDto> commentList = comments.map(
                entity -> new CommentResponseDto(entity)
        );

        return commentList;
    }

    @Override
    @Transactional
    public CommentResponseDto modifyComment(CommentUpdateRequestDto dto) {
        Comment comment = commentRepository.findById(dto.getCommIdx())
                .orElseThrow(() -> new CustomException(DATA_NOT_EXIST));

        User user = userRepository.findByUserEmail(dto.getUserEmail())
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        if (!isSameWriter(comment, user)) {
            throw new CustomException(INVALID_AUTH_USER);
        }

        comment = comment.update(dto.getCommContent());

        CommentResponseDto commentDetail = CommentResponseDto.builder()
                .comment(comment)
                .build();

        return commentDetail;
    }

    @Override
    @Transactional
    public ResponseEntity<ResponseState> disableComment(Long commentIdx) {
        Comment comment = commentRepository.findById(commentIdx)
                .orElseThrow(() -> new CustomException(DATA_NOT_EXIST));

        comment.disableBoard();

        return ResponseState.toResponseEntity(DISABLE_SUCCESS);
    }

    @Override
    @Transactional
    public ResponseEntity<ResponseState> enableComment(Long commentIdx) {
        Comment comment = commentRepository.findById(commentIdx)
                .orElseThrow(() -> new CustomException(DATA_NOT_EXIST));

        comment.enableBoard();

        return ResponseState.toResponseEntity(ENABLE_SUCCESS);
    }

    @Override
    @Transactional
    public ResponseEntity<ResponseState> removeComment(Long commentIdx) {
        Comment comment = commentRepository.findById(commentIdx)
                .orElseThrow(() -> new CustomException(DATA_NOT_EXIST));

        commentRepository.delete(comment);

        return ResponseState.toResponseEntity(REMOVE_SUCCESS);
    }

    private Boolean isSameWriter(Comment comment, User user) {
        String writerEmail = comment.getWriter().getUserEmail();
        String userEmail = user.getUserEmail();

        return (writerEmail.equals(userEmail)) ? true : false;
    }
}
