package com.cmpe239;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MessageReciever {

	String[] responseData;

	public String[] messageReciever() {

		String url = "http://localhost:8080/moonshotvalues";
		try {
			//URL message receiver
			URL obj = new URL(url);
			HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
			connection.setRequestMethod("GET");
			//Create InputReader
			BufferedReader inputReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String inputLine;
			String data = "";
			while ((inputLine = inputReader.readLine()) != null) {
				data = inputLine;
			}
			inputReader.close();
			responseData = data.split(";");
			for (int i = 0; i < responseData.length; i++) {
				System.out.println("@@@ resp " + responseData[i]);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return responseData;
	}
}
