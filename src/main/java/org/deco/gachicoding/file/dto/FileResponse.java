package org.deco.gachicoding.file.dto;

import org.deco.gachicoding.file.dto.response.FileResponseDto;

import java.util.List;

public interface FileResponse {
    void setFiles(List<FileResponseDto> files);
}
