
package com.ironically.googletranslate.main;

import java.util.*;

import javax.swing.*;

import Main.*;

public class Data {
	
	// This Project_id for the GoogleTranslator
	public static final String project_id = "personaltranslater";
	
	// Flag to use ALL Availible google translate.
	public static boolean useAll = false;
	
	// Flag to use the GUI.
	public static boolean isGui = false;
	
	// Amount of Languages to Translate through.
	public static int LanguageLayers = 10;
	
	// Progress Made through Current Translation.
	public static int progress = 0;
	
	// Sets the Current Language List.
	public static void LanguageSetup() {
		if (Data.useAll == true) {
			CODES.addAll(Arrays.asList(ALL_LANGS));
			return;
		}
		CODES.clear();
		for (int i = 0; i < LanguageLayers; i++) {
			String next = ALL_LANGS[new Random().nextInt(ALL_LANGS.length - 1)];
			if (!CODES.contains(next)) {
				CODES.add(next);
			}
			else {
				i--;
			}
		}
		CODES.add("en-US");
		if (isGui == false) {
			LL.console("LANGUAGE CHAIN: ");
			LL.console(Arrays.toString(CODES.toArray()).replaceAll(", ", " > "));
		}
		else {
			Window.GUI.setLangChain();
		}
		
	}
	
	public static void updateProgressBarData_GUI(JProgressBar pb, int newMAX) {
		pb.setMaximum(newMAX);
		pb.setValue(0);
		LanguageLayers = pb.getMaximum();
		Data.LanguageSetup();
		Window.GUI.setLangChain();
	}
	
	/*
	 * ALL possible google translate languages availible.
	 */
	public static final String[] ALL_LANGS = { "af", "am", "ar", "az", "be", "bg", "bn", "bs", "ca", "ceb", "co", "cs", "cy", "da", "de", "el", "eo",
			"es", "et", "eu", "fa", "fi", "fr", "fy", "ga", "gd", "gl", "gu", "ha", "haw", "hi", "hmn", "hr", "ht", "hu", "hy", "id", "ig", "is",
			"it", "iw", "ja", "jw", "ka", "kk", "km", "kn", "ko", "ku", "ky", "la", "lb", "lo", "lt", "lv", "mg", "mi", "mk", "ml", "mn", "mr", "ms",
			"mt", "my", "ne", "nl", "no", "ny", "pa", "pl", "ps", "pt", "ro", "ru", "sd", "si", "sk", "sl", "sm", "sn", "so", "sq", "sr", "st", "su",
			"sv", "sw", "ta", "te", "tg", "th", "tl", "tr", "uk", "ur", "uz", "vi", "xh", "yi", "yo", "zh-CN", "zh-TW", "zu" };
	
	public static ArrayList<String> CODES = new ArrayList<String>();
	
}
