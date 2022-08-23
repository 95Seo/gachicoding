package org.deco.gachicoding.file.application;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.deco.gachicoding.file.domain.File;
import org.deco.gachicoding.file.domain.repository.FileRepository;
import org.deco.gachicoding.file.dto.FileResponse;
import org.deco.gachicoding.file.dto.response.FileResponseDto;
import org.deco.gachicoding.file.dto.request.FileSaveRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// 리팩토링1 @Autowired -> @RequiredArgsConstructor
@Slf4j
@Service
@NoArgsConstructor
public class FileService {
        @Autowired
        private FileRepository fileRepository;
        
        /**
         *  tempImg 폴더를 무조건 보장해줄 로직을 만들고 싶다 ㅠㅠ
         * 파일 경로 찾기가 어렵네 배포하면 또 달라진다는 듯
         * 상대 경로로 접근한다는게 가장 안전하다는데 좀더 알아봄
        **/
//        private final static String tempRoot = "/src/main/resources/tempImg/";
//        // 절대 경로를 위한 absolutePath
//        private final static String absolutePath = new File("").getAbsolutePath() + "\\";
//        private final static Path path = Path.of(absolutePath + tempRoot);

        private AmazonS3 s3Client;

        @Value("${cloud.aws.credentials.accessKey}")
        private String accessKey;

        @Value("${cloud.aws.credentials.secretKey}")
        private String secretKey;

        @Value("${cloud.aws.s3.bucket}")
        private String bucket;

        @Value("${cloud.aws.region.static}")
        private String region;

        @PostConstruct
        public void setS3Client() {
                AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);

