package com.apexify.logic.unit;

import com.apexify.logic.Controller.TagsController;
import com.apexify.logic.DTO.TagRequestDTO;
import com.apexify.logic.DTO.TagResponseDTO;
import com.apexify.logic.Service.TagService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TagsControllerTest {

    @Mock
    private TagService tagService;

    private TagsController tagsController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        tagsController = new TagsController(tagService);
    }

    @Test
    void createOrFindTag_Success() {
        // Arrange
        TagRequestDTO requestDTO = new TagRequestDTO();
        requestDTO.setName("Design");
        TagResponseDTO responseDTO = new TagResponseDTO(1L, "Design");

        when(tagService.findOrCreateTag(any(TagRequestDTO.class))).thenReturn(responseDTO);

        // Act
        ResponseEntity<TagResponseDTO> response = tagsController.createOrFindTag(requestDTO);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Design", response.getBody().getName());
        verify(tagService).findOrCreateTag(requestDTO);
    }

    @Test
    void getAllTags_Success() {
        // Arrange
        List<TagResponseDTO> tags = Arrays.asList(
                new TagResponseDTO(1L, "Design"),
                new TagResponseDTO(2L, "Video")
        );
        when(tagService.getAllTags()).thenReturn(tags);

        // Act
        ResponseEntity<List<TagResponseDTO>> response = tagsController.getAllTags();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void getAllTags_EmptyList() {
        // Arrange
        when(tagService.getAllTags()).thenReturn(Collections.emptyList());

        // Act
        ResponseEntity<List<TagResponseDTO>> response = tagsController.getAllTags();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    void createOrFindTag_InvalidName() {
        // Arrange
        TagRequestDTO requestDTO = new TagRequestDTO();
        when(tagService.findOrCreateTag(any(TagRequestDTO.class)))
                .thenThrow(new IllegalArgumentException("Invalid tag name"));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                tagsController.createOrFindTag(requestDTO));
    }
}
