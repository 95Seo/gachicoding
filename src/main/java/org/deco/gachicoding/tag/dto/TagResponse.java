package org.deco.gachicoding.tag.dto;

import org.deco.gachicoding.tag.dto.response.TagResponseDto;

import java.util.List;

public interface TagResponse {
    void setTags(List<TagResponseDto> tags);
}
