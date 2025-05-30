package com.grademusic.main.service;

import com.grademusic.main.controller.model.AlbumGradeSearchRequest;
import com.grademusic.main.controller.model.PaginatedRequest;
import com.grademusic.main.entity.AlbumGrade;
import com.grademusic.main.entity.AlbumGradeId;
import com.grademusic.main.entity.WishlistItemId;
import com.grademusic.main.exception.AlbumGradeNotFoundException;
import com.grademusic.main.repository.AlbumGradeRepository;
import com.grademusic.main.repository.WishlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.grademusic.main.model.StatisticsType.GRADES;
import static com.grademusic.main.model.StatisticsType.WISHLIST;

@Service
@RequiredArgsConstructor
public class GradeServiceImpl implements GradeService {

    private final AlbumGradeRepository albumGradeRepository;

    private final WishlistRepository wishlistRepository;

    private final KafkaClient kafkaClient;

    @Override
    public void gradeAlbum(long userId, String albumId, int grade) {
        saveAlbumGradeAndDeleteFromWishlist(userId, albumId, grade);
        kafkaClient.sendUpdateAlbumStatistics(albumId, GRADES);
        kafkaClient.sendUpdateAlbumStatistics(albumId, WISHLIST);
        kafkaClient.sendUpdateUserStatistics(userId, GRADES);
        kafkaClient.sendUpdateUserStatistics(userId, WISHLIST);
    }

    @Override
    public void updateAuditionDate(long userId, String albumId, LocalDate auditionDate) {
        AlbumGrade albumGrade = albumGradeRepository.findById(calculateAlbumGradeId(userId, albumId))
                .orElseThrow(() -> new AlbumGradeNotFoundException(String.format("Grade for albumId=%s not found", albumId)));
        albumGrade.setAuditionDate(auditionDate);
        albumGradeRepository.save(albumGrade);
    }

    @Override
    public void deleteGrade(long userId, String albumId) {
        AlbumGradeId albumGradeId = calculateAlbumGradeId(userId, albumId);
        if (albumGradeRepository.existsById(albumGradeId)) {
            albumGradeRepository.deleteById(albumGradeId);
            kafkaClient.sendUpdateUserStatistics(userId, GRADES);
            kafkaClient.sendUpdateAlbumStatistics(albumId, GRADES);
        }
    }

    @Override
    public AlbumGrade findGrade(long userId, String albumId) {
        return albumGradeRepository.findById(calculateAlbumGradeId(userId, albumId))
                .orElse(AlbumGrade.builder().userId(userId).albumId(albumId).build());
    }

    @Override
    public Page<AlbumGrade> findPaginatedGrades(
            AlbumGradeSearchRequest request,
            PaginatedRequest paginatedRequest
    ) {
        List<String> albumIds = request.albumIds();
        Long userId = request.userId();
        if (albumIds != null && !albumIds.isEmpty()) {
            return findGradesByUserIdAndAlbumIds(userId, albumIds, paginatedRequest);
        }

        return findGradesByUserId(userId, paginatedRequest);
    }

    private Page<AlbumGrade> findGradesByUserIdAndAlbumIds(Long userId, List<String> albumIds, PaginatedRequest paginatedRequest) {
        return albumGradeRepository.findByUserIdAndAlbumIdIn(
                userId,
                albumIds,
                PageRequest.of(
                        paginatedRequest.page(),
                        paginatedRequest.perPage(),
                        Sort.by(Sort.Direction.DESC, "createDate")
                )
        );
    }

    private Page<AlbumGrade> findGradesByUserId(Long userId, PaginatedRequest paginatedRequest) {
        return albumGradeRepository.findByUserId(
                userId,
                PageRequest.of(
                        paginatedRequest.page(),
                        paginatedRequest.perPage(),
                        Sort.by(Sort.Direction.DESC, "createDate")
                )
        );
    }

    private AlbumGradeId calculateAlbumGradeId(long userId, String albumId) {
        return AlbumGradeId.builder()
                .userId(userId)
                .albumId(albumId)
                .build();
    }

    @Transactional
    private void saveAlbumGradeAndDeleteFromWishlist(long userId, String albumId, int grade) {
        Optional<AlbumGrade> albumGradeOpt = albumGradeRepository.findById(calculateAlbumGradeId(userId, albumId));
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
        WishlistItemId wishlistItemId = WishlistItemId.builder()
                .userId(userId)
                .albumId(albumId)
                .build();
        if (wishlistRepository.existsById(wishlistItemId)) {
            wishlistRepository.deleteById(wishlistItemId);
        }
    }
}
