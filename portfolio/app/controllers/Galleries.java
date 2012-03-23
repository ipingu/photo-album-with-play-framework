package controllers;

import play.mvc.With;
import models.Gallery;

@With(Secure.class)
@CRUD.For(Gallery.class)
public class Galleries extends CRUD {

}
