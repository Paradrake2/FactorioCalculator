package factorioCalculator;

public class ItemList {
	String iName;
	double amount;
	String type;
	
	public ItemList(String itemName, double amount2, String type) {
		this.iName = itemName;
		this.amount = amount2;
		this.type = type;
	}
	
	public String getItemName() {
		return iName;
	}
	
	public double getAmount() {
		return amount;
	}
	
	public String getType() {
		return type;
	}
}
