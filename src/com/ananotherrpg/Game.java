package com.ananotherrpg;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.ananotherrpg.entity.PlayerAvatar;
import com.ananotherrpg.io.IOManager;
import com.ananotherrpg.io.IOManager.ListType;
import com.ananotherrpg.io.IOManager.SelectionMethod;
import com.ananotherrpg.level.CampaignState;

/**
 * The Game class is reponsible for loading or creating new Campaigns
 */
public class Game {

	private final static String CAMPAIGN_DIRECTORY = "res\\campaigns\\";
	private final static String CAMPAIGN_EXTENSION = ".camp";

	private final static String SAVE_FILE_DIRECTORY = "res\\saves\\";
	private final static String SAVE_FILE_EXTENSION = ".save";

	private Campaign currentCampaign;

	private static Boolean shouldExit = false;

	public static void main(String[] args) {

		while (!shouldExit) {
			IOManager.println("Welcome to an another rpg!");

			// Get desired campaign file

			try {
				Optional<File> opFile = queryUserForFile(CAMPAIGN_DIRECTORY, CAMPAIGN_EXTENSION);

				if (!opFile.isPresent()) {
					IOManager.println("BYE!");
					System.exit(0);
				} else {
					File file = opFile.get();
					ArrayList<String> options = new ArrayList<String>(
							Arrays.asList("Create a new campaign", "Load a previous campaign", "Quit :("));

					Optional<String> opInput = IOManager.listAndQueryUserInputAgainstStrings(options, ListType.NUMBERED,
							SelectionMethod.NUMBERED, false);

					String input;
					if (opInput.isPresent()) {
						input = opInput.get();
					} else {
						throw new IllegalStateException("User didn't choose an option!");
					}

					Game game;
					if (input == options.get(0)) {
						GameLoader loader = new GameLoader(file);
						game = loader.loadGame();

						if (game == null) {
							IOManager.println("Loading failed");
						} else
							game.start();
					} else if (input == options.get(1)) {
						Optional<File> opSaveFile = queryUserForFile(SAVE_FILE_DIRECTORY, SAVE_FILE_EXTENSION);

						if (!opSaveFile.isPresent()) {
							IOManager.println("Couldn't load save file");
						} else {
							GameLoader loader = new GameLoader(file, opSaveFile.get());
							game = loader.loadGame();

							if (game == null) {
								IOManager.println("Loading failed");
							} else
								game.start();
						}
						
					} else if (input == options.get(2)) {
						IOManager.print("Goodbye");
						shouldExit = true;
					}
				}
			} catch (IOException e) {
				IOManager.println("Couldn't get file");
				e.printStackTrace();
				System.exit(0);
			}
		}

		System.exit(0);
	}

	private static Optional<File> queryUserForFile(String directory, String extension) throws IOException {
		IOManager.println("Please select one of these campaigns: ");

		List<Path> campaignPaths;
		try (Stream<Path> paths = Files.walk(Paths.get(directory))) {
			campaignPaths = paths.filter(Files::isRegularFile).filter(p -> p.toString().endsWith(extension)).distinct()
					.collect(Collectors.toList());
		}

		List<File> campaignFiles = campaignPaths.stream().map(Path::toFile).collect(Collectors.toList());

		Optional<File> file = IOManager.listAndQueryUserInputAgainstCustomMap(campaignFiles, File::getName,
				ListType.NUMBERED, SelectionMethod.NUMBERED, true);

		return file;

	}

	public String getFileName(Path path) {
		return path.getFileName().toString();
	}

	private void start() {

		currentCampaign.play();
	}

	public Game(Campaign campaign) {
		this.currentCampaign = campaign;

	}

	public static void saveGame(CampaignState campaignState, PlayerAvatar player) {
		String fileName = IOManager.getInput("What filename should this save go under?");

		String filePath = SAVE_FILE_DIRECTORY + fileName + SAVE_FILE_EXTENSION;

		GameSaver saver = new GameSaver(filePath);
		saver.save(campaignState, player);
	
	}

}
