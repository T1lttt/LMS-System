package com.lms.lmssystem.controller;

import com.lms.lmssystem.service.impl.FileServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FileControllerTest {

    @Mock
    private FileServiceImpl fileService;

    @InjectMocks
    private FileController fileController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testUpload() throws Exception {
        MultipartFile file = mock(MultipartFile.class);
        when(file.getOriginalFilename()).thenReturn("test.txt");
        when(fileService.uploadFile(file)).thenReturn("File uploaded successfully");

        String result = fileController.upload(file);

        assertEquals("File uploaded successfully", result);
        verify(fileService, times(1)).uploadFile(file);
    }

    @Test
    void testDownloadFile() {
        String fileName = "test.txt";
        byte[] content = "Hello".getBytes();
        ByteArrayResource resource = new ByteArrayResource(content);

        when(fileService.downloadFile(fileName)).thenReturn(resource);

        ResponseEntity<ByteArrayResource> response = fileController.downloadFile(fileName);

        assertNotNull(response);
        assertEquals(resource, response.getBody());
        assertTrue(response.getHeaders().getContentDisposition().getFilename().contains(fileName));
        verify(fileService, times(1)).downloadFile(fileName);
    }
}