import java.io.Serializable;


public class Expense implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String itemName;
	private double unitCost;
	private int quantity;
	private double totalCost;
	
	public Expense() {
		this.itemName = "";
		this.unitCost = 0.0;
		this.quantity = 0;
		this.totalCost = 0.0;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public double getUnitCost() {
		return unitCost;
	}

	public void setUnitCost(double unitCost) {
		this.unitCost = unitCost;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getTotalCost() {
		return totalCost;
	}

	public void setTotalCost() {		
		this.totalCost = this.getQuantity() * this.getUnitCost();
	}
	
	public void setTotalCost(double totalCost){
		this.totalCost = totalCost;
	}
	
}
