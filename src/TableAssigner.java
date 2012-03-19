import java.util.*;

public class TableAssigner {
	private Vector<Guest> guests;
	private int tableSize;
	
	private Vector<Chromosome> currPopulation;
	private Vector<Chromosome> newPopulation;
	private int fitnessSumOfCurrPopulation;		//sum of fitness values of all chromosomes in currPopulation
	private int fitnessSumOfNewPopulation;
	private int eliteIndex;					//index of chromosome in currPopulation that has highest fitness value
	private Vector<Integer> seatsPerTable;
	
	//algorithm settings
	private int minTableSize;
	private int populationSize;
	private int numberOfGenerations;
	private int singlePointCross;
	
	private Random rnd;
	
	public TableAssigner(){
		/*
		this.currPopulation = new Vector<Chromosome>();
		this.newPopulation = new Vector<Chromosome>();
		this.seatsPerTable = new Vector<Integer>();
		this.fitnessSumOfCurrPopulation = 0;
		this.fitnessSumOfNewPopulation = 0;
		*/
		
		this.minTableSize = 7;
		this.populationSize = 60;
		this.numberOfGenerations = 10000;

		this.rnd = new Random();
	}
	
	public int getMinTableSize(){
		return minTableSize;
	}
	
	public int getPopulationSize(){
		return populationSize;
	}
	
	public int getNumberOfGenerations(){
		return numberOfGenerations;
	}
	
	public void setNumberOfGenerations(int number){
		this.numberOfGenerations = number;
	}
	
	public int getBestFitness(){
		return currPopulation.get(eliteIndex).getFitnessValue();
	}
	
	//overall method called
	public Vector<Vector<Guest>> generateArrangement(Vector<Guest> guestList, int tableSize){
		//init
		this.currPopulation = new Vector<Chromosome>();
		this.newPopulation = new Vector<Chromosome>();
		this.seatsPerTable = new Vector<Integer>();
		this.fitnessSumOfCurrPopulation = 0;
		this.fitnessSumOfNewPopulation = 0;
		this.guests = guestList;
		this.singlePointCross = guests.size()/2;
		this.tableSize = tableSize;
		
		findOptimalSeatsPerTable();
		
		generateInitialPopulation();
		
		for(int k = 0; k < numberOfGenerations; k++){
			evolve();
		}
			
		Chromosome bestChromosome = currPopulation.get(eliteIndex);
		System.out.println(bestChromosome.getFitnessValue());
		Vector<Vector<Guest>> seatingList = new Vector<Vector<Guest>>();
		
		int index = 0;
		for(int i = 0; i < seatsPerTable.size(); i++){
			int currTableSize = seatsPerTable.get(i);
			Vector<Guest> currTable = new Vector<Guest>();
			
			for(int j = 0; j < currTableSize; j++){
				currTable.add(guests.get(bestChromosome.getChromosome().get(index+j)));
			}
			seatingList.add(currTable);
			index += currTableSize;
		}
		
		return seatingList;
	}
	
	private void findOptimalSeatsPerTable(){
		int totalGuests = guests.size();
		//greedily select for tables
		//result: will have tables with max capacity + 1 table with totalGuests%tableSize number
		for(int i = 0; i < guests.size();){
			if(totalGuests > tableSize){
				totalGuests -= tableSize;
				seatsPerTable.add(tableSize);
				i += tableSize;
			}
			else{
				seatsPerTable.add(totalGuests);
				i += totalGuests;
			}
		}
		
		//optimize - if last table is less than minTableSize, bring guests over from other tables
		while(seatsPerTable.lastElement() < minTableSize){
			int index = seatsPerTable.size() - 1;
			for(int i = index; i > 0; i--){
				//decrement size of 1 table
				seatsPerTable.set(i, seatsPerTable.get(i)-1);
				//increment size of last table
				seatsPerTable.set(seatsPerTable.size()-1, seatsPerTable.lastElement()+1);
				if(seatsPerTable.lastElement() >= minTableSize)
					break;
			}
		}
	}
	
	private void generateInitialPopulation(){
		//first chromosome should use default vector passed in
		Vector<Integer> firstChromosome = new Vector<Integer>();
		for(int i = 0; i < guests.size(); i++)
			firstChromosome.add(i);
		currPopulation.add(new Chromosome(firstChromosome));
		currPopulation.get(0).setFitnessValue(fitnessFunction(currPopulation.get(0)));
		fitnessSumOfCurrPopulation += currPopulation.get(0).getFitnessValue();
		eliteIndex = 0;
		
		//add rest of population randomly
		for(int i = 1; i < populationSize; i++){
			Vector<Integer> newChromosome = new Vector<Integer>(firstChromosome);
			Collections.shuffle(newChromosome);
			
			currPopulation.add(new Chromosome(newChromosome));
			
			int newFitnessValue = fitnessFunction(currPopulation.get(i));
			currPopulation.get(i).setFitnessValue(newFitnessValue);
			
			if(newFitnessValue > currPopulation.get(eliteIndex).getFitnessValue())
				eliteIndex = i;
			
			fitnessSumOfCurrPopulation += newFitnessValue;
		}
	}
	
