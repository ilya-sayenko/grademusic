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

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_statistics")
public class UserStatistics {

    @Id
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "average_grade")
    private Double averageGrade;

    @Column(name = "count_of_grades")
    private Long countOfGrades;

    @Column(name = "count_of_wishlist_items")
    private Long countOfWishlistItems;

    @Column(name = "first_grade_at")
    private LocalDate firstGradeDate;

    @Column(name = "last_grade_at")
    private LocalDate lastGradeDate;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private OffsetDateTime updateDate;
}
