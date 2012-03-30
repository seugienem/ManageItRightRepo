import java.io.*;
import java.util.*;
import java.util.zip.DataFormatException;


public class ExpenseDataManager {
final int numberOfTokens = 3;
	
	public void saveExpense(File out, Vector<Expense> expense){ 
		try{
			BufferedWriter writer = new BufferedWriter(new FileWriter(out));
			
			for(Expense item:expense){
				writer.write(item.getItemName() + ",");
				writer.write(item.getUnitCost() + ",");
				writer.write(item.getQuantity() + ",");
				writer.write(item.getTotalCost() + "\n");
			}
			
			writer.close();
		} catch(IOException ex) {
			ex.printStackTrace();
		}
	}
	
	public Vector<Expense> loadExpense(File in) throws DataFormatException{
		Vector<Expense> expense = new Vector<Expense>();
		try{
			BufferedReader reader = new BufferedReader(new FileReader(in));
			String line;
			while((line = reader.readLine()) != null){
				String [] expenseData = line.split(",", numberOfTokens);
				
				expense.add(new Expense());
				expense.lastElement().setItemName(expenseData[0]);
				expense.lastElement().setUnitCost(Double.parseDouble(expenseData[1]));
				expense.lastElement().setQuantity(Integer.parseInt(expenseData[2]));
				expense.lastElement().setTotalCost(Double.parseDouble(expenseData[3]));
			}
			
			reader.close();
		} catch(IOException ex){
			ex.printStackTrace();
		}
		
		return expense;
	}

}
