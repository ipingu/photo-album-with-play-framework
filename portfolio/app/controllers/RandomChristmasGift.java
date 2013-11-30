package controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import notifiers.Mails;

import models.Child;
import play.Logger;
import play.mvc.Mailer;


public class RandomChristmasGift {
    
    private List<Child> children = new ArrayList<Child>();
    
    public RandomChristmasGift() {
        super();
    }
    
    public static void main(String[] args) {
        RandomChristmasGift santa = new RandomChristmasGift();
        santa.addChild("erik@me.com", "Erik");
        santa.addChild("joe@me.com", "Manon");
        santa.addChild("jane@me.com", "Marie");
        santa.addChild("rogers@me.com", "Melissa");
        Map<Child, Child> invitations = santa.getInvitations();
        santa.sendInvitations(invitations);
    }

    public void addChild(String email, String name) {
		Child child = new Child(email, name, this.children.size() + 1);
		
//	        if (children.contains(child)) {
//	            throw new IllegalArgumentException("Child already registered to this Christmas party");
//	        }
		children.add(child);
	}

    public Map<Child, Child> getInvitations() {
    	if (children.isEmpty()) return Collections.EMPTY_MAP;
    	
        Collections.shuffle(children, new Random(System.nanoTime()));
        
        Map<Child,Child> invitations = new HashMap<Child,Child>();
        
        for (int i = 0; i < children.size() - 1; i++) {
            invitations.put(children.get(i), children.get(i+1));
        }
        invitations.put(children.get(children.size() - 1), children.get(0));

        return invitations;
    }

    public String sendInvitations(Map<Child, Child> invitations) {
        StringBuilder proofOfValidity = new StringBuilder();
        for (Map.Entry<Child,Child> entry : invitations.entrySet()) {
            proofOfValidity.append(entry.getKey().getSecretName() + " offre un cadeau à " + entry.getValue().getName() + "\n");
        }

        for (Map.Entry<Child,Child> entry : invitations.entrySet()) {
        	Mails.sendMessageForChristmas(entry.getKey(), entry.getValue(), proofOfValidity.toString());
        }
        
        return proofOfValidity.toString();
    }

}
