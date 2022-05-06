package com.example;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Main {	
	public static void main(String[] args) {
        Options options = new Options();

        Option loadIdOp = new Option("loadId", true, "a string containing an arbitrary ID of the load which needs to be moved.");
        loadIdOp.setRequired(true);
        options.addOption(loadIdOp);
        
        Option xOp = new Option("x", true, "a number indicating the x coordinate of the load which needs to be moved.");
        xOp.setRequired(true);
        options.addOption(xOp);

        Option yOp = new Option("y", true, "a number indicating the y coordinate of the load which needs to be moved.");
        yOp.setRequired(true);
        options.addOption(yOp);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd = null; 

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp(" ", options);
            System.exit(1);
        }

        String loadId = cmd.getOptionValue("loadId");
        long x = Long.parseLong(cmd.getOptionValue("x"));
        long y = Long.parseLong(cmd.getOptionValue("y"));

        System.out.println(findBestRobot(loadId, x, y));
	}

	public static JSONObject findBestRobot(String loadId, long tx, long ty) {
		try {
			URL url = new URL("https://60c8ed887dafc90017ffbd56.mockapi.io/robots");

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.connect();

			// Getting the response code
			int responsecode = conn.getResponseCode();
			if (responsecode != 200) {
				throw new RuntimeException("HttpResponseCode: " + responsecode);
			} else {
				StringBuilder inline = new StringBuilder();
				Scanner scanner = new Scanner(url.openStream());

				// Write all the JSON data into a string using a scanner
				while (scanner.hasNext()) {
					inline.append(scanner.nextLine());
				}

				// Close the scanner
				scanner.close();

				// Using the JSON simple library parse the string into a json object
				JSONParser parse = new JSONParser();
				JSONArray jsonArray = (JSONArray) parse.parse(inline.toString());
				
				// stores All robots
				List<Robot> list = new ArrayList<>();
				
				// stores robots within the 10-unit range
				List<Robot> list2 = new ArrayList<>(); 
				
				// make Robot objects
				for(int i = 0; i < jsonArray.size(); i++) {
					JSONObject obj = (JSONObject) jsonArray.get(i);
					String robotId = (String)obj.get("robotId");
					long x = (long) obj.get("x");
					long y = (long) obj.get("y");
					long batteryLevel = (long) obj.get("batteryLevel");
					Robot robot = new Robot(robotId, x, y, batteryLevel);
					list.add(robot);
					robot.computeDistance(tx, ty);
					if (robot.distance <= 10) {
						list2.add(robot);
					}
				}
				
				// no robots available
				if (list.size() == 0) return null;
				
				// there're robots within 10-unit range, return the one with highest battery level
				if (list2.size() > 0) {
					Collections.sort(list2, new Comparator<Robot>() {
						@Override
						public int compare(Robot o1, Robot o2) {
							if (o1.batteryLevel == o2.batteryLevel) return 0;
							return o1.batteryLevel > o2.batteryLevel ? -1 : 1;
						}
					});
					Robot r = list2.get(0);
					JSONObject res = new JSONObject();
					res.put("robotId", r.robotId);
					res.put("distanceToGoal", new Double(r.distance));
					res.put("batteryLevel", new Long(r.batteryLevel));
					return res;
				}
				
				// there's no robots within 10-unit range, return the closest
				Collections.sort(list, new Comparator<Robot>() {
						@Override
						public int compare(Robot o1, Robot o2) {
							if (o1.distance == o2.distance) return 0;
							return o1.distance < o2.distance ? -1 : 1;
						}
				});
				Robot r = list.get(0);
				JSONObject res = new JSONObject();
				res.put("robotId", r.robotId);
				res.put("distanceToGoal", new Double(r.distance));
				res.put("batteryLevel", new Long(r.batteryLevel));
				return res;				
			}
		} catch (Exception e) {
//			System.out.println(e);
			return null;
		}
	}
}
