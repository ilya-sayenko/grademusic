package com.grademusic.main.mapper;

import com.grademusic.main.controller.model.AlbumResponse;
import com.grademusic.main.controller.model.TrackResponse;
import com.grademusic.main.model.Album;
import com.grademusic.main.model.Track;
import com.grademusic.main.model.lastfm.AlbumLastFm;
import com.grademusic.main.model.lastfm.ImageLastFm;
import com.grademusic.main.model.lastfm.TrackLastFm;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.List;

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AlbumMapperTest {

    AlbumMapper mapper = Mappers.getMapper(AlbumMapper.class);

    @Test
    public void shouldMapToAlbumResponse() {
        Album album = Instancio.create(Album.class);
        AlbumResponse response = mapper.toResponse(album);

        assertEquals(album.getId(), response.id());
        assertEquals(album.getName(), response.name());
        assertEquals(album.getArtist(), response.artist());
        assertEquals(album.getGrade(), response.grade());
        assertEquals(album.getImage().getSmall(), response.image().small());
        assertEquals(album.getImage().getMedium(), response.image().medium());
        assertEquals(album.getImage().getLarge(), response.image().large());
        assertEquals(album.getImage().getExtralarge(), response.image().extralarge());
        List<Track> tracks = album.getTracks();
        List<TrackResponse> trackResponses = response.tracks();
        for (int i = 0; i < tracks.size(); i++) {
            Track track = tracks.get(i);
            TrackResponse trackResponse = trackResponses.get(i);
            assertEquals(track.getId(), trackResponse.id());
            assertEquals(track.getName(), trackResponse.name());
            assertEquals(track.getDuration(), trackResponse.duration());
            assertEquals(track.getOrder(), trackResponse.order());
        }
    }

    @Test
    public void shouldMapAlbumFromLastFm() {
        List<ImageLastFm> imageLastFmList = List.of(
                ImageLastFm.builder().size("small").text("small_url").build(),
                ImageLastFm.builder().size("medium").text("small_medium").build(),
                ImageLastFm.builder().size("large").text("small_large").build(),
                ImageLastFm.builder().size("extralarge").text("small_extralarge").build()
        );
        AlbumLastFm albumLastFm = Instancio.of(AlbumLastFm.class)
                .set(field(AlbumLastFm::image), imageLastFmList)
                .create();
        Album album = mapper.fromLastFm(albumLastFm);

        assertEquals(albumLastFm.mbid(), album.getId());
        assertEquals(albumLastFm.name(), album.getName());
        assertEquals(albumLastFm.artist(), album.getArtist());
        assertEquals(imageLastFmList.get(0).text(), album.getImage().getSmall());
        assertEquals(imageLastFmList.get(1).text(), album.getImage().getMedium());
        assertEquals(imageLastFmList.get(2).text(), album.getImage().getLarge());
        assertEquals(imageLastFmList.get(3).text(), album.getImage().getExtralarge());
        List<TrackLastFm> trackLastFmList = albumLastFm.tracks().tracks();
        List<Track> tracks = album.getTracks();
        for (int i = 0; i < trackLastFmList.size(); i++) {
            TrackLastFm trackLastFm = trackLastFmList.get(i);
            Track track = tracks.get(i);
            assertEquals(trackLastFm.name(), track.getName());
            assertEquals(trackLastFm.duration(), track.getDuration());
            assertEquals(trackLastFm.attributes().rank(), track.getOrder());
        }
    }
}
