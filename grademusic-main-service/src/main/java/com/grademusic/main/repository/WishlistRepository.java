package com.grademusic.main.repository;

import com.grademusic.main.entity.WishlistItem;
import com.grademusic.main.entity.WishlistItemId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WishlistRepository extends JpaRepository<WishlistItem, WishlistItemId> {

    Page<WishlistItem> findByUserId(Long userId, Pageable pageable);
}
