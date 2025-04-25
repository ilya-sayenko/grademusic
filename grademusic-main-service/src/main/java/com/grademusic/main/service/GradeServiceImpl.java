package com.grademusic.main.service;

import com.grademusic.main.controller.model.AlbumGradeSearchRequest;
import com.grademusic.main.entity.AlbumGrade;
import com.grademusic.main.entity.AlbumGradeId;
import com.grademusic.main.exception.AlbumGradeNotFoundException;
import com.grademusic.main.repository.AlbumGradeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GradeServiceImpl implements GradeService {

    private final AlbumGradeRepository albumGradeRepository;

    private final ProfileService profileService;

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
        profileService.deleteAlbumFromWishList(userId, albumId);
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

    @Override
    public List<AlbumGrade> findGrades(AlbumGradeSearchRequest request) {
        List<String> albumIds = request.albumIds();
        Long userId = request.userId();
        if (albumIds != null && !albumIds.isEmpty()) {
            return findGradesByUserIdAndAlbumIds(userId, albumIds);
        }

        return findGradesByUserId(userId);
    }

    @Override
    public List<AlbumGrade> findGradesByUserIdAndAlbumIds(Long userId, List<String> albumIds) {
        return albumGradeRepository.findByUserIdAndAlbumIdIn(userId, albumIds);
    }

    @Override
    public List<AlbumGrade> findGradesByUserId(Long userId) {
        return albumGradeRepository.findByUserId(userId);
    }

    private AlbumGradeId calculateId(long userId, String albumId) {
        return AlbumGradeId.builder()
                .userId(userId)
                .albumId(albumId)
                .build();
    }
}
