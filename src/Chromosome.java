import java.util.*;

public class Chromosome {
	private int fitnessValue;
	private Vector<Integer> chromosomeList;
	
	public Chromosome(Vector<Integer> chromosomeList){
		this.chromosomeList = chromosomeList;
		//calculate fitness value
	}
	
	public int getFitnessValue(){
		return fitnessValue;
	}
	
	public void setFitnessValue(int fitness){
		this.fitnessValue = fitness;
	}
	
	public Vector<Integer> getChromosome(){
		return chromosomeList;
	}
	
	public void setChromosome(Vector<Integer> chromosome){
		this.chromosomeList = chromosome;
	}
}
