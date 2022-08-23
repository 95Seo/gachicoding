package org.deco.gachicoding.file.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
@Table(name = "file")
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fileIdx;

    private Long articleIdx;
    private String articleCategory;
    private String originFilename;
    private String saveFilename;
    private Long fileSize;
    private String fileExt;
    private String filePath;
    private Boolean fileActivated;

    private LocalDateTime fileRegdate;

    @Builder
    public File(Long fileIdx, Long articleIdx, String articleCategory, String originFilename, String saveFilename, Long fileSize, String fileExt, String filePath, Boolean fileActivated, LocalDateTime fileRegdate) {
        this.fileIdx = fileIdx;
        this.articleIdx = articleIdx;
        this.articleCategory = articleCategory;
        this.originFilename = originFilename;
        this.saveFilename = saveFilename;
        this.fileSize = fileSize;
        this.fileExt = fileExt;
        this.filePath = filePath;
        this.fileActivated = fileActivated;
        this.fileRegdate = fileRegdate;
    }
}
