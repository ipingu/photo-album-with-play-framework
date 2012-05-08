package jobs;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import controllers.Galleries;

import models.Gallery;
import models.Picture;
import play.Logger;
import play.Play;
import play.jobs.Job;
import play.jobs.OnApplicationStart;
import play.libs.WS;
import play.libs.WS.HttpResponse;
import play.libs.WS.WSRequest;
import play.libs.ws.WSAsync;
import play.mvc.Http;
import play.templates.JavaExtensions;

/**
 * Create few galleries and fill them with pictures from placeholdit.
 */
@OnApplicationStart
public class LoadPicturesFromPlaceholdIt extends Job{

	private byte[] buffer = new byte[1024];
	
	private static Map<String, Integer> albums  = new HashMap<String, Integer>() {{
		put("Alpha", 8);
		put("Beta", 0);
		put("Delta", 16);
		put("Gamma", 11);
		put("Epsilon", 31);
	}};
	
	@Override
	public void doJob() throws Exception {
		if (! Play.configuration.getProperty("application.mode").equals("dev")) return;
		if (Picture.count() > 0) return;

		Logger.info("Dev mode & no pictures yet saved.\nStarting to load data from placehold.it");
		
		for (Map.Entry<String, Integer> entry : albums.entrySet()) {
			Gallery gallery = Gallery.create(entry.getKey());

			for (int i = 0; i < entry.getValue(); i++) {
				File image = getImageFromUrl("http://placehold.it/800x600/" + 
					getRandomColorInHex() + "/" + "000000" + "/" +
					"&text=Image+" + i);
				
				if (image != null) Galleries.addFileToGallery(image, gallery.id, "Image " + i);
			}
		}
		
		Logger.info("Data are loaded");
	}
	
	public File getImageFromUrl(String url) {
		Logger.info("Retrieve file from " + url);
		HttpResponse response = WS.url(url).get();
		if (200 == response.getStatus()) {
			File file = new File(Play.tmpDir, UUID.randomUUID().toString() + ".jpg");
			try {
				FileOutputStream fos = new FileOutputStream(file);
				InputStream inputStream = response.getStream();
				
				int count = 0;
				while ((count = inputStream.read(buffer)) > 0) {
					fos.write(buffer, 0, count);
				}

				fos.close();
				Logger.info("Image has been saved in file %s", file.getAbsolutePath());
				
				return file;
			} catch (FileNotFoundException e) {
				Logger.warn("File cannot be opened for storing image", e);
			} catch (IOException e) {
				Logger.warn("IO error while storing image", e);
			}
		} else {
			Logger.warn("Error : response returns " + response.getStatus() + " code");
		}
		
		return null;
	}

	// FIXME random fail
	private String getRandomColorInHex() {
		Random rand = new Random(System.currentTimeMillis());
		String color = "";
		while (color.length() < 6) color += Integer.toHexString(rand.nextInt(255));
		return color;
	}
}