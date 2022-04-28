package org.deco.gachicoding.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.deco.gachicoding.dto.answer.AnswerResponseDto;
import org.deco.gachicoding.dto.answer.AnswerSaveRequestDto;
import org.deco.gachicoding.dto.answer.AnswerUpdateRequestDto;
import org.deco.gachicoding.service.answer.AnswerService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@Api
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RestAnswerController {
    private final AnswerService answerService;

    @ApiOperation(value = "답변 리스트")
    @GetMapping("/answer/list/{page}")
    public Page<AnswerResponseDto> getAnswerListByKeyword(@RequestParam(value = "keyword", defaultValue = "") String keyword, @PathVariable int page){
        return answerService.getAnswerListByKeyword(keyword, page);
    }

    @ApiOperation(value = "답변 디테일")
    @GetMapping("/answer/detail/{answerIdx}")
    public AnswerResponseDto getAnswerDetailById(@PathVariable Long answerIdx){
        return answerService.getAnswerDetailById(answerIdx);
    }

    @ApiOperation(value = "답변 등록")
    @PostMapping("/answer")
    public Long registerAnswer(@RequestBody AnswerSaveRequestDto dto){
        return answerService.registerAnswer(dto);
    }

    @ApiOperation(value = "답변 수정")
    @PutMapping("/answer/modify/{answerIdx}")
    public AnswerResponseDto modifyAnswerById(@PathVariable Long answerIdx, @RequestBody AnswerUpdateRequestDto dto){
        return answerService.modifyAnswerById(answerIdx, dto);
    }

    @ApiOperation(value = "답변 비활성화")
    @PutMapping("/answer/disable/{answerIdx}")
    public void disableAnswer(@PathVariable Long answerIdx){
        answerService.disableAnswer(answerIdx);
    }

    @ApiOperation(value = "답변 활성화")
    @PutMapping("/answer/enable/{answerIdx}")
    public void enableAnswer(@PathVariable Long answerIdx){
        answerService.enableAnswer(answerIdx);
    }

    @ApiOperation(value = "답변 삭제")
    @DeleteMapping("/answer/remove/{answerIdx}")
    public void removeAnswer(@PathVariable Long answerIdx){
        answerService.removeAnswer(answerIdx);
    }
}
