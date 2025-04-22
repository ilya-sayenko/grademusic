package com.grademusic.main.service;

import com.grademusic.main.entity.AlbumGrade;
import com.grademusic.main.entity.AlbumGradeId;
import com.grademusic.main.exception.AlbumGradeNotFoundException;
import com.grademusic.main.repository.AlbumGradeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GradeServiceImpl implements GradeService {

    private final AlbumGradeRepository albumGradeRepository;

    private final KafkaClient kafkaClient;

    @Override
    @Transactional
    public void gradeAlbum(long userId, String albumId, int grade) {
        Optional<AlbumGrade> albumGradeOpt = albumGradeRepository.findById(calculateId(userId, albumId));
        AlbumGrade albumGrade;
        if (albumGradeOpt.isEmpty()) {
            albumGrade = AlbumGrade.builder()
                    .userId(userId)
                    .albumId(albumId)
                    .grade(grade)
                    .auditionDate(LocalDate.now())
                    .build();
        } else {
            albumGrade = albumGradeOpt.get();
            albumGrade.setGrade(grade);
        }
        albumGradeRepository.save(albumGrade);
        kafkaClient.sendUpdateAlbumStatistics(albumId);
        kafkaClient.sendUpdateUserStatistics(userId);
    }

    @Override
    public void updateAuditionDate(long userId, String albumId, LocalDate auditionDate) {
        AlbumGrade albumGrade = albumGradeRepository.findById(
                AlbumGradeId.builder()
                        .userId(userId)
                        .albumId(albumId)
                        .build()
        ).orElseThrow(() -> new AlbumGradeNotFoundException(String.format("Grade for albumId=%s not found", albumId)));
        albumGrade.setAuditionDate(auditionDate);
        albumGradeRepository.save(albumGrade);
    }

    @Override
    @Transactional
    public void deleteGrade(long userId, String albumId) {
        albumGradeRepository.deleteById(calculateId(userId, albumId));
        kafkaClient.sendUpdateAlbumStatistics(albumId);
    }

    private AlbumGradeId calculateId(long userId, String albumId) {
        return AlbumGradeId.builder()
                .userId(userId)
                .albumId(albumId)
                .build();
    }
}
