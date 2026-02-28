package com.lms.lmssystem.service;

import com.lms.lmssystem.service.impl.FileServiceImpl;
import io.minio.MinioClient;
import io.minio.GetObjectArgs;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mock.web.MockMultipartFile;
import io.minio.GetObjectResponse;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class FileServiceImplTest {

    @Mock
    private MinioClient minioClient;

    @InjectMocks
    private FileServiceImpl fileService;

    @BeforeEach
    void setup() throws Exception {
        MockitoAnnotations.openMocks(this);
        Field field = FileServiceImpl.class.getDeclaredField("bucket");
        field.setAccessible(true);
        field.set(fileService, "test-bucket");
    }

    @Test
    void uploadFile_success() throws Exception {
        MockMultipartFile file =
                new MockMultipartFile("file", "test.txt",
                        "text/plain", "data".getBytes());

        doNothing().when(minioClient).putObject(any());

        String result = fileService.uploadFile(file);

        assertEquals("File  uploaded successfully", result);
        verify(minioClient).putObject(any());
    }

    @Test
    void uploadFile_exception() throws Exception {
        MockMultipartFile file =
                new MockMultipartFile("file", "test.txt",
                        "text/plain", "data".getBytes());

        doThrow(new RuntimeException())
                .when(minioClient).putObject(any());

        String result = fileService.uploadFile(file);

        assertEquals("Some errors on file uploads", result);
    }

    @Test
    void downloadFile_success() throws Exception {

        byte[] data = "hello".getBytes();
        InputStream stream = new ByteArrayInputStream(data);

        GetObjectResponse response = mock(GetObjectResponse.class);
        when(response.readAllBytes()).thenReturn(data);

        when(minioClient.getObject(any(GetObjectArgs.class)))
                .thenReturn(response);

        ByteArrayResource resource = fileService.downloadFile("test.txt");

        assertNotNull(resource);
        assertArrayEquals(data, resource.getByteArray());
    }

    @Test
    void downloadFile_exception() throws Exception {
        when(minioClient.getObject(any(GetObjectArgs.class)))
                .thenThrow(new RuntimeException());

        ByteArrayResource resource = fileService.downloadFile("test.txt");

        assertNull(resource);
    }
}