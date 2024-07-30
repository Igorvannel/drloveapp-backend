package com.drloveapp.drloveapp_backend.service;


import com.drloveapp.drloveapp_backend.model.Content;
import com.drloveapp.drloveapp_backend.repository.ContentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ContentService {

    @Autowired
    private final ContentRepository contentRepository;



    public List<Content> getAllContents(){
        return contentRepository.findAll();
    }

    public Optional<Content> getContentById(UUID id) {
        return contentRepository.findById(id);
    }

    public Content createContent(Content content) {
        content.setCreatedAt(LocalDateTime.now());
        content.setUpdatedAt(LocalDateTime.now());
        return contentRepository.save(content);

    }

    public Content updateContent(UUID id, Content contentDetails) {
        Optional<Content> contentOptional = contentRepository.findById(id);
        if (contentOptional.isPresent()) {
            Content content = contentOptional.get();
            content.setTitle(contentDetails.getTitle());
            content.setBody(contentDetails.getBody());
            content.setCategory(contentDetails.getCategory());
            content.setUpdatedAt(LocalDateTime.now());
            return contentRepository.save(content);
        }
        return null;
    }

    public Void deleteContent(UUID id) {
        contentRepository.deleteById(id);
        return null;
    }
}
