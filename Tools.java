
package com.ironically.googletranslate.main;

import java.io.*;

import com.google.api.gax.paging.*;
import com.google.auth.oauth2.*;
import com.google.cloud.storage.*;
import com.google.cloud.translate.v3.*;
import com.google.common.collect.*;

public class Tools {
	/*
	 * THIS IS CRITICAL SETUP CODE!!!!! THIS USES IS WHAT ALLOWS OUR GOOGLE API
	 * KEY TO WORK.
	 */
	public static void GoogleAuth(String jsonPath) throws IOException {
		// You can specify a credential file by providing a path to
		// GoogleCredentials.
		// Otherwise credentials are read from the
		// GOOGLE_APPLICATION_CREDENTIALS environment variable.
		GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream(jsonPath))
				.createScoped(Lists.newArrayList("https://www.googleapis.com/auth/cloud-platform"));
		Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
		
		// System.out.println("Buckets:");
		Page<Bucket> buckets = storage.list();
		for (Bucket bucket : buckets.iterateAll()) {
			bucket.toString();
		}
	}
	
	/*
	 * Takes passed TXT including newLines THEN returns the Translated String
	 */
	public static String TranslateGivenText(String ENG_TO_TRANSLATE) throws IOException {
		String projectId = Data.project_id;
		String ret = ENG_TO_TRANSLATE;
		if (Data.isGui == true) {
			Window.GUI.ProgressRESET();
		}
		
		if (Data.useAll == true) {
			for (int i = 0; i < (Data.ALL_LANGS.length); i++) {
				if (i == 0) {
					ret = translatRequest(projectId, "global", "en", (String) Data.CODES.toArray()[i], ret);
				}
				else {
					ret = translatRequest(projectId, "global", Data.ALL_LANGS[i - 1], Data.ALL_LANGS[i], ret);
				}
				if (ret == null) return null;
				if (Data.isGui == true) {
					Window.GUI.progressBar.update(Window.GUI.progressBar.getGraphics());
					Window.GUI.ProgressUpdate();
				}
			}
			
			return translatRequest(projectId, "global", Data.ALL_LANGS[Data.ALL_LANGS.length - 1], "en", ret);
		}
		else {
			
			for (int i = 0; i < Data.CODES.size(); i++) {
				if (i == 0) {
					ret = translatRequest(projectId, "global", "en", (String) Data.CODES.toArray()[i], ret);
				}
				else {
					ret = translatRequest(projectId, "global", (String) Data.CODES.toArray()[i - 1], (String) Data.CODES.toArray()[i], ret);
				}
				if (ret == null) return null;
				if (Data.isGui == true) {
					Window.GUI.progressBar.update(Window.GUI.progressBar.getGraphics());
					Window.GUI.ProgressUpdate();
				}
			}
			return ret;
		}
	}
	
	// Actual Translation Requestor
	private static String translatRequest(String projectId, String location, String sourceLanguage, String targetLanguage, String text) {
		
		// Initialize client that will be used to send requests. This client
		// only needs to be created
		// once, and can be reused for multiple requests. After completing all
		// of your requests, call
		// the "close" method on the client to safely clean up any remaining
		// background resources.
		try (TranslationServiceClient client = TranslationServiceClient.create()) {
			LocationName parent = LocationName.of(projectId, location);
			TranslateTextRequest request = TranslateTextRequest.newBuilder().setParent(parent.toString())
					.setModel("projects/personaltranslater/locations/global/models/general/nmt").setMimeType("text/plain")
					.setTargetLanguageCode(targetLanguage).setSourceLanguageCode(sourceLanguage).addContents(text).build();
			TranslateTextResponse response = client.translateText(request);
			String ret = "";
			// Display the translation for each input text provided
			for (com.google.cloud.translate.v3.Translation translation : response.getTranslationsList()) {
				ret = translation.getTranslatedText();
			}
			return ret;
		}
		catch (Exception E) {
			E.printStackTrace();
			return null;
		}
	}
}
