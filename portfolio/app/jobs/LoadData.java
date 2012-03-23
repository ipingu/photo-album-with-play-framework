package jobs;

import controllers.Application;
import models.Gallery;
import play.Logger;
import play.Play;
import play.jobs.Job;
import play.jobs.OnApplicationStart;
import play.test.Fixtures;

/**
 * Exécuté au premier chargement de page, et lors d'un rechargement de classes.
 * @author imac2
 *
 */
@OnApplicationStart
public class LoadData extends Job {

	private static final String INITIAL_DATASET = "initial-dataset.yml";
	
	@Override
	public void doJob() throws Exception {
		Logger.info("LoadData job in execution");
		
		if (! Play.configuration.getProperty("application.mode").equals("dev")) return;

		if (Gallery.count() == 0) {
			Logger.info("Load initial dataset ...");
			Fixtures.loadModels(INITIAL_DATASET);
			Logger.info("Dataset is initialized");
		} else {
			Logger.info("Dataset is already initialized - skip data load");
		}
		
		// For each created album, we ensure folders have been created too
		for (Object gallery: Gallery.all().fetch()) {
			Application.prepareFoldersForGallery((Gallery) gallery);
		}
	}

}
