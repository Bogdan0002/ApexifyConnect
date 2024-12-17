package com.apexify.logic.Controller;

import com.apexify.logic.DTO.TagRequestDTO;
import com.apexify.logic.DTO.TagResponseDTO;
import com.apexify.logic.Service.TagService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tags")
public class TagsController {

    private final TagService tagService;

    public TagsController(TagService tagService) {
        this.tagService = tagService;
    }

    /**
     * Create or find a tag by name.
     * Accepts a TagRequestDTO in the request body.
     */
    @PostMapping
    public ResponseEntity<TagResponseDTO> createOrFindTag(@RequestBody TagRequestDTO tagRequestDTO) {
        TagResponseDTO tagResponseDTO = tagService.findOrCreateTag(tagRequestDTO);
        return ResponseEntity.ok(tagResponseDTO);
    }

    /**
     * Retrieve all tags.
     * Returns a list of TagResponseDTOs.
     */
    @GetMapping("/all")
    public ResponseEntity<List<TagResponseDTO>> getAllTags() {
        List<TagResponseDTO> tags = tagService.getAllTags();
        return ResponseEntity.ok(tags);
    }
}