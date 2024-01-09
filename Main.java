package factorioCalculator;

import java.io.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
    	try {
    		boolean start = true;
    		File saveFile = new File("C:\\Users\\Leo\\eclipse-workspace\\factorioCalculator\\src\\factorioCalculator\\save.txt");
        	Calculations c = new Calculations();
        	Menu m = new Menu();
			double craftingSpeed = 0;		
			Scanner sc = new Scanner(System.in);

			System.out.println("Welcome to my Factorio calculator!");
			
			
			boolean exitMain = false;
			
			do {
				m.promptLoadFile(saveFile);
				c.addBasicIngredients();
				//needs to clear the ingredients list
				c.clearItemList();
				System.out.println("Please enter the item you want to craft.");
				String item = sc.nextLine();
				//check item
				boolean basic = c.checkIfBasic(item);
				if (basic) {
					System.out.println("This is a basic item. It cannot be crafted. ");
				} else {
					String type = m.crafterType(item);
					
					System.out.println("How much of this item do you want to craft per minute?");
					double rate = Double.parseDouble(sc.nextLine());
					
					//m.crafter
					System.out.println("What tier of assembler do you want to use?");
					System.out.println("Available options are: 1, 2, 3.");
					int tier = Integer.parseInt(sc.nextLine());
					
					m.callCalculations(item, rate, tier, craftingSpeed, start, type);
					
					m.checkItemList(rate);
					

					
					System.out.println("Would you like to save this search? y/n");
					String svc = sc.nextLine();
					if (svc.equalsIgnoreCase("y")) {
						c.saveFile(item);
					}
				}
				
				System.out.println("Exit? y or n");
				String exitConfirm = sc.nextLine(); 
				if (exitConfirm.equalsIgnoreCase("y")) {
					exitMain = true;
					break;
				}
				
				
			} while(exitMain == false);
		sc.close();
        } catch(Exception e) {
        	System.out.println("You idiot.");
        	e.printStackTrace();
        }
    } 
}
