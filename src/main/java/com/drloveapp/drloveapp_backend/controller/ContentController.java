package com.drloveapp.drloveapp_backend.controller;

import com.drloveapp.drloveapp_backend.model.Content;
import com.drloveapp.drloveapp_backend.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/contents")
@CrossOrigin(origins = "*")
public class ContentController {

    @Autowired
    private ContentService contentService;

    @GetMapping
    public List<Content> getAllContents() {

        return contentService.getAllContents();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Content> getContentById(@PathVariable UUID id) {
        Optional<Content> content = contentService.getContentById(id);
        if (content.isPresent()) {
            return ResponseEntity.ok(content.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/create")
    public Content createContent(@RequestBody Content content) {

        return contentService.createContent(content);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Content> updateContent(@PathVariable UUID id, @RequestBody Content contentDetails) {
        Content updatedContent = contentService.updateContent(id, contentDetails);
        if (updatedContent != null) {
            return ResponseEntity.ok(updatedContent);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public Void deleteContent(@PathVariable UUID id) {

    return contentService.deleteContent(id);
    }

}