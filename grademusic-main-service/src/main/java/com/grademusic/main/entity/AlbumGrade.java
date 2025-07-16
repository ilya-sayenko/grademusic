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
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@IdClass(AlbumGradeId.class)
@Table(name = "album_grades")
public class AlbumGrade {

    @Id
    @Column(name = "user_id")
    private Long userId;

    @Id
    @Column(name = "album_id")
    private String albumId;

    @Column(name = "grade")
    private int grade;

    @Column(name = "auditioned_at")
    private LocalDate auditionDate;

    @Column(name = "created_at")
    @CreationTimestamp
    private OffsetDateTime createDate;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private OffsetDateTime updateDate;
}
