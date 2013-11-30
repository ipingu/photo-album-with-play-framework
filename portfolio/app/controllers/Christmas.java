package controllers;

import java.util.List;
import java.util.Map;

import models.Child;
import play.mvc.Controller;
import play.mvc.With;

@With(Secure.class)
public class Christmas extends Controller {

	public static void prepareParticipants() {
		render("Misc/showParticipants.html");
	}

	public static void sendMailToParticipants(List<String> name,
			List<String> email) {
		RandomChristmasGift randomChristmas = new RandomChristmasGift();

		for (int i = 0; i < name.size(); i++) {
			String childName = name.get(i);
			String childEmail = email.get(i);
			if (childName.length() > 0 && childEmail.length() > 0) {
				randomChristmas.addChild(childEmail, childName);
			}
		}

		Map<Child, Child> invitations = randomChristmas.getInvitations();
		if (invitations.isEmpty()) {
			flash("noChildren", "Please add at least one child...");
			prepareParticipants();
		} else {
			String proofOfValidity = randomChristmas.sendInvitations(invitations);
			redirect("christmas.ChristmasMailSent", proofOfValidity);
		}
	}

	public static void christmasMailSent(String proofOfValidity) {
		renderArgs.put("proofOfValidity", proofOfValidity);
		render("/Misc/ChristmasMailSent.html");
	}
}
