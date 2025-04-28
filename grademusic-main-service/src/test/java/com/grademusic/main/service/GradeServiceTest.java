package com.grademusic.main.service;

import com.grademusic.main.entity.AlbumGrade;
import com.grademusic.main.entity.AlbumGradeId;
import com.grademusic.main.exception.AlbumGradeNotFoundException;
import com.grademusic.main.repository.AlbumGradeRepository;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GradeServiceTest {

    @Mock
    private AlbumGradeRepository albumGradeRepository;

    @Mock
    private ProfileService profileService;

    @Mock
    private KafkaClient kafkaClient;

    @InjectMocks
    private GradeServiceImpl gradeService;

    @Test
    public void shouldGradeAlbumFirstly() {
        long userId = 1;
        String albumId = "album";
        int grade = 1;
        when(albumGradeRepository.findById(any(AlbumGradeId.class))).thenReturn(Optional.empty());
        gradeService.gradeAlbum(userId, albumId, grade);

        verify(albumGradeRepository, atLeastOnce()).save(any(AlbumGrade.class));
        verify(profileService, atLeastOnce()).deleteAlbumFromWishList(userId, albumId);
        verify(kafkaClient, atLeastOnce()).sendUpdateAlbumStatistics(albumId);
        verify(kafkaClient, atLeastOnce()).sendUpdateUserStatistics(userId);
    }

    @Test
    public void shouldGradeAlbumSecondary() {
        long userId = 1;
        String albumId = "album";
        int grade = 1;
        when(albumGradeRepository.findById(any(AlbumGradeId.class))).thenReturn(
                Optional.of(Instancio.create(AlbumGrade.class))
        );
        gradeService.gradeAlbum(userId, albumId, grade);

        verify(albumGradeRepository, atLeastOnce()).save(any(AlbumGrade.class));
        verify(profileService, atLeastOnce()).deleteAlbumFromWishList(userId, albumId);
        verify(kafkaClient, atLeastOnce()).sendUpdateAlbumStatistics(albumId);
        verify(kafkaClient, atLeastOnce()).sendUpdateUserStatistics(userId);
    }

    @Test
    public void shouldUpdateAuditionDate() {
        long userId = 1;
        String albumId = "album";
        LocalDate auditionDate = LocalDate.now();
        when(albumGradeRepository.findById(any(AlbumGradeId.class))).thenReturn(
                Optional.of(Instancio.create(AlbumGrade.class))
        );
        gradeService.updateAuditionDate(userId, albumId, auditionDate);

        verify(albumGradeRepository, atLeastOnce()).save(any(AlbumGrade.class));
    }

    @Test
    public void shouldThrowWhileUpdateAuditionDateIfAlbumGradeIsNotExists() {
        long userId = 1;
        String albumId = "album";
        LocalDate auditionDate = LocalDate.now();
        when(albumGradeRepository.findById(any(AlbumGradeId.class))).thenThrow(AlbumGradeNotFoundException.class);

        assertThrows(AlbumGradeNotFoundException.class, () -> gradeService.updateAuditionDate(userId, albumId, auditionDate));
        verify(albumGradeRepository, never()).save(any(AlbumGrade.class));
    }

    @Test
    public void shouldDeleteGrade() {
        long userId = 1;
        String albumId = "album";
        gradeService.deleteGrade(userId, albumId);

        verify(albumGradeRepository, atLeastOnce()).deleteById(any(AlbumGradeId.class));
        verify(kafkaClient, atLeastOnce()).sendUpdateAlbumStatistics(eq(albumId));
        verify(kafkaClient, atLeastOnce()).sendUpdateUserStatistics(eq(userId));
    }

    @Test
    public void shouldFindGrade() {
        long userId = 1;
        String albumId = "album";
        AlbumGrade albumGradeFromDatabase = Instancio.create(AlbumGrade.class);
        when(albumGradeRepository.findById(any(AlbumGradeId.class))).thenReturn(Optional.of(albumGradeFromDatabase));
        AlbumGrade albumGrade = gradeService.findGrade(userId, albumId);

        assertThat(albumGradeFromDatabase)
                .usingRecursiveComparison()
                .isEqualTo(albumGrade);
    }
}