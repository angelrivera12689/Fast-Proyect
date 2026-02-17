package com.app.ventas_api.CMS.IRepository;

import com.app.ventas_api.CMS.Entity.ContentBlock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * CMS - IRepository
 * Interface: IContentBlockRepository
 */
@Repository
public interface IContentBlockRepository extends JpaRepository<ContentBlock, Long> {
    
    List<ContentBlock> findBySection(String section);
    
    List<ContentBlock> findBySectionAndActiveTrue(String section);
    
    Optional<ContentBlock> findBySectionAndContentKey(String section, String contentKey);
    
    Optional<ContentBlock> findByContentKey(String contentKey);
    
    boolean existsBySectionAndContentKey(String section, String contentKey);
}
