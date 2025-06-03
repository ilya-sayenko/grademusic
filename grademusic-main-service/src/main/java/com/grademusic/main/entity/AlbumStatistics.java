package com.grademusic.main.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "album_statistics")
public class AlbumStatistics {

    @Id
    @Column(name = "album_id")
    private String albumId;

    @Column(name = "grade")
    private Double grade;

    @Column(name = "count_of_grades")
    private Long countOfGrades;

    @Column(name = "count_of_wishlist_items")
    private Long countOfWishlistItems;

    @Column(name = "count_of_reviews")
    private Long countOfReviews;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private OffsetDateTime updateDate;
}
