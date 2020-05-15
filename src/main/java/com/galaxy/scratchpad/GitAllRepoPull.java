package com.galaxy.scratchpad;

import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class GitAllRepoPull {

	private static String GIT_REPO_URL = "https://api.github.com/users/dhirajraut/repos";

	public static void main(String args[]) throws Exception {
		URL gitRepoUrl = new URL(GIT_REPO_URL);
		HttpURLConnection connection = (HttpURLConnection) gitRepoUrl.openConnection();
		connection.setRequestMethod("GET");

		connection.connect();
		if (200 == connection.getResponseCode()) {
			String response = "";
			Scanner sc = new Scanner(gitRepoUrl.openStream());
			List <String> cloneURLs = new ArrayList<String> ();
			while (sc.hasNext()) {
				response = sc.nextLine();
				if (response.contains("clone_url")) {
					
					JSONArray results = new JSONArray();
					results.add(new JSONParser().parse(response));
					JSONArray array = (JSONArray) results.get(0);
					for (int i = 0; i < array.size(); i++) {
						JSONObject jsonObject = (JSONObject) array.get(i);
						cloneURLs.add((String) jsonObject.get("clone_url"));
					}
				}
			}
			sc.close();
			
			Runtime runtime = Runtime.getRuntime();
//			runtime.exec("cd", null, new File("C:/Users/draut/00_Dhiraj/Workspace/Source/Temp"));
			runtime.exec("cmd /c start cmd.exe /K \"dir && cd C:\\Users\\draut\\00_Dhiraj\\Workspace\\Source\\Temp");
			
			for (String cloneURL: cloneURLs) {
				runtime.exec("cmd /c start cmd.exe /K \"dir && git clone " + cloneURL);
				String[] split = cloneURL.split("/");
				String folderName = split[4].substring(0, split[4].length() - 4);
//				runtime.exec("cd " + folderName);
				runtime.exec("cmd /c start cmd.exe /K \"dir && cd " + folderName);
				runtime.exec("cmd /c start cmd.exe /K \"dir && mvn clean install");
			}
		}

	}
}
