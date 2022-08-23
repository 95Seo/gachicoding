package org.deco.gachicoding.file.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.deco.gachicoding.file.domain.File;

@Getter
@Setter
@NoArgsConstructor
public class FileSaveRequestDto {

    private Long articleIdx;
    private String articleCategory;
    private String originFilename;
    private String saveFilename;
    private String fileExt;
    private Long fileSize;
    private String filePath;

    @Builder
    public FileSaveRequestDto(Long articleIdx, String articleCategory, String originFilename, String saveFilename, String fileExt, Long fileSize, String filePath) {
        this.articleIdx = articleIdx;
        this.articleCategory = articleCategory;
        this.originFilename = originFilename;
        this.saveFilename = saveFilename;
        this.fileExt = fileExt;
        this.fileSize = fileSize;
        this.filePath = filePath;
    }

    public File toEntity() {
        return File.builder()
                .articleIdx(articleIdx)
                .articleCategory(articleCategory)
                .originFilename(originFilename)
                .saveFilename(saveFilename)
                .fileExt(fileExt)
                .fileSize(fileSize)
                .filePath(filePath)
                .build();
    }
}
