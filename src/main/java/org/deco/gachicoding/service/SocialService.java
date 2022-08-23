package org.deco.gachicoding.service;

import lombok.RequiredArgsConstructor;
import org.deco.gachicoding.domain.social.Social;
import org.deco.gachicoding.domain.social.SocialRepository;
import org.deco.gachicoding.dto.social.SocialSaveRequestDto;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SocialService {

    private final SocialRepository socialAuthRepository;

    // 소셜 인증 테이블 input
    public Long registerSocial(SocialSaveRequestDto dto) {
        Long idx = socialAuthRepository.save(dto.toEntity()).getSocialIdx();

        return idx;
    }

    // SocialType(kakao, google, github) SocialId(Email)로 회원 검색
    public Optional<Social> getSocialTypeAndEmail(SocialSaveRequestDto dto) {
        return socialAuthRepository.findBySocialTypeAndSocialId(dto.getSocialType(), dto.getSocialId());
    }
}
