package com.grademusic.main.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.OffsetDateTime;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@IdClass(WishlistItemId.class)
@Table(name = "user_wishlists")
public class WishlistItem {

    @Id
    @Column(name = "user_id")
    private Long userId;

    @Id
    @Column(name = "album_id")
    private String albumId;

    @Column(name = "created_at")
    @CreationTimestamp
    private OffsetDateTime createDate;
}
