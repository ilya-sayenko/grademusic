package com.grademusic.main.mapper;


import com.grademusic.main.config.MapperConfig;
import com.grademusic.main.controller.model.AlbumSearchResponse;
import com.grademusic.main.model.Album;
import com.grademusic.main.model.Image;
import com.grademusic.main.model.lastfm.AlbumLastFm;
import com.grademusic.main.model.lastfm.ImageLastFm;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.ArrayList;
import java.util.List;

@Mapper(config = MapperConfig.class)
public interface AlbumMapper {

    AlbumSearchResponse toResponse(Album album);

    List<AlbumSearchResponse> toResponse(List<Album> albums);

    @Mapping(source = "mbid", target = "id")
    Album fromLastFm(AlbumLastFm albumLastFm);

    List<Album> fromLastFm(List<AlbumLastFm> albumsLastFm);

    default Image fromLastFm(ArrayList<ImageLastFm> imagesLastFm) {
        Image.ImageBuilder imageBuilder = Image.builder();

        for (ImageLastFm imageLastFm : imagesLastFm) {
            String size = imageLastFm.size();
            switch (size) {
                case "small":
                    imageBuilder.small(imageLastFm.text());
                    break;
                case "medium":
                    imageBuilder.medium(imageLastFm.text());
                    break;
                case "large":
                    imageBuilder.large(imageLastFm.text());
                    break;
                case "extralarge":
                    imageBuilder.extralarge(imageLastFm.text());
                    break;
            }
        }

        return imageBuilder.build();
    }
}
