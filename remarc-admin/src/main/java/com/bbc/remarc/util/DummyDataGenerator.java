/*
 * Copyright (C) 2016 BBC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bbc.remarc.util;

import com.bbc.remarc.db.MongoClient;
import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DB;
import com.mongodb.DBCollection;

/**
 * Generate some dummy mongo db records
 * 
 * @author Peter Brock
 */
public class DummyDataGenerator {

	public static void saveMetaData() {

		DB db = MongoClient.getDB();

		try {
			db.requestStart();
			generateImages(db);
			generateAudio(db);
			generateVideo(db);
		} finally {
			db.requestDone();
		}
	}

	private static void generateVideo(DB db) {

		DBCollection video = db.getCollection("video");

		video.remove(new BasicDBObject()); 

		/*
		video.insert(BasicDBObjectBuilder.start()
				.add("imageUrl", "/remarc_resources/video/cat.jpg")
				.add("theme", "Animals/Pets").add("decade", "1970s")
				.add("id", "cat")
				.add("mp4ContentUrl", "/remarc_resources/video/cat.mp4")
				.add("ogvContentUrl", "/remarc_resources/video/cat.ogv")
				.get());
				*/

	}

	private static void generateAudio(DB db) {

		DBCollection audio = db.getCollection("audio");

		audio.remove(new BasicDBObject()); 

		/*
		audio.insert(BasicDBObjectBuilder.start()
				.add("imageUrl", "/remarc_resources/audio/pirates.jpg")
				.add("theme", "Music").add("decade", "1970s")
				.add("id", "pirates")
				.add("mp3ContentUrl", "/remarc_resources/audio/pirates.mp3")
				.add("oggContentUrl", "/remarc_resources/audio/pirates.ogg")
				.get());

		audio.insert(BasicDBObjectBuilder
				.start()
				.add("imageUrl",
						"/remarc_resources/audio/choir-der-sommer.jpg")
				.add("theme", "Music")
				.add("decade", "1970s")
				.add("id", "choir-der-sommer")
				.add("mp3ContentUrl",
						"/remarc_resources/audio/choir-der-sommer.mp3")
				.add("oggContentUrl",
						"/remarc_resources/audio/choir-der-sommer.ogg").get());

		audio.insert(BasicDBObjectBuilder
				.start()
				.add("imageUrl", "/remarc_resources/audio/circus-lenclume.jpg")
				.add("theme", "Music")
				.add("decade", "1970s")
				.add("id", "circus-lenclume")
				.add("mp3ContentUrl",
						"/remarc_resources/audio/circus-lenclume.mp3")
				.add("oggContentUrl",
						"/remarc_resources/audio/circus-lenclume.ogg").get());

		audio.insert(BasicDBObjectBuilder
				.start()
				.add("imageUrl", "/remarc_resources/audio/handel-feet.jpg")
				.add("theme", "Music")
				.add("decade", "1970s")
				.add("id", "handel-feet")
				.add("mp3ContentUrl",
						"/remarc_resources/audio/handel-feet.mp3")
				.add("oggContentUrl",
						"/remarc_resources/audio/handel-feet.ogg").get());

		audio.insert(BasicDBObjectBuilder
				.start()
				.add("imageUrl", "/remarc_resources/audio/krackatoa-time.jpg")
				.add("theme", "Music")
				.add("decade", "1970s")
				.add("id", "krackatoa-time")
				.add("mp3ContentUrl",
						"/remarc_resources/audio/krackatoa-time.mp3")
				.add("oggContentUrl",
						"/remarc_resources/audio/krackatoa-time.ogg").get());

		audio.insert(BasicDBObjectBuilder
				.start()
				.add("imageUrl", "/remarc_resources/audio/megatone-black.jpg")
				.add("theme", "Music")
				.add("decade", "1970s")
				.add("id", "megatone-black")
				.add("mp3ContentUrl",
						"/remarc_resources/audio/megatone-black.mp3")
				.add("oggContentUrl",
						"/remarc_resources/audio/megatone-black.ogg").get());

		audio.insert(BasicDBObjectBuilder
				.start()
				.add("imageUrl",
						"/remarc_resources/audio/mozart-laudate-dominum.jpg")
				.add("theme", "Music")
				.add("decade", "1970s")
				.add("id", "mozart-laudate-dominum")
				.add("mp3ContentUrl",
						"/remarc_resources/audio/mozart-laudate-dominum.mp3")
				.add("oggContentUrl",
						"/remarc_resources/audio/mozart-laudate-dominum.ogg")
				.get());

		audio.insert(BasicDBObjectBuilder
				.start()
				.add("imageUrl",
						"/remarc_resources/audio/Soniventorum-danzi.jpg")
				.add("theme", "Music")
				.add("decade", "1970s")
				.add("id", "Soniventorum-danzi")
				.add("mp3ContentUrl",
						"/remarc_resources/audio/Soniventorum-danzi.mp3")
				.add("oggContentUrl",
						"/remarc_resources/audio/Soniventorum-danzi.ogg")
				.get());
		
		//Note: these have no images associated - this is to test the fallback
		audio.insert(BasicDBObjectBuilder
				.start()
				.add("theme", "Music")
				.add("decade", "1930s")
				.add("id", "audio-no-image-1")
				.add("mp3ContentUrl",
						"/remarc_resources/audio/Soniventorum-danzi.mp3")
				.add("oggContentUrl",
						"/remarc_resources/audio/Soniventorum-danzi.ogg")
				.get());
		*/
	}

