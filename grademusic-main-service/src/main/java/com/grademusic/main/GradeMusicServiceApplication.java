package com.grademusic.main;

import com.grademusic.main.service.cache.AlbumStatisticsCache;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GradeMusicServiceApplication {

	public static void main(String[] args) {
		var context = SpringApplication.run(GradeMusicServiceApplication.class, args);
		AlbumStatisticsCache albumStatisticsCache = context.getBean(AlbumStatisticsCache.class);
		var data = albumStatisticsCache.findById("6288a20e-3f11-45b9-a678-3cfe04377b31");

		System.out.println();
	}

}
