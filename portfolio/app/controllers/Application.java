package controllers;

import play.*;
import play.data.validation.Required;
import play.data.validation.URL;
import play.db.jpa.Blob;
import play.db.jpa.JPABase;
import play.libs.Files;
import play.libs.Images;
import play.libs.WS.HttpResponse;
import play.libs.WS.WSRequest;
import play.mvc.*;
import play.utils.Utils;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.*;

import com.google.gdata.client.photos.PicasawebService;
import com.google.gdata.data.photos.AlbumFeed;
import com.google.gdata.data.photos.PhotoEntry;
import com.google.gdata.util.ServiceException;

import models.*;

public class Application extends Controller {

	public static final File FILE_FOLDER = Play.getFile("public/pictures/");

	public static final String THUMBNAILS = "thumbnail";
	public static final String FULLSIZE = "fullsize";
	public static final String RESIZED = "resized";

	@Util
	public static List<Gallery> getGalleries() {
    	return Gallery.all().fetch();
	}
	
	@Util
	public static List<Gallery> getPicasaGalleries() {
    	return PicasaGallery.all().fetch();
	}
	
    public static void index() {
        if (Gallery.count() > 0) {
        	Gallery defaultGallery = Gallery.all().first();
			showGallery(defaultGallery.id, defaultGallery.name);
        } else {
        	render();
        }
    }
    
    public static void showImage(Long id) {
    	Content content = Content.findById(id);
    	notFoundIfNull(content);
		response.setContentTypeIfNotSet(content.image.type());
		File file = content.image.getFile();
		notFoundIfNull(file);
		renderBinary(file);
    }
    
    public static void showGallery(Long id, String name) {
    	notFoundIfNull(id);
    	Gallery gallery = Gallery.findById(id);
    	notFoundIfNull(gallery);
    	
    	List<Picture> pictures = gallery.getPictures();
    	List<ImageView> images = new ArrayList<ImageView>();
    	for (Picture picture : pictures) images.add(picture.toImageView());
    	
    	render("Application/gallery.html", images, gallery);
    }
    
    public static void showPicasaGallery(Long id, String name) {
    	notFoundIfNull(id);
    	PicasaGallery gallery = PicasaGallery.findById(id);
    	notFoundIfNull(gallery);
    	
    	PicasawebService service = new PicasawebService("portfolio");
    	List<PhotoEntry> photoEntries = Collections.emptyList();
		try {
			java.net.URL feedUrl = new java.net.URL(gallery.getFeedUrl());
			
			AlbumFeed feed = service.getFeed(feedUrl, AlbumFeed.class);
			photoEntries = feed.getPhotoEntries();
		} catch (MalformedURLException e) {
			Logger.error("Service URL for Picasa is not well formed");
			e.printStackTrace();
		} catch (IOException e) {
			Logger.error("Error I/O while communicating with Picasa Service");
			e.printStackTrace();
		} catch (ServiceException e) {
			Logger.error("Picasa service error");
			e.printStackTrace();
		}
 
		List<ImageView> images = new ArrayList<ImageView>();
		for (PhotoEntry entry : photoEntries) {
			ImageView image = new ImageView();
			// We take the largest
			image.thumbnail = entry.getMediaThumbnails().get(entry.getMediaThumbnails().size() - 1).getUrl();
			image.url = entry.getMediaContents().get(0).getUrl();
			images.add(image);
		}
		
		render("Application/gallery.html", images, gallery);
    }
    
    public static void showContents() {
    	List<Content> contents = Content.findAll();
    	render(contents);
    }

    public static void thumbnail(Long galleryId, Long pictureId) {
     	File picture = Picture.getFile(galleryId, pictureId, Application.THUMBNAILS);
     	if (! picture.exists()) notFound();
     	renderBinary(picture);
    }
    
    public static void resized(Long galleryId, Long pictureId) {
    	File picture = Picture.getFile(galleryId, pictureId, Application.RESIZED);
       	if (! picture.exists()) notFound();
     	renderBinary(picture);
    }
}