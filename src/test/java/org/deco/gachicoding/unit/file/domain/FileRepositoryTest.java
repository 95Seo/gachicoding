package org.deco.gachicoding.unit.file.domain;

import org.deco.gachicoding.file.domain.repository.FileRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class FileRepositoryTest {

    @Autowired
    FileRepository fileRepository;

    @Test
    @DisplayName("파일 메타 정보를 저장한다.")
    void save_saveFile_Success() {
        // given

    }

}
