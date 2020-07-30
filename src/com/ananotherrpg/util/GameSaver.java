package com.ananotherrpg.util;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import com.ananotherrpg.IIdentifiable;
import com.ananotherrpg.entity.Attributes.Attribute;
import com.ananotherrpg.entity.Entity;
import com.ananotherrpg.entity.PlayerAvatar;
import com.ananotherrpg.inventory.ItemStack;
import com.ananotherrpg.level.CampaignState;
import com.ananotherrpg.level.Location;
import com.ananotherrpg.level.Objective;
import com.ananotherrpg.level.Path;
import com.ananotherrpg.level.Quest;
import com.ananotherrpg.level.TallyObjective;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class GameSaver {

    private File file;

    public GameSaver(String filePath) {
        file = new File(filePath);
    }

    public void save(CampaignState state, PlayerAvatar avatar) {
        try {
            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();

            Element root = document.createElement("campaign");
            document.appendChild(root);

            Element e_campaignState = document.createElement("campaignState");
            root.appendChild(e_campaignState);
            {
                Element e_locationGraph = document.createElement("locationGraph");
                e_campaignState.appendChild(e_locationGraph);

                Element e_locations = document.createElement("locations");
                e_locationGraph.appendChild(e_locations);
                for (Location location : state.getLocationManager().getAllLocations()) {
                    Element e_location = generateIIdentifiableElement(document, "location", location);

                    for (Entity entity : location.getPermanentEntities()) {
                        Element e_entity = generateEntity(document, entity);
                        e_location.appendChild(e_entity);
                    }

                    Element e_itemsOnGround = document.createElement("itemsOnGround");
                    e_location.appendChild(e_itemsOnGround);
                    for (ItemStack itemStack : location.getInventory().getItems()) {
                        Element e_item = document.createElement("item");

                        e_item.setAttribute("id", String.valueOf(itemStack.getItem().getID()));
                        e_item.setAttribute("quantity", String.valueOf(itemStack.getQuantity()));

                        e_itemsOnGround.appendChild(e_item);
                    }
                    e_locations.appendChild(e_location);
                }

                Element e_paths = document.createElement("paths");
                e_locationGraph.appendChild(e_paths);

                for (Path path : state.getLocationManager().getAllPaths()) {
                    Element e_path = generateIIdentifiableElement(document, "path", path);
                    e_paths.appendChild(e_path);

                    Element e_pathlocations = document.createElement("locations");
                    e_path.appendChild(e_pathlocations);

                    Element e_pathlocation1 = document.createElement("location");
                    e_pathlocation1.setAttribute("id", String.valueOf(path.getNodes().get(0).getID()));

                    Element e_pathlocation2 = document.createElement("location");
                    e_pathlocation2.setAttribute("id", String.valueOf(path.getNodes().get(1).getID()));

                    e_pathlocations.appendChild(e_pathlocation1);
                    e_pathlocations.appendChild(e_pathlocation2);
                }
            }

            Element e_player = document.createElement("player");
            root.appendChild(e_player);
            {
                Element e_location = document.createElement("location");
                e_location.setAttribute("id", String.valueOf(avatar.getCurrentLocation().getID()));
                e_player.appendChild(e_location);

                Element e_entity = generateEntity(document, avatar.getEntity());
                e_player.appendChild(e_entity);

                Element e_questLog = document.createElement("questLog");
                e_player.appendChild(e_questLog);
                {
                    Element e_acceptedQuests = document.createElement("acceptedQuests");
                    e_player.appendChild(e_acceptedQuests);

                    for (Quest quest : avatar.getQuestLog().getAcceptedQuests()) {
                        Element e_quest = generateQuestElement(document, quest);
                        e_acceptedQuests.appendChild(e_quest);

                    }

                    Element e_completedQuests = document.createElement("completedQuests");
                    e_questLog.appendChild(e_completedQuests);

                    for (Quest quest : avatar.getQuestLog().getCompletedQuests()) {
                        Element e_quest = generateQuestElement(document, quest);
                        e_completedQuests.appendChild(e_quest);
                    }

                }

                Element e_knownPaths = document.createElement("knownPaths");
                e_player.appendChild(e_knownPaths);
                for (Integer pathID : avatar.getKnownPathIDs()) {
                    Element e_path = document.createElement("path");

                    e_path.setAttribute("id", String.valueOf(pathID));

                    e_knownPaths.appendChild(e_path);
                }
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(file);
            transformer.transform(source, result);
        } catch (ParserConfigurationException | TransformerException e) {
            e.printStackTrace();
        }
    }

    private Element generateQuestElement(Document document, Quest quest) {
        Element e_quest = generateIIdentifiableElement(document, "quest", quest);

        Element e_objectives = document.createElement("objectives");
        e_quest.appendChild(e_objectives);

        for (Objective objective : quest.getObjectives()) {

            Element e_objective = document.createElement("objective");
            e_objectives.appendChild(e_objective);
            e_objective.setAttribute("name", objective.getName());

            Element e_targetID = document.createElement("targetID");
            e_targetID.setTextContent(String.valueOf(objective.getTargetID()));
            e_objective.appendChild(e_targetID);

            Element e_gameEvent = document.createElement("gameEvent");
            e_gameEvent.setTextContent(objective.getAssociatedEvent().toString());
            e_objective.appendChild(e_gameEvent);

            if (objective instanceof TallyObjective) {
                TallyObjective obj = (TallyObjective) objective;
                e_objective.setAttribute("type", "tally");

                Element e_targetQuantity = document.createElement("targetQuantity");
                e_targetQuantity.setTextContent(String.valueOf(obj.getTargetQuantity()));
                e_objective.appendChild(e_targetQuantity);

                Element e_count = document.createElement("count");
                e_count.setTextContent(String.valueOf(obj.getCount()));
                e_objective.appendChild(e_count);

            }
        }
        return e_quest;
    }

    private Element generateEntity(Document document, Entity entity) {
        Element e_entity = generateIIdentifiableElement(document, "entity", entity);

        Element e_level = document.createElement("level");
        e_level.setTextContent(String.valueOf(entity.getLevel()));
        e_entity.appendChild(e_level);

        Element e_inventory = document.createElement("inventory");
        for (ItemStack itemStack : entity.getInventory().getItems()) {
            Element e_item = document.createElement("item");

            e_item.setAttribute("id", String.valueOf(itemStack.getItem().getID()));
            e_item.setAttribute("quantity", String.valueOf(itemStack.getQuantity()));
            e_inventory.appendChild(e_item);
        }
        e_entity.appendChild(e_inventory);

        Element e_dialogue = document.createElement("dialogue");
        {
            e_dialogue.setAttribute("id", String.valueOf(entity.getDialogue().getGraphID()));

            e_dialogue.setAttribute("start", String.valueOf(entity.getDialogue().getStartingLine().getLocalID()));

        }
        e_entity.appendChild(e_dialogue);

        Element e_attributes = document.createElement("attributes");
        for (Attribute attribute : Attribute.values()) {
            Element e_attribute = document.createElement("attribute");

            Attr a_type = document.createAttribute("type");
            a_type.setNodeValue(attribute.toString());
            e_attribute.setAttributeNode(a_type);

            e_attribute.setTextContent(String.valueOf(entity.getAttributes().getAttributePoints(attribute)));

            e_attributes.appendChild(e_attribute);
        }
        e_entity.appendChild(e_attributes);

        Element e_isDead = document.createElement("isDead");
        e_isDead.setTextContent(String.valueOf(entity.isDead()));
        e_entity.appendChild(e_isDead);

        return e_entity;
    }

    private Element generateIIdentifiableElement(Document doc, String tagName, IIdentifiable identifable) {
        Element e_identifiable = doc.createElement(tagName);

        e_identifiable.setAttribute("id", String.valueOf(identifable.getID()));
        e_identifiable.setAttribute("name", identifable.getName());

        Element e_description = doc.createElement("description");
        e_description.setTextContent(identifable.getDescription());
        e_identifiable.appendChild(e_description);

        return e_identifiable;
    }

}