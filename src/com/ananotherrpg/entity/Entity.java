package com.ananotherrpg.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.ananotherrpg.Identifiable;
import com.ananotherrpg.entity.dialogue.DialogueLine;
import com.ananotherrpg.inventory.Inventory;
import com.ananotherrpg.util.Link;
import com.ananotherrpg.util.LinkedDirectedGraph;

public class Entity implements Identifiable {
	private int entityId;

	protected String name;

	protected LinkedDirectedGraph<DialogueLine, String> dialogue;

	protected int hp;
	protected int maxHealth;
	protected Inventory inventory;

	protected Boolean isDead;

	public Entity(int entityId) {
		this.entityId = entityId;
	}

	public Entity(String name, int hp, int maxHealth, Inventory inventory) {
		this.name = name;
		this.hp = hp;
		this.maxHealth = maxHealth;
		this.inventory = inventory;
		this.isDead = false;
		this.dialogue = Entity.NO_DIALOGUE;
	}

	public Entity(String name, int hp, int maxHealth, Inventory inventory, Boolean isDead, Boolean isKnown,
	LinkedDirectedGraph<DialogueLine, String> dialogue) {
		this.name = name;
		this.hp = hp;
		this.maxHealth = maxHealth;
		this.inventory = inventory;
		this.isDead = isDead;;
		this.dialogue = dialogue;
	}

	public Boolean IsDead() {
		return isDead;
	}

	public void setIsDead(Boolean isDead) {
		this.isDead = isDead;
	}

	public String getName() {
		return name;
	}

	public LinkedDirectedGraph<DialogueLine, String> getDialogueGraph(){
		return dialogue;
	}

	public static final LinkedDirectedGraph<DialogueLine,String> NO_DIALOGUE;
    static{
        DialogueLine noDialogueLine = new DialogueLine("They don't seem interested in talking");

        HashMap<DialogueLine, List<Link<DialogueLine,String>>> noDialogueMap = new HashMap<DialogueLine, List<Link<DialogueLine,String>>>();
        noDialogueMap.putIfAbsent(noDialogueLine, new ArrayList<Link<DialogueLine, String>>());

        NO_DIALOGUE = new LinkedDirectedGraph<DialogueLine,String>(noDialogueMap, noDialogueLine);
    }		
}
