package com.app.ventas_api.CMS.IService;

import com.app.ventas_api.CMS.Entity.ContentBlock;
import java.util.List;
import java.util.Optional;

/**
 * CMS - IService
 * Interface: IContentBlockService
 */
public interface IContentBlockService {
    
    List<ContentBlock> findAll() throws Exception;
    
    Optional<ContentBlock> findById(Long id) throws Exception;
    
    List<ContentBlock> findBySection(String section) throws Exception;
    
    ContentBlock findByContentKey(String contentKey) throws Exception;
    
    ContentBlock save(ContentBlock entity) throws Exception;
    
    void update(Long id, ContentBlock entity) throws Exception;
    
    void delete(Long id) throws Exception;
}
