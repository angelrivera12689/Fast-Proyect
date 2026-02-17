package com.app.ventas_api.CMS.Service;

import com.app.ventas_api.CMS.Entity.ContentBlock;
import com.app.ventas_api.CMS.IRepository.IContentBlockRepository;
import com.app.ventas_api.CMS.IService.IContentBlockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * CMS - Service
 * Implementation: ContentBlockService
 */
@Service
public class ContentBlockService implements IContentBlockService {
    
    @Autowired
    private IContentBlockRepository repository;
    
    @Override
    public List<ContentBlock> findAll() throws Exception {
        return repository.findAll();
    }
    
    @Override
    public Optional<ContentBlock> findById(Long id) throws Exception {
        Optional<ContentBlock> op = repository.findById(id);
        if (op.isEmpty()) {
            throw new Exception("ContentBlock not found");
        }
        return op;
    }
    
    @Override
    public List<ContentBlock> findBySection(String section) throws Exception {
        return repository.findBySectionAndActiveTrue(section);
    }
    
    @Override
    public ContentBlock findByContentKey(String contentKey) throws Exception {
        return repository.findByContentKey(contentKey)
                .orElseThrow(() -> new Exception("ContentBlock not found with contentKey: " + contentKey));
    }
    
    @Override
    public ContentBlock save(ContentBlock entity) throws Exception {
        // Si ya existe, actualizar en lugar de crear duplicado
        if (entity.getSection() != null && entity.getContentKey() != null) {
            Optional<ContentBlock> existing = repository.findBySectionAndContentKey(entity.getSection(), entity.getContentKey());
            if (existing.isPresent()) {
                ContentBlock update = existing.get();
                update.setContent(entity.getContent());
                update.setImageUrl(entity.getImageUrl());
                update.setVideoUrl(entity.getVideoUrl());
                update.setSortOrder(entity.getSortOrder());
                update.setActive(entity.getActive());
                return repository.save(update);
            }
        }
        return repository.save(entity);
    }
    
    @Override
    public void update(Long id, ContentBlock entity) throws Exception {
        Optional<ContentBlock> op = repository.findById(id);
        if (op.isEmpty()) {
            throw new Exception("ContentBlock not found");
        }
        
        ContentBlock update = op.get();
        update.setSection(entity.getSection());
        update.setContentKey(entity.getContentKey());
        update.setContent(entity.getContent());
        update.setImageUrl(entity.getImageUrl());
        update.setVideoUrl(entity.getVideoUrl());
        update.setSortOrder(entity.getSortOrder());
        update.setActive(entity.getActive());
        
        repository.save(update);
    }
    
    @Override
    public void delete(Long id) throws Exception {
        Optional<ContentBlock> op = repository.findById(id);
        if (op.isEmpty()) {
            throw new Exception("ContentBlock not found");
        }
        repository.delete(op.get());
    }
}