	private void evolve(){
		//given a population
		//find elite chromosome and bring over
		//generate next generation by
		//	select() 2 chromosome
		//	cross() the 2 chromosome
		//	mutate() the resulting chromosome
		//	add to population
		//repeat until population number is full
		
		fitnessSumOfNewPopulation = 0;
		newPopulation = new Vector<Chromosome>();
		newPopulation.add(currPopulation.get(eliteIndex));
		eliteIndex = 0;
		fitnessSumOfNewPopulation += newPopulation.get(0).getFitnessValue();
		
		
		for(int i = 1; i < populationSize; i++){
			Chromosome parent1 = select();
			Chromosome parent2 = select();
			
			Chromosome newChromosome = cross(parent1, parent2);
			
			mutate(newChromosome);
			
			newChromosome.setFitnessValue(fitnessFunction(newChromosome));
			
			fitnessSumOfNewPopulation += newChromosome.getFitnessValue();
			
			if(newChromosome.getFitnessValue() > newPopulation.get(eliteIndex).getFitnessValue())
				eliteIndex = i;
			
			newPopulation.add(newChromosome);
		}
		
		
		currPopulation = newPopulation;
		fitnessSumOfCurrPopulation = fitnessSumOfNewPopulation;
	}
	
	private Chromosome cross(Chromosome parent1, Chromosome parent2){
		//copy from chrom1 elements before singlePointCross
		//scan chrom2, if element not in, copy over
		
		Chromosome childChromosome = new Chromosome(new Vector<Integer>());
		double hashCapacity = parent1.getChromosome().size() * 1.33;
		HashMap<Integer, Boolean> isInChild = new HashMap<Integer, Boolean>((int)hashCapacity);
		
		for(int i = 0; i < singlePointCross; i++){
			int toAdd = parent1.getChromosome().get(i);
			childChromosome.getChromosome().add(toAdd);
			isInChild.put(parent1.getChromosome().get(i), true);
		}
		
		for(int i = 0; i < parent2.getChromosome().size(); i++){
			//if hash is true, skip else add to childChromosome
			if(!isInChild.containsKey(parent2.getChromosome().get(i))){
				childChromosome.getChromosome().add(parent2.getChromosome().get(i));
			}
		}
		
		childChromosome.setFitnessValue(fitnessFunction(childChromosome));
		return childChromosome;
	}
	
	private void mutate(Chromosome chrom){
		int first = rnd.nextInt(chrom.getChromosome().size());
		int second = rnd.nextInt(chrom.getChromosome().size());
		
		//swap
		int temp = chrom.getChromosome().get(first);
		chrom.getChromosome().set(first, chrom.getChromosome().get(second));
		chrom.getChromosome().set(second, temp);
	}
	
	private Chromosome select(){
		/*
		 * select() uses algorithm called roulette wheel selection
		 * 1) calculate sum of all chromosome fitness in population (done when generating population)
		 * 2) select random number from 0 to the sum
		 * 3) go through population and sum until it exceeds rnd. pick that chromosome
		 */
		
		int selectNumber = rnd.nextInt(fitnessSumOfCurrPopulation);
		int sum = 0;
		for(int i = 0; i < currPopulation.size(); i++){
			sum += currPopulation.get(i).getFitnessValue();
			if(sum > selectNumber){
				return currPopulation.get(i);
			}
		}
		
		System.out.println(fitnessSumOfCurrPopulation);
		System.out.println(selectNumber + " " + sum);
		return new Chromosome(new Vector<Integer>());
	}
	
	//OLD FITNESS FUNCTION
	/*
	private int fitnessFunction(Chromosome chrom){
		int fitness = 100;
		Vector<Integer> chromosomeSolution = chrom.getChromosome();
		int chromosomeIndex = 0;
		int subtract = 0;

		
		for(Integer seats : seatsPerTable){				//for each table
			for(int j = 0; j < seats-1; j++){				//for each person at the table
				boolean lonePerson = true;
				String guestAGroup = guests.get(chromosomeSolution.get(chromosomeIndex + j)).getGroup();
				
				for(int k = j+1; k < seats; k++){			//for every other person at the table
					
					String guestBGroup = guests.get(chromosomeSolution.get(chromosomeIndex + k)).getGroup();
					if(guestAGroup.equals(guestBGroup)){
						lonePerson = false;
						fitness++;
					}
				}
				
				if(lonePerson){
					subtract += 20;
				}
			}
			chromosomeIndex += seats;
		}
		
		if(subtract < fitness)
			fitness -= subtract;
		else
			fitness = 1;
		return fitness;
	}
	*/
	
	//new fitness function
	//idea: count frequency of each group type in a table
	//if there exist a group with only 1 representative, subtract points
	//use weights to favour certain number. eg. 2 is not as desirable as 5 or 6
	//2 - 1, 3 - 2, 4 - 3, 5 - 5, 6 - 5, 7 - 3, 8 - 2, 9 - 2, 10 - 2
	
	private int fitnessFunction(Chromosome chrom){
		int fitness = 100;
		Vector<Integer> chromosomeSolution = chrom.getChromosome();
		int chromosomeIndex = 0;
		int subtract = 0;
		HashMap<String, Integer> groupFrequency;
		
		for(Integer seats : seatsPerTable){
			groupFrequency = new HashMap<String, Integer>();
			
			for(int j = 0; j < seats; j++){
				String group = guests.get(chromosomeSolution.get(chromosomeIndex + j)).getGroup();
				Integer count = groupFrequency.get(group);
				if(count == null)	//group not added to hash yet
					groupFrequency.put(group, 1);
				else
					groupFrequency.put(group, count+1);
			}
			
			//calculate table's fitness
			Collection<Integer> frequencies = groupFrequency.values();
			for(Integer currFrequency : frequencies){
				switch(currFrequency){
				case 1:
					subtract += 20;
					break;
				case 2:
					//fitness += 2;
					break;
				case 3:
				case 4:
				case 5:
					fitness += currFrequency*2;
					break;
				case 6:
				case 7:
				case 8:
				case 9:
				case 10:
					fitness += 20;
					break;
				}
			}
			
			chromosomeIndex += seats;
		}
		
		if(subtract < fitness)
			fitness -= subtract;
		else
			fitness = 1;
		
		return fitness;
	}
}