package factorioCalculator;

import java.util.*;
import java.io.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
//import com.fasterxml.jackson.databind.node.ObjectNode;

public class Calculations {
    public Calculations() {}
	ObjectMapper objectMapper = new ObjectMapper();

	Scanner sc = new Scanner(System.in);
    
	static ArrayList<ItemList> itemList = new ArrayList<>();
	static ArrayList<Ingredient> ingredientList = new ArrayList<>();
	static ArrayList<IngredientListHolder> inListHolder = new ArrayList<>();
	static HashMap<String, Double> basicIngredientList = new HashMap<>();
	static HashMap<String, Double> map = new HashMap<>();
	static Set<String> basicIn = new HashSet<String>();
	
	File file = new File("C:\\Users\\thund\\eclipse-workspace\\factorioCalculator\\src\\factorioCalculator\\Data.json");
	
    public void doCalc(String item, double reqAmount, double craftingSpeed, int tier, boolean start, String type) throws IOException {
    	System.out.println(item);
		JsonNode jNode = objectMapper.readTree(file);
		
		boolean found = false;
		//too many if statements. If I have time, I will break this up into smaller sections
		
		if(jNode.isArray()) {
			for (JsonNode node : jNode) {
				String name = node.get("name").asText();
				if (name.equals(item)) {
					if (!basicIn.contains(node.get("id").asText())) {
						found = true;
						if (start == true) { //this will print off the information at the beginning						
							System.out.println("ID: " + node.get("id").asText());
							System.out.println("Name: " + name);
							System.out.println("Type: " + node.get("type").asText());
							System.out.println("Wiki Link: " + node.get("wiki_link").asText());
							System.out.println("Category: " + node.get("category").asText());
							System.out.println("Recipe: " + node.get("recipe").toString());
						}
						
						ArrayNode recipe = (ArrayNode) node.get("recipe").get("ingredients");
						ArrayList<JsonNode> rList = new ArrayList<>();
						
						double makeSpeed = node.get("recipe").get("time").asDouble();
						int yield = node.get("recipe").get("yield").asInt();
						
						for (JsonNode recipe1 : recipe) {
							rList.add(recipe1);
						}
						
						for (JsonNode r : rList) {
							String id = r.get("id").asText();
							double amount = r.get("amount").asDouble();   
							itemList.add(new ItemList(id, amount, type));
							
						}
						//add check for type
						calculator(reqAmount, craftingSpeed, itemList, makeSpeed, yield, name, tier, start, type);
						
						break;
					} 
				}
			}

		}
		
		if (!found) {
			System.out.println("Name not found!");
		}
		
    }
    
    private void calculator(double reqAmount, double craftingSpeed, ArrayList<ItemList> itemList, double makeSpeed, int yield, String name, int tier, boolean start, String type) throws IOException {
    	//crafting speed impacts time required 
        for (int i = 0; i < itemList.size(); i++) {
        	System.out.println(itemList.get(i).getItemName() + "  " + itemList.get(i).getAmount());
        }
    	double baseItemsPerMinute = (60 / makeSpeed);
    	double adjustedItemsPerMinute = (baseItemsPerMinute * craftingSpeed) * yield;
    	double assemblersNeeded = reqAmount / adjustedItemsPerMinute;    	

    	switch(type) {
    	case "assembler":
    		System.out.println("In order to produce " + reqAmount +" " + name + " per minute, you need " + assemblersNeeded + " tier " + tier + " assemblers. ");    		
    		break;
    	case "liquid":
    		System.out.println("In order to produce " + reqAmount + " " + name + " per minute, you need " + assemblersNeeded + "chemical plants.");
    		break;
    	case "silo":
    		System.out.println("In order to produce " + reqAmount + " " + name + " per minute, you need " + assemblersNeeded +" "+ "rocket silos");
    		break;
    	}
	    		
		System.out.println("Would you like more detailed information? y/n");
		String cho = sc.nextLine();
		if (cho.equalsIgnoreCase("y")) {    		
			calcIngredients(reqAmount, yield, itemList, makeSpeed, yield, name, tier);
		}
    	 
    	
    }
    
