
package com.ironically.googletranslate.main;

import java.io.*;

import Main.*;

public class Start {
	
	private static final File GOOGLE_APPLICATION_CREDENTIALS = new File("keys.json");
	
	public static void main(String[] args) {
		// REMOVE ASAP
		for (int i = 1; i < 9; i++) {
			if (i > 9) {
				LL.console("HM" + i + " : \n\n\n");
			}
			else {
				LL.console("HM0" + i + " : \n\n\n");
			}
			
		}
		//
		try {
			if (args[0].toLowerCase().equals("nogui")) {
				CONSOLE_LAUNCH();
			}
			else
				throw new Exception("NO LAUNCH PARAMETERS!");
			
		}
		catch (Exception E) {
			Data.isGui = true;
			GUI_LAUNCH();
		}
	}
	
	private static void CONSOLE_LAUNCH() {
		Data.LanguageSetup();
		try {
			LL.slowPrint("Welcome to the Translator!", 1);
			LL.console("Setting up....");
			Tools.GoogleAuth(GOOGLE_APPLICATION_CREDENTIALS.getAbsolutePath());
			boolean setupComplete = false;
			while (setupComplete == false) {
				try {
					Data.LanguageLayers = Integer.parseInt(LL.askQuestion("How many Layers (3-99)>> "));
					if (Data.LanguageLayers < 4 || Data.LanguageLayers > 100) {
						LL.console("Outside Range! 3-99");
					}
					else {
						setupComplete = true;
					}
				}
				catch (Exception e) {
					LL.console("INVALID!! Try again...");
				}
			}
			boolean quit = false;
			boolean first = true;
			String INPUT = "DEFAULT";
			String OUTPUT = "OUT";
			while (quit != true) {
				INPUT = LL.askQuestion("(qqq to Quit) ENGLISH INPUT: ");
				// Check if the program needs to be quit.
				if (INPUT.toLowerCase().equals("qqq")) {
					quit = true;
					break;
				}
				// Make Sure the statement is able to be translator.
				LL.console("\n");
				if (INPUT.length() < 2) {
					LL.console("TOO SHORT!");
					LL.console("USING DEAULT STATEMENT.....");
					INPUT = "TESTING";
				}
				
				// The Translation Occurs here!
				OUTPUT = Tools.TranslateGivenText(INPUT.trim());
				if (first == true) {
					LL.CLEAR_CONSOLE();
					LL.console("ENGLISH INPUT: " + INPUT);
					LL.console("\n");
					first = false;
				}
				LL.console("TRANSLATED " + Data.LanguageLayers + " TIMES: " + OUTPUT + "\n==============================================\n");
				LL.pressEnterToContinue();
				LL.CLEAR_CONSOLE();
			}
			
		}
		catch (IOException e) {
			e.printStackTrace();
			LL.pressEnterToContinue();
		}
		LL.console("Thanks for using me!");
		LL.pressEnterToContinue();
	}
	
	private static void GUI_LAUNCH() {
		Window.LaunchGUI();
	}
	
}
