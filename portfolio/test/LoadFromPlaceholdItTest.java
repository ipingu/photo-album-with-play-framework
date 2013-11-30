import java.io.File;
import java.util.Map;

import jobs.LoadPicturesFromPlaceholdIt;

import models.Gallery;
import models.Picture;

import org.junit.Assert;
import org.junit.Test;

import play.Logger;
import play.Play;
import play.libs.WS;
import play.libs.WS.WSRequest;
import play.test.UnitTest;


public class LoadFromPlaceholdItTest extends UnitTest {
	
	@Test
	public void getImageFromUrl() {
		LoadPicturesFromPlaceholdIt job = new LoadPicturesFromPlaceholdIt();
		File file = job.getImageFromUrl("http://placehold.it/350x150/AAAAAA/00AA00/&text=Image+1");
		Assert.assertNotNull(file);
		Assert.assertTrue(file.exists());
	}

}
