package org.deco.gachicoding.file.domain.vo;

import org.hibernate.annotations.Comment;
import org.hibernate.validator.constraints.UniqueElements;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class FilePath {

    private static final String PATH_FORMAT = "%s/%d/%s";

    protected FilePath() {}

    @Comment("저장된 파일 url")
    @Column(name = "file_path", columnDefinition = "TEXT", unique = true, nullable = false)
    private String filePath;

    // 유효성 검증을 어떤 걸 해야 할지 모르겠다...
    public FilePath(String articleCategory, Long articleIdx, SaveFileInfo saveFileInfo) {
        filePath = String.format(
                PATH_FORMAT,
                articleCategory,
                articleIdx,
                saveFileInfo.getSaveFilename()
        );
    }

    public String getFilePath() {
        return filePath;
    }

    // 메서드 이름 컨벤션
    public boolean isEquals(String path) {
        return filePath.equals(path);
    }
}
