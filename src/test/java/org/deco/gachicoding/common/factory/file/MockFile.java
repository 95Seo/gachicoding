package org.deco.gachicoding.common.factory.file;

import org.deco.gachicoding.file.domain.File;

import java.time.LocalDateTime;

public class MockFile {

    private MockFile() {}

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long fileIdx;
        private Long articleIdx;

        private String articleCategory;
        private String originFilename = "testSuccessImage1.png";
        private String saveFilename;
        private String fileExt;
        private String filePath;

        private LocalDateTime createdAt = LocalDateTime.of(2022, 2, 2, 2, 2);
        private LocalDateTime updatedAt = LocalDateTime.of(2022, 2, 2, 2, 2);

        public Builder fileIdx(Long fileIdx) {
            this.fileIdx = fileIdx;
            return this;
        }

        public Builder articleIdx(Long articleIdx) {
            this.articleIdx = articleIdx;
            return this;
        }

        public Builder articleCategory(String articleCategory) {
            this.articleCategory = articleCategory;
            return this;
        }

        public Builder originFilename(String originFilename) {
            this.originFilename = originFilename;
            return this;
        }

        public Builder saveFilename(String saveFilename) {
            this.saveFilename = saveFilename;
            return this;
        }

        public Builder fileExt(String fileExt) {
            this.fileExt = fileExt;
            return this;
        }

        public Builder filePath(String filePath) {
            this.filePath = filePath;
            return this;
        }

        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public File build() {
            return new File(
                    fileIdx,
                    articleIdx,
                    articleCategory,
                    originFilename,
                    saveFilename,
                    fileExt,
                    filePath,
                    createdAt,
                    updatedAt
            );
        }
    }

}