                s3Client = AmazonS3ClientBuilder.standard()
                        .withCredentials(new AWSStaticCredentialsProvider(credentials))
                        .withRegion(this.region)
                        .build();

//                try {
//                        if (!Files.exists(path)) {
//                                Files.createDirectories(path);
//                        }
//                } catch (IOException e) {
//                        e.printStackTrace();
//                }
        }

        public List<String> uploadTempImg(List<MultipartFile> files) throws IOException {
                List<String> result = new ArrayList<>();

                // 저장 경로 바꿔야 함 (날짜도 추가)
                for(MultipartFile f : files) {
                        String origFileName = null;
                        String saveFileName = null;
                        String filePath = null;

                        System.out.println("f : "+f.getOriginalFilename());
                        // 파일 정보 추출 (이걸 dto 생성 시 바로 해버리면?)
                        origFileName = f.getOriginalFilename();
                        // 경로에 userId 추가
                        saveFileName = "temp/" + UUID.randomUUID() + "_" + origFileName;

                        filePath = putS3(f, saveFileName);

                        result.add(filePath);
                }
                return result;
        }

        @Transactional
        public void registerFile(FileSaveRequestDto fileSaveRequestDto) {
                // Uncheck Exception 발생 가능
                // 어떤 식으로 로그를 남길 수 있을까?
                try {
                        fileRepository.save(fileSaveRequestDto.toEntity());
                        log.info("Success Save File");
                        // 구체적인 익셉션?
                } catch (Exception e) {
                        log.info("Error Save File");
                        e.printStackTrace();
                }
        }

        @Transactional
        public FileResponse getFiles(Long idx, String category, FileResponse dto) {
                List<FileResponseDto> result = new ArrayList<>();
                List<File> fileList = fileRepository.findAllByArticleCategoryAndArticleIdx(category, idx);

                for (File f : fileList) {
                        result.add(new FileResponseDto(f));
                }

                dto.setFiles(result);
                return dto;
        }

        public String extractImgSrc(Long idx, String content, String category) throws IOException {
                // 정규 표현식 공부하자
                Pattern nonValidPattern = Pattern
                        .compile("(?i)< *[IMG][^\\>]*[src] *= *[\"\']{0,1}([^\"\'\\ >]*)");
                Matcher matcher = nonValidPattern.matcher(content);
                String beforeImg = "";
                String afterImg = "";

                while (matcher.find()) {
                        beforeImg = matcher.group(1);

                        // 구현 절차
                        // 추출된 이미지 링크 s3업로드
                        // File DB에 업로드
                        // content 리플레이스
                        afterImg = uploadRealImg(idx, beforeImg, category);
                        content = content.replace(beforeImg, afterImg);
                }

                return content;
        }

        private String uploadRealImg(Long idx, String path, String category) throws IOException {
                String origFileName = null;
                String origFileExtension = null;
                String tamperingFileName = null;
                String filePath = null;
                String oldPath = null;
                String newPath = null;
                Long fileSize = null;

                // 저장 경로 바꿔야 함 (날짜도 추가)
                log.info("path : " + path);

                // oldPath -> substring, indexOf, lastIndexOf -> 뒤에서 부터 인덱스 찾기
                try {
                        oldPath = URLDecoder.decode(path.substring(path.indexOf("temp")),"UTF-8");
                } catch (UnsupportedEncodingException e) {
                        log.error("Encoding Error");
                        throw e;
                }
                tamperingFileName = oldPath.split("temp/")[1];  //[4] -> /의 수에따라 바뀜

                origFileName = tamperingFileName.substring(tamperingFileName.indexOf("_")).replaceFirst("_", "");
                origFileExtension = origFileName.substring(origFileName.lastIndexOf("."));

                // 날짜 추가
                newPath = category + "/" + idx + "/" + tamperingFileName;
                fileSize = convertFile(path, tamperingFileName);

                filePath = updateS3(oldPath, newPath);
                log.info("oldPath : " + oldPath);
                log.info("tamperingFileName : " + tamperingFileName);
                log.info("origFileName : " + origFileName);
                log.info("origFileExtension : " + origFileExtension);
                log.info("newPath : " + newPath);
                log.info("fileSize : " + fileSize);
                log.info("filePath : " + filePath);

                FileSaveRequestDto dto = FileSaveRequestDto.builder()
                        .articleIdx(idx)
                        .articleCategory(category)
                        .originFilename(origFileName)
                        .fileExt(origFileExtension)
                        .saveFilename(tamperingFileName)
                        .fileSize(fileSize)
                        .filePath(filePath)
                        .build();

                registerFile(dto);

                return filePath;
        }

        private Long convertFile(String filePath, String tamperingFileName) throws IOException {
                try {
                        Long fileSize;

                        URL url = new URL(filePath);
                        String canonicalPath = new java.io.File("").getCanonicalPath();
                        Path savePath = Paths.get(canonicalPath + "/src/main/resources/tempImg/", tamperingFileName);
                        InputStream is = url.openStream();

                        copyLocalFile(is, savePath);
                        fileSize = getFileSize(savePath);
                        deleteLocalFile(savePath);

                        log.info("Success Convert File");

                        return fileSize;
                } catch (IOException e) {
                        log.error("Failed Convert File");
                        throw e;
                }
        }

        private void copyLocalFile(InputStream is, Path savePath) throws IOException {
                try {
                        Files.copy(is, savePath);
                        is.close();
                } catch (IOException e) {
                        log.error("Failed Copy Local File");
                        throw e;
                }
        }

        private void deleteLocalFile(Path savePath) throws IOException {
                try {
                        Files.delete(savePath);
                } catch (IOException e) {
                        log.error("Failed Delete Local File");
                        throw e;
                }
        }

        private Long getFileSize(Path savePath) throws IOException {
                try {
                        log.info("start Get File Size");
                        Long bytes = Files.size(savePath);
                        Long kilobyte = bytes/1024;
                        Long megabyte = kilobyte/1024;
                        return bytes;
                } catch (IOException e) {
                        log.info("Filed Get File Size");
                        throw e;
                }
        }

        private String updateS3(String oldPath, String newPath) {
                String result = copyS3(oldPath, newPath);
                deleteS3(oldPath);
                return result;
        }

        private String getS3Url(String filePath) {
                // s3 객체 URL
                return s3Client.getUrl(bucket, filePath).toString();
        }

        private String putS3(MultipartFile file, String saveFilePath) throws IOException {
                // try catch 걸까?
                // s3 저장
                s3Client.putObject(new PutObjectRequest(bucket, saveFilePath, file.getInputStream(), null)
                        .withCannedAcl(CannedAccessControlList.PublicRead));

                return getS3Url(saveFilePath);
        }

        private String copyS3(String oldPath, String newPath) {
                s3Client.copyObject(bucket, oldPath, bucket, newPath);

                return getS3Url(newPath);
        }

        private void deleteS3(String source) {
                s3Client.deleteObject(bucket, source);
        }

}
