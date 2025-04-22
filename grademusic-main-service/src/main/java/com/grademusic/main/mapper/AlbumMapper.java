package com.grademusic.main.mapper;

import com.grademusic.main.config.MapperConfig;
import com.grademusic.main.controller.model.AlbumResponse;
import com.grademusic.main.controller.model.TrackResponse;
import com.grademusic.main.model.Album;
import com.grademusic.main.model.Image;
import com.grademusic.main.model.Track;
import com.grademusic.main.model.lastfm.AlbumLastFm;
import com.grademusic.main.model.lastfm.ImageLastFm;
import com.grademusic.main.model.lastfm.TrackLastFm;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(config = MapperConfig.class)
public interface AlbumMapper {

    AlbumResponse toResponse(Album album);

    List<AlbumResponse> toResponse(List<Album> albums);

    @Mapping(source = "mbid", target = "id")
    @Mapping(source = "tracks.tracks", target = "tracks")
    Album fromLastFm(AlbumLastFm albumLastFm);

    List<Album> fromLastFm(List<AlbumLastFm> albumsLastFm);

    default Image fromLastFmImages(List<ImageLastFm> imagesLastFm) {
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

    @Mapping(source = "attributes.rank", target = "order")
    Track fromLastFm(TrackLastFm trackLastFm);

    List<Track> fromLastFmTracks(List<TrackLastFm> tracksLastFm);

    TrackResponse toResponse(Track track);

    List<TrackResponse> toResponses(List<Track> tracks);
}