    private void calcIngredients(double reqAmount, double craftingSpeed, ArrayList<ItemList> itemList, double makeSpeed, int yield, String name, int tier) throws IOException {
    	System.out.println("calcIngredients called");
    	
    	//using iterator here because it seems cool
    	Iterator<ItemList> iterator = itemList.iterator();
    	while (iterator.hasNext()) {
    		ItemList item = iterator.next();
    		String itemName = item.getItemName();
			double amount = item.getAmount();
			ingredientList.add(new Ingredient(itemName, amount));
			//System.out.println(ingredientList.get(i).getItemId() + " " + ingredientList.get(i).getItemAmount());
			try {
				System.out.println("Trying to get item recipe...");
				getItemRecipe(itemName);
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
    }  	
    
    public void detailedCalc() throws IOException {
    	getBasicIngredients();
    }
    
    public void printItemList() {
    	System.out.println("Printing item list...");
    	for (int i = 0; i < itemList.size(); i++) {    		
    		System.out.println(itemList.get(i).getItemName());
    	}
    }
    
    public void clearItemList() {
    	itemList.clear();
    }
    
    
    
    public void getItemRecipe(String item) throws IOException {
		JsonNode jNode = objectMapper.readTree(file);
		//System.out.println(item);
		for (JsonNode node : jNode) {
			String oName = node.get("id").asText();
			if (oName.equalsIgnoreCase(item)) { 
				ArrayNode recipe = (ArrayNode) node.get("recipe").get("ingredients");
				System.out.println("got recipe");				
				addToList(recipe);
			}
		}
    }
    
    public void addToList(ArrayNode node) {
    	System.out.println(node);
    	System.out.println("Printed node");
    	for (int i  = 0; i < node.size(); i++) {
    		String itemName = node.get(i).get("id").asText();
    		double amount = node.get(i).get("amount").asInt();
    		if (map.containsKey(itemName)) {
    			map.put(itemName, map.get(itemName) + amount);
    		} else {    			
    			map.put(itemName, amount);
    		}
    	}
    }
    
    public void printInList(double rate) {
    	System.out.println("Printing ingredient list...");
    	for (Map.Entry<String, Double> entry : map.entrySet()) {
    		System.out.println("You need " + entry.getValue() * rate + " of " + entry.getKey() + " per minute. ");
    	}
    	
    	
    }
    
    public void saveFile(String name) {
    	//write to file
    	File sFile = new File("C:\\Users\\thund\\eclipse-workspace\\factorioCalculator\\src\\factorioCalculator\\save.txt");
    	try {
    		System.out.println("Attempting to save to file");
			FileWriter write = new FileWriter(sFile, true);
			write.write(name + "\n");
			write.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
    }
    
	public void readFile(File saveFile) {
		System.out.println("Your previous searches are: ");
		
		try (Scanner fileRead = new Scanner(saveFile)) {
			while (fileRead.hasNextLine()) {
				String str = fileRead.nextLine();
				System.out.println(str);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	
	public void addBasicIngredients() {
		basicIn.add("copper-plate");
		basicIn.add("Copper plate");
		basicIn.add("iron-plate");
		basicIn.add("Iron plate");
		basicIn.add("water");
		basicIn.add("Water");
		basicIn.add("crude-oil");
		basicIn.add("Crude oil");
		basicIn.add("petroleum-gas");
		basicIn.add("Petroleum gas");
		basicIn.add("heavy-oil");
		basicIn.add("Heavy oil");
		basicIn.add("light-oil");
		basicIn.add("Light oil");
		basicIn.add("uranium-ore");
		basicIn.add("Uranium ore");
	}
	
	public void checkBasic(String cItem) {
		if(!basicIn.contains(cItem)) {
			//get recipe 
		}
	}
	
	public boolean checkIfBasic(String item) {
		boolean basic = false;
		if(basicIn.contains(item)) {
			basic = true;
		} else {
			basic = false;
		}
		
		return basic;
	}
	
	public boolean areIngBasic(String cItem) {
		boolean answer = false;
		for(Map.Entry<String, Double> entry : map.entrySet()) {
			if(!basicIn.contains(cItem)) {
				answer = false;
				break;
			} else {
				answer = true;
			}
		}
		
		return answer;
	}
	public void getBasicIngredients () throws IOException { 
		for(Map.Entry<String, Double> et : map.entrySet()) {
			String cItem = et.getKey();
			boolean areValsBasic = areIngBasic(cItem);
			if (!areValsBasic) {
				getItemRecipe(cItem);
				
			}
		}
		    printBasicIngredients();
	}
	
	public void printBasicIngredients() throws IOException {
		printInList(0);
		printItemList();
		System.out.println("Basic ingredients called");
	}
	
	public boolean nameIsFound() {
		return true;
	}
	
	public boolean nameNotFound() {
		return false;
	}
	
}
