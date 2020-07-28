package com.ananotherrpg;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import com.ananotherrpg.entity.AttributeModifier;
import com.ananotherrpg.entity.Attributes;
import com.ananotherrpg.entity.Attributes.Attribute;
import com.ananotherrpg.entity.Entity;
import com.ananotherrpg.entity.PlayerAvatar;
import com.ananotherrpg.entity.QuestLog;
import com.ananotherrpg.entity.dialogue.Dialogue;
import com.ananotherrpg.entity.dialogue.DialogueGraph;
import com.ananotherrpg.entity.dialogue.DialogueLine;
import com.ananotherrpg.entity.dialogue.PathDialogueLine;
import com.ananotherrpg.entity.dialogue.QuestDialogueLine;
import com.ananotherrpg.entity.dialogue.Response;
import com.ananotherrpg.event.EventDispatcher.GameEvent;
import com.ananotherrpg.inventory.Inventory;
import com.ananotherrpg.inventory.Item;
import com.ananotherrpg.inventory.Potion;
import com.ananotherrpg.inventory.Weapon;
import com.ananotherrpg.io.IOManager;
import com.ananotherrpg.level.CampaignData;
import com.ananotherrpg.level.CampaignState;
import com.ananotherrpg.level.EntityTemplate;
import com.ananotherrpg.level.Location;
import com.ananotherrpg.level.LocationGraph;
import com.ananotherrpg.level.Loot;
import com.ananotherrpg.level.Objective;
import com.ananotherrpg.level.Path;
import com.ananotherrpg.level.Quest;
import com.ananotherrpg.level.QuestTemplate;
import com.ananotherrpg.level.TallyObjective;
import com.ananotherrpg.util.DirectedGraph;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class GameLoader {

    private String filePath;

    public GameLoader(String filePath) {
        this.filePath = filePath;
    }

    public Game loadGame() {
        Document doc = getDocumentFromSaveFile();
        doc.getDocumentElement().normalize();

        Game game;

        try {
            Element docElement = doc.getDocumentElement();
            // First load campaign data which is stateless
            CampaignData campaignData;
            {
                Element campaignDataElement = (Element) docElement.getElementsByTagName("campaignData").item(0);

                String name = getTextValue(campaignDataElement, "name");
                String introduction = getTextValue(campaignDataElement, "introduction");

                // Entity templates
                NodeList entityTemplateNodeList = campaignDataElement.getElementsByTagName("entityTemplate");
                List<EntityTemplate> entityTemplates = new ArrayList<EntityTemplate>();

                for (int i = 0; i < entityTemplateNodeList.getLength(); i++) {
                    Node entityTemplateNode = entityTemplateNodeList.item(i);
                    if (entityTemplateNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element entityTemplateElement = (Element) entityTemplateNode;
                        entityTemplates.add(generateEntityTemplate(entityTemplateElement));
                    }
                }

                // Quest templates
                List<QuestTemplate> questTemplates = new ArrayList<QuestTemplate>();
                NodeList questTemplateNodeList = campaignDataElement.getElementsByTagName("questTemplate");

                for (int i = 0; i < questTemplateNodeList.getLength(); i++) {
                    Node questTemplateNode = questTemplateNodeList.item(i);
                    if (questTemplateNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element questTemplateElement = (Element) questTemplateNode;
                        questTemplates.add(generateQuestTemplate(questTemplateElement));
                    }
                }

                // Campaign items
                List<Item> items = new ArrayList<Item>();
                NodeList itemsNodeList = campaignDataElement.getElementsByTagName("item");

                for (int i = 0; i < itemsNodeList.getLength(); i++) {
                    Node itemNode = itemsNodeList.item(i);
                    if (itemNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element itemElement = (Element) itemNode;
                        items.add(generateItem(itemElement));
                    }
                }

                // Dialogue Graphs
                List<DialogueGraph> dialogueGraphs = new ArrayList<DialogueGraph>();
                NodeList dialogueNodeList = campaignDataElement.getElementsByTagName("dialogueGraph");
                for (int i = 0; i < dialogueNodeList.getLength(); i++) {
                    Node dialogueNode = dialogueNodeList.item(i);
                    if (dialogueNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element dialogueElement = (Element) dialogueNode;
                        dialogueGraphs.add(generateDialogueGraph(dialogueElement, questTemplates));
                    }
                }

                campaignData = new CampaignData(name, introduction, entityTemplates, dialogueGraphs, questTemplates,
                        items);

            }

            // Second campaign STATE
            CampaignState campaignState;
            {
                Element campaignDataElement = (Element) docElement.getElementsByTagName("campaignState").item(0);

                // Locations
                Element locationsElement = (Element) docElement.getElementsByTagName("locations").item(0);
                LocationGraph locationGraph = generateLocationGraph(locationsElement, campaignData);

                campaignState = new CampaignState(locationGraph);
            }

            // Third P L A Y E R
            PlayerAvatar player;
            {
                Element playerElement = (Element) docElement.getElementsByTagName("player").item(0);

                Element entityElement = (Element) playerElement.getElementsByTagName("entity").item(0);

                List<Quest> acceptedQuests = new ArrayList<Quest>();
                {
                    Element acceptedQuestsElement = (Element) playerElement.getElementsByTagName("acceptedQuests").item(0);
                    NodeList questList = acceptedQuestsElement.getElementsByTagName("quest");
                    for (int i = 0; i < questList.getLength(); i++) {
                        Node questNode = questList.item(i);
                        if (questNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element questElement = (Element) questNode;
                            acceptedQuests.add(generateQuest(questElement));
                        }
                    }
                }

                List<Quest> completedQuests = new ArrayList<Quest>();
                {
                    Element completedQuestsElement = (Element) playerElement.getElementsByTagName("completedQuests").item(0);
                    NodeList questList = completedQuestsElement.getElementsByTagName("quest");
                    for (int i = 0; i < questList.getLength(); i++) {
                        Node questNode = questList.item(i);
                        if (questNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element questElement = (Element) questNode;
                            completedQuests.add(generateQuest(questElement));
                        }
                    }
                }
                
                List<Integer> knownPaths = new ArrayList<Integer>();
                {
                    Element knownPathsElement = (Element) playerElement.getElementsByTagName("knownPaths").item(0);
                    NodeList pathList = knownPathsElement.getElementsByTagName("path");
                    for (int i = 0; i < pathList.getLength(); i++) {
                        Node pathNode = pathList.item(i);
                        if (pathNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element pathElement = (Element) pathNode;
                            knownPaths.add(getIDFromElement(pathElement));
                        }
                    }
                }

                QuestLog log = new QuestLog(acceptedQuests, completedQuests);
                Entity playerEntity = generateEntity(entityElement, campaignData);
                Location currentLocation = campaignState.getLocationByID(getIDFromChildElement(playerElement, "location"));

               player = new PlayerAvatar(playerEntity, currentLocation, knownPaths, log);
            }

            game = new Game(new Campaign(campaignData, campaignState, player));

        } catch (Exception e) {
            e.printStackTrace();
            IOManager.println("Loading failed");
            return null;
        }

        return game;

    }

    private Quest generateQuest(Element questElement) {
        int questID = Integer.parseInt(questElement.getAttribute("id"));
        String name = questElement.getAttribute("name");

        NodeList childNodes = questElement.getChildNodes();
        String description = childNodes.item(0).getTextContent();

        Element objectivesElement = (Element) questElement.getElementsByTagName("objectives").item(0);

        List<Objective> objectives = new ArrayList<Objective>();

        NodeList objectiveNodes = objectivesElement.getElementsByTagName("objective");
        for (int i = 0; i < objectiveNodes.getLength(); i++) {
            if (objectiveNodes.item(i).getNodeType() == Node.ELEMENT_NODE) {
                Element objective = (Element) objectiveNodes.item(i);
                if (objective.getAttribute("type").equals("tally")) {
                    int targetID = getIntValue(objective, "targetID");

                    GameEvent gameEvent = GameEvent.valueOf(getTextValue(objective, "gameEvent"));

                    int targetQuantity = getIntValue(objective, "targetQuantity");
                    int count = getIntValue(objective, "count");

                    objectives.add(new TallyObjective(objective.getAttribute("name"), targetID, gameEvent, targetQuantity, count));
                }
            }
        }

        return new Quest(questID, name, description, objectives);
    }

    private LocationGraph generateLocationGraph(Element locationsElement, CampaignData data) {
        List<Location> locations = new ArrayList<Location>();
        NodeList locationNodeList = locationsElement.getElementsByTagName("location");
        for (int i = 0; i < locationNodeList.getLength(); i++) {
            Node locationNode = locationNodeList.item(i);
            if (locationNode.getNodeType() == Node.ELEMENT_NODE) {
                locations.add(generateLocation(((Element) locationNode), data));

            }
        }

        Map<Integer, Location> locationToIDMap = locations.stream().collect(Collectors.toMap(Location::getID, Function.identity()));
        List<Path> paths = new ArrayList<Path>();

        NodeList pathList = locationsElement.getElementsByTagName("path");
        for (int i = 0; i < pathList.getLength(); i++) {
            Node pathNode = pathList.item(i);
            if (pathNode.getNodeType() == Node.ELEMENT_NODE) {
                paths.add(generatePath(((Element) pathNode), data, locationToIDMap));

            }
        }

        LocationGraph locGraph = new LocationGraph();

        for (Location location : locations) {
            locGraph.addNewLocation(location);
        }
        for (Path path : paths) {
            locGraph.addPath(path);
        }

        return locGraph;
    }

    private Path generatePath(Element pathElement, CampaignData data, Map<Integer, Location> locationToIDMap) {
        int pathID = Integer.parseInt(pathElement.getAttribute("id"));
        String name = pathElement.getAttribute("name"); 
        
        String description = getTextValue(pathElement, "description");
        boolean isTraversible = Boolean.parseBoolean(getTextValue(pathElement, "isTraversible"));
        
        List<Location> locations = new ArrayList<Location>();
        NodeList locationNodeList = pathElement.getElementsByTagName("location");
        for (int i = 0; i < locationNodeList.getLength(); i++) {
            Node locationNode = locationNodeList.item(i);
            if (locationNode.getNodeType() == Node.ELEMENT_NODE) {
                locations.add(locationToIDMap.get(getIDFromElement((Element) locationNode)));
            }
        }

        return new Path(pathID, name, description, locations.get(0), locations.get(1), isTraversible);
    }

    private Location generateLocation(Element locationElement, CampaignData data) {
        int locationID = Integer.parseInt(locationElement.getAttribute("id"));
        String name = locationElement.getAttribute("name");        
        String description = getTextValue(locationElement, "Lobby");

        List<Entity> entities = new ArrayList<Entity>();
        NodeList entitiesNodeList = locationElement.getElementsByTagName("entity");
        for (int i = 0; i < entitiesNodeList.getLength(); i++) {
            Node entityNode = entitiesNodeList.item(i);
            if (entityNode.getNodeType() == Node.ELEMENT_NODE) {
                entities.add(generateEntity((Element) entityNode, data));
            }
        }

        Inventory locationInventory = new Inventory();
        NodeList itemList = locationElement.getElementsByTagName("item");
        for (int i = 0; i < itemList.getLength(); i++) {
            Node itemNode = itemList.item(i);
            if (itemNode.getNodeType() == Node.ELEMENT_NODE) {
                Element itemElement = (Element) itemNode;
                locationInventory.addToInventory(data.getItemByID(getIDFromElement(itemElement)), Integer.parseInt(itemElement.getAttribute("quantity")));
            }
        }

        return new Location(locationID, name, description, entities, locationInventory);
    }

    private Entity generateEntity(Element entityElement, CampaignData data) {
        int entityID = Integer.parseInt(entityElement.getAttribute("id"));
        String name = entityElement.getAttribute("name");        
        String description = getTextValue(entityElement, "Lobby");

        int level = getIntValue(entityElement, "level");

        Inventory inventory = new Inventory(); 
        NodeList itemNodeList = entityElement.getElementsByTagName("item");
        for (int i = 0; i < itemNodeList.getLength(); i++) {
            Node itemNode =  itemNodeList.item(i);
            if (itemNode.getNodeType() == Node.ELEMENT_NODE) {
                Element itemElement = (Element) itemNode;
                inventory.addToInventory(data.getItemByID(getIDFromElement(itemElement)), Integer.parseInt(itemElement.getAttribute("quantity")));
            }
        }

        Dialogue dialogue = new Dialogue(data.getDialogueByID(getIDFromChildElement(entityElement, "dialogue")));

        
        NodeList attributeNodes = entityElement.getElementsByTagName("attribute");
        EnumMap<Attribute, Integer> attributes = new EnumMap<Attribute, Integer>(Attribute.class);
        for (int i = 0; i < attributeNodes.getLength(); i++) {
            Element e = (Element) attributeNodes.item(i);

            attributes.put(Attribute.valueOf(e.getAttribute("type")), Integer.parseInt(e.getTextContent()));
        }

        boolean isDead = Boolean.parseBoolean(getTextValue(entityElement, "isDead"));

        return new Entity(entityID, name, description, new Attributes(attributes), level, inventory, dialogue, isDead);
    }

    private DialogueGraph generateDialogueGraph(Element dialogueElement, List<QuestTemplate> questTemplates) {
        int dialogueID = Integer.parseInt(dialogueElement.getAttribute("id"));
        int startingLineID = getIDFromChildElement(dialogueElement, "startingLine");

        Map<Integer, QuestTemplate> questTemplateIDMap = questTemplates.stream()
                .collect(Collectors.toMap(QuestTemplate::getID, Function.identity()));
        Map<DialogueLine, List<Response>> incidenceMap = new HashMap<DialogueLine, List<Response>>();

        NodeList dialogueLineNodes = dialogueElement.getElementsByTagName("dialogueLine");
        Map<Integer, DialogueLine> dialogueLines = new HashMap<Integer, DialogueLine>();
        for (int i = 0; i < dialogueLineNodes.getLength(); i++) {
            if (dialogueLineNodes.item(i).getNodeType() == Node.ELEMENT_NODE) {
                Element dialogueLineElement = (Element) dialogueLineNodes.item(i);

                int ID = getIDFromElement(dialogueLineElement);
                String text = getTextValue(dialogueLineElement, "text");

                DialogueLine dialogueLine;
                if (dialogueLineElement.getAttribute("type").equals("")) {
                    dialogueLine = new DialogueLine(text);
                } else if (dialogueLineElement.getAttribute("type").equals("path")) {
                    int pathID = getIDFromChildElement(dialogueLineElement, "path");

                    dialogueLine = new PathDialogueLine(text, pathID);
                } else if (dialogueLineElement.getAttribute("type").equals("quest")) {
                    int questID = getIDFromChildElement(dialogueLineElement, "quest");
                    dialogueLine = new QuestDialogueLine(text, questTemplateIDMap.get(questID));
                } else {
                    throw new IllegalArgumentException("Non-existent dialogue type");
                }

                dialogueLines.put(ID, dialogueLine);
                incidenceMap.put(dialogueLine, new ArrayList<Response>());
            }
        }

        for (int i = 0; i < dialogueLineNodes.getLength(); i++) {
            if (dialogueLineNodes.item(i).getNodeType() == Node.ELEMENT_NODE) {
                Element dialogueLineElement = (Element) dialogueLineNodes.item(i);

                int ID = getIDFromElement(dialogueLineElement);

                NodeList responseNodeList = dialogueLineElement.getElementsByTagName("response");
                for (int j = 0; j < responseNodeList.getLength(); j++) {
                    Element responseElement = (Element) responseNodeList.item(j);

                    String responseText = getTextValue(responseElement, "text");
                    int target = getIntValue(responseElement, "target");

                    incidenceMap.get(dialogueLines.get(ID)).add(new Response(dialogueLines.get(target), responseText));
                }
            }
        }

        DialogueGraph dialogueGraph = new DialogueGraph(dialogueID,
                new DirectedGraph<DialogueLine, Response>(incidenceMap), dialogueLines.get(startingLineID));

        return dialogueGraph;
    }

    private Item generateItem(Element itemElement) {
        String type = itemElement.getAttribute("type");

        int itemID = Integer.parseInt(itemElement.getAttribute("id"));
        String name = itemElement.getAttribute("name");

        String description = getTextValue(itemElement, "description");
        int weight = getIntValue(itemElement, "weight");
        int sellPrice = getIntValue(itemElement, "sellPrice");

        if (type.equals("")) {

            boolean consumeOnUse = Boolean.parseBoolean(getTextValue(itemElement, "consumeOnUse"));

            return new Item(itemID, name, description, weight, sellPrice, consumeOnUse);
        } else if (type.equals("weapon")) {
            int damage = getIntValue(itemElement, "damage");
            double critChance = getDoubleValue(itemElement, "critChance");
            double critDamageMultiplier = getDoubleValue(itemElement, "critDamageMultiplier");

            return new Weapon(itemID, name, description, weight, sellPrice, damage, critChance, critDamageMultiplier);
        } else if (type.equals("potion")) {
            int modifier = getIntValue(itemElement, "modifier");
            int duration = getIntValue(itemElement, "duration");

            Attribute attribute = Attribute.valueOf(getAttributeValue(itemElement, "attributeModifier", "attribute"));

            return new Potion(itemID, name, description, weight, sellPrice,
                    new AttributeModifier(attribute, modifier, duration));
        } else {
            throw new IllegalArgumentException("ITEM TYPE NOT FOUND");
        }
    }

    private QuestTemplate generateQuestTemplate(Element rootElement) {
        int questID = Integer.parseInt(rootElement.getAttribute("id"));
        String name = rootElement.getAttribute("name");

        NodeList childNodes = rootElement.getChildNodes();
        String description = childNodes.item(0).getTextContent();

        Element objectivesElement = (Element) rootElement.getElementsByTagName("objectives").item(0);

        List<Objective> objectives = new ArrayList<Objective>();

        NodeList objectiveNodes = objectivesElement.getElementsByTagName("objective");
        for (int i = 0; i < objectiveNodes.getLength(); i++) {
            if (objectiveNodes.item(i).getNodeType() == Node.ELEMENT_NODE) {
                Element objective = (Element) objectiveNodes.item(i);
                if (objective.getAttribute("type").equals("tally")) {
                    int targetID = getIntValue(objective, "targetID");

                    GameEvent gameEvent = GameEvent.valueOf(getTextValue(rootElement, "gameEvent"));

                    int targetQuantity = getIntValue(objective, "targetQuantity");

                    objectives.add(
                            new TallyObjective(objective.getAttribute("name"), targetID, gameEvent, targetQuantity));
                }
            }
        }

        return new QuestTemplate(questID, name, description, objectives);
    }

    private Document getDocumentFromSaveFile() {
        File saveFile = new File(filePath);
        Document fileDoc;

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            fileDoc = builder.parse(saveFile);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
            return null;
        }

        return fileDoc;

    }

    private EntityTemplate generateEntityTemplate(Element rootElement) {
        int entityID = Integer.parseInt(rootElement.getAttribute("id"));
        String name = rootElement.getAttribute("name");

        String description = getTextValue(rootElement, "description");
        int equippedWeaponID = getIntValue(rootElement, "equippedWeapon");

        List<Loot> possibleLoot = new ArrayList<Loot>();
        NodeList lootNodes = rootElement.getElementsByTagName("loot");
        for (int i = 0; i < lootNodes.getLength(); i++) {
            int itemID = getIntValue(rootElement, "itemID");
            int minimum = getIntValue(rootElement, "minimum");
            int maximum = getIntValue(rootElement, "maximum");

            // Element lootElement = (Element) lootNodes.item(i);

            // Node itemIdNode = lootElement.getFirstChild();
            // itemID = Integer.parseInt(lootElement.getTextContent());

            // Node minimumNode = itemIdNode.getNextSibling();
            // minimum = Integer.parseInt(minimumNode.getTextContent());

            // Node maximumNode = itemIdNode.getNextSibling();
            // maximum = Integer.parseInt(maximumNode.getTextContent());

            possibleLoot.add(new Loot(itemID, minimum, maximum));
        }

        int dialogueID = Integer.parseInt(getAttributeValue(rootElement, "dialogue", "id"));

        NodeList attributeNodes = rootElement.getElementsByTagName("attribute");

        EnumMap<Attribute, Integer> attributeDistribution = new EnumMap<Attribute, Integer>(Attribute.class);
        for (int i = 0; i < attributeNodes.getLength(); i++) {
            Element e = (Element) attributeNodes.item(i);

            attributeDistribution.put(Attribute.valueOf(e.getAttribute("type")), Integer.parseInt(e.getTextContent()));
        }

        return new EntityTemplate(entityID, name, description, equippedWeaponID, possibleLoot, dialogueID,
                attributeDistribution);
    }

    private String getTextValue(Element parent, String tag) {
        String value = null;
        NodeList nodeList;
        nodeList = parent.getElementsByTagName(tag);
        if (nodeList.getLength() > 0 && nodeList.item(0).hasChildNodes()) {
            value = nodeList.item(0).getFirstChild().getNodeValue();
        }
        return value;
    }

    private String getAttributeValue(Element parent, String tag, String attribute) {
        String value = null;
        NodeList nodeList;
        nodeList = parent.getElementsByTagName(tag);
        if (nodeList.getLength() > 0) {
            Element e = (Element) nodeList.item(0);
            value = e.getAttribute(attribute);
        }
        return value;
    }

    private Integer getIntValue(Element parent, String tag) {

        return Integer.parseInt(getTextValue(parent, tag));
    }

    private Double getDoubleValue(Element parent, String tag) {

        return Double.parseDouble(getTextValue(parent, tag));
    }

    private int getIDFromElement(Element e) {
        return Integer.parseInt(e.getAttribute("id"));
    }

    private int getIDFromChildElement(Element rootElement, String tag) {
        return Integer.parseInt(getAttributeValue(rootElement, tag, "id"));
    }

}
