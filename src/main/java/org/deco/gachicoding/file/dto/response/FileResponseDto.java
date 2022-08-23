package org.deco.gachicoding.file.dto.response;

import lombok.Getter;
import lombok.Setter;
import org.deco.gachicoding.file.domain.File;

@Getter
@Setter
public class FileResponseDto {
    private String filePath;

    public FileResponseDto(File file) {
        this.filePath = file.getFilePath();
    }
}
