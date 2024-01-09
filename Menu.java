package factorioCalculator;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.*;
import java.io.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

public class Menu {
	ObjectMapper objectMapper = new ObjectMapper();

	Scanner sc = new Scanner(System.in);
	Calculations c = new Calculations();
	File file = new File("C:\\Users\\thund\\eclipse-workspace\\factorioCalculator\\src\\factorioCalculator\\Data.json");
	File sFile = new File("C:\\Users\\thund\\eclipse-workspace\\factorioCalculator\\src\\factorioCalculator\\save.txt");
	public Menu() {}

	public void promptLoadFile(File file) {
		System.out.println("Would you like to load previous searches?");
		
		String loadQ = sc.nextLine();
		
		if (loadQ.equalsIgnoreCase("y")) {
			c.readFile(sFile);
		} else {
			System.out.println("ok");
		}
		
	}
	
	public void callCalculations(String item, double rate, int tier, double craftingSpeed, boolean start, String type) throws IOException {
		switch(tier) {
	    case 1:
	        craftingSpeed = 0.5;
	        c.doCalc(item, rate, craftingSpeed, tier, start, type);
	        break;
	    case 2:
	        craftingSpeed = 0.75;
	        c.doCalc(item, rate, craftingSpeed, tier, start, type);
	        break;
	    case 3:
	        craftingSpeed = 1.25;
	        c.doCalc(item, rate, craftingSpeed, tier, start, type);
	        break;
	    default:
	        System.out.println("Invalid input. Try again. ");
	        break;
		}
	}
	
	public void printItemList() {
		c.printItemList();
	}
	
	public void printInList(double rate) {
		
	}
	
	public void checkItemList(double rate) {		
		System.out.println("check item list? y/n");
		String ic = sc.nextLine();
		if (ic.equalsIgnoreCase("y")) {
			System.out.println("Attempting to print item list...");					
			c.printItemList();
			c.printInList(rate);
		}
	}
	
	public String crafterType(String item) throws IOException {
		JsonNode jNode = objectMapper.readTree(file);
		double craftingSpeed;
		String whatType = null;
		
		if(jNode.isArray()) {
			for (JsonNode node : jNode) {
				String name = node.get("name").asText();
				if (name.equals(item)) {
					String itemName = node.get("name").asText();
					if (!itemName.equals("Rocket part")) {						
						String type = node.get("type").asText();
						switch(type) {
							case "liquid": //call liquid crafter
								craftingSpeed = 1;
								whatType = "liquid";
								break;
							default: //use assembler
								whatType = "assembler";
								break;
						}
					}else {
						craftingSpeed = 1;
						whatType = "silo";
						break;
					}
						
				}
					
			}
		}
		return whatType;
	} 
		
}
