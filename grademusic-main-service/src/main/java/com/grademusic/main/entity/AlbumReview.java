package com.grademusic.main.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "album_reviews")
public class AlbumReview {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "album_id")
    private String albumId;

    @Column(name = "grade")
    private int grade;

    @Column(name = "text")
    private String text;

    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private OffsetDateTime createDate;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private OffsetDateTime updateDate;
}