	private static void generateImages(DB db) {

		DBCollection images = db.getCollection("images");

		images.remove(new BasicDBObject()); 

		/*
		images.insert(BasicDBObjectBuilder.start()
				.add("imageUrl", "/remarc_resources/images/beatles.jpg")
				.add("theme", "Music").add("decade", "1970s")
				.add("id", "beatles").get());

		images.insert(BasicDBObjectBuilder.start()
				.add("imageUrl", "/remarc_resources/images/cars.jpg")
				.add("theme", "Travel").add("decade", "1960s")
				.add("id", "cars").get());

		images.insert(BasicDBObjectBuilder.start()
				.add("imageUrl", "/remarc_resources/images/horse_dentist.jpg")
				.add("theme", "Animals/Pets").add("decade", "1980s")
				.add("id", "horse_dentist.jpg").get());

		images.insert(BasicDBObjectBuilder.start()
				.add("imageUrl", "/remarc_resources/images/sample_1.jpg")
				.add("theme", "Family").add("decade", "1980s")
				.add("id", "sample_1").get());

		images.insert(BasicDBObjectBuilder.start()
				.add("imageUrl", "/remarc_resources/images/sample_2.jpg")
				.add("theme", "People").add("decade", "1950s")
				.add("id", "sample_2").get());

		images.insert(BasicDBObjectBuilder.start()
				.add("imageUrl", "/remarc_resources/images/sample_3.jpg")
				.add("theme", "Family").add("decade", "1960s")
				.add("id", "sample_3").get());

		images.insert(BasicDBObjectBuilder.start()
				.add("imageUrl", "/remarc_resources/images/war.jpg")
				.add("theme", "Events").add("decade", "1940s").add("id", "war")
				.get());

		images.insert(BasicDBObjectBuilder.start()
				.add("imageUrl", "/remarc_resources/images/bbc.jpg")
				.add("theme", "Film").add("decade", "1960s").add("id", "bbc")
				.get());

		images.insert(BasicDBObjectBuilder.start()
				.add("imageUrl", "/remarc_resources/images/hunting.jpg")
				.add("theme", "Work").add("decade", "1970s")
				.add("id", "hunting").get());

		images.insert(BasicDBObjectBuilder.start()
				.add("imageUrl", "/remarc_resources/images/indians.JPG")
				.add("theme", "Leisure").add("decade", "1960s")
				.add("id", "indians").get());

		images.insert(BasicDBObjectBuilder.start()
				.add("imageUrl", "/remarc_resources/images/new_york.jpg")
				.add("theme", "Travel").add("decade", "1960s")
				.add("id", "new_york").get());

		images.insert(BasicDBObjectBuilder.start()
				.add("imageUrl", "/remarc_resources/images/parade.jpg")
				.add("theme", "Events").add("decade", "1920s")
				.add("id", "parade").get());

		images.insert(BasicDBObjectBuilder.start()
				.add("imageUrl", "/remarc_resources/images/records.jpg")
				.add("theme", "Leisure").add("decade", "1980s")
				.add("id", "records").get());

		images.insert(BasicDBObjectBuilder.start()
				.add("imageUrl", "/remarc_resources/images/sentry.jpg")
				.add("theme", "Events").add("decade", "1920s")
				.add("id", "sentry").get());

		images.insert(BasicDBObjectBuilder.start()
				.add("imageUrl", "/remarc_resources/images/tamer.jpg")
				.add("theme", "Leisure").add("decade", "1950s")
				.add("id", "tamer").get());
		
		//Note: these have no images associated - this is to test the fallback
		images.insert(BasicDBObjectBuilder.start()
				.add("theme", "Work").add("decade", "1970s")
				.add("id", "img-no-image-1").get());
		
		images.insert(BasicDBObjectBuilder.start()
				.add("theme", "Sport").add("decade", "1980s")
				.add("id", "img-no-image-2").get());
				*/
	}
}
