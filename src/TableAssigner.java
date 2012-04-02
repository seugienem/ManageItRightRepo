import java.util.*;

public class TableAssigner {
	private Vector<Guest> guests;
	private int tableSize;
	
	private boolean random;
	
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
	
	protected javax.swing.event.EventListenerList listenerList = new javax.swing.event.EventListenerList();
	
	public TableAssigner(){
		/*
		this.currPopulation = new Vector<Chromosome>();
		this.newPopulation = new Vector<Chromosome>();
		this.seatsPerTable = new Vector<Integer>();
		this.fitnessSumOfCurrPopulation = 0;
		this.fitnessSumOfNewPopulation = 0;
		*/
		
		this.minTableSize = 9;
		this.populationSize = 60;
		this.numberOfGenerations = 1000;
		this.random = false;
		
		this.rnd = new Random();
	}
	
	public Vector<Integer> getSeatsPerTable(){
		return seatsPerTable;
	}
	
	public boolean getRandom(){
		return random;
	}
	
	public void setRandom(boolean random){
		this.random = random;
	}
	
	public int getMinTableSize(){
		return minTableSize;
	}
	
	public int getPopulationSize(){
		return populationSize;
	}
	
	public void setPopulationSize(int popSize){
		this.populationSize = popSize;
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
	
	public void addEventListener(ProgressListener listener){
		listenerList.add(ProgressListener.class, listener);
	}
	
	public void removeEventListener(ProgressListener listener){
		listenerList.remove(ProgressListener.class, listener);
	}
	
	public void fireEvent(ProgressEvent evt){
		Object[] listeners = listenerList.getListenerList();
		
		for(int i = 0; i < listeners.length; i+=2){
			if(listeners[i] == ProgressListener.class){
				((ProgressListener)listeners[i+1]).progressEventOccured(evt);
			}
		}
	}
	
	//overall method called
	public Vector<Integer> generateArrangement(Vector<Guest> guestList, int tableSize){
		Chromosome bestChromosome = null;
		this.guests = guestList;
		this.tableSize = tableSize;
		this.seatsPerTable = new Vector<Integer>();
		
		Collections.sort(guests);
		if(random){
			findOptimalSeatsPerTable();
			
			Vector<Integer> random = new Vector<Integer>();
			for(int i = 0; i < guests.size(); i++)
				random.add(i);
			Collections.shuffle(random);
			
			bestChromosome = new Chromosome(random);
		}
		else{
			//init
			this.currPopulation = new Vector<Chromosome>();
			this.newPopulation = new Vector<Chromosome>();
			this.fitnessSumOfCurrPopulation = 0;
			this.fitnessSumOfNewPopulation = 0;
			this.singlePointCross = guests.size()/2;

			findOptimalSeatsPerTable();

			generateInitialPopulation();

			for(int k = 0; k < numberOfGenerations; k++){
				float progress = (float)k / (float)numberOfGenerations;
				progress *= 100;
				fireEvent(new ProgressEvent(this, (int)progress));
				
				evolve();
			}
				
			bestChromosome = currPopulation.get(eliteIndex);
			System.out.println(bestChromosome.getFitnessValue());
			

		}
		
		
		fireEvent(new ProgressEvent(this, 100));
		
		/*
		Vector<Vector<String>> seatingList = new Vector<Vector<String>>();		
		//this returns Tables in columns 
		for(int i = 0; i < tableSize; i++){
			seatingList.add(new Vector<String>());
		}
		int index = 0;
		for(int i = 0; i < seatsPerTable.size(); i++){
			int currTableSize = seatsPerTable.get(i);
			
			for(int j = 0; j < currTableSize; j++){
				int currGuestIndex = bestChromosome.getChromosome().get(index+j);
				seatingList.get(j).add(guests.get(currGuestIndex).getName());
			}
			index += currTableSize;
		}
		*/
		/*
		 *this return Table in rows instead of columns
		int index = 0;
		for(int i = 0; i < seatsPerTable.size(); i++){
			int currTableSize = seatsPerTable.get(i);
			Vector<String> currTable = new Vector<String>();
			
			for(int j = 0; j < currTableSize; j++){
				currTable.add(guests.get(bestChromosome.getChromosome().get(index+j)).getName());
			}
			seatingList.add(currTable);
			index += currTableSize;
		}
		*/	
		return bestChromosome.getChromosome();
	}
	
	private void findOptimalSeatsPerTable(){
		int totalGuests = guests.size();
		if(totalGuests < tableSize){
			seatsPerTable.add(totalGuests);
			return;
		}
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
		
		//check if possible to optimize
		if(seatsPerTable.lastElement() < minTableSize){
			int difference = minTableSize - seatsPerTable.lastElement();
			
			int numberOfTables = seatsPerTable.size();
			if(difference < numberOfTables - 1){	//if there aren't enough tables to bring over
				int perTable = totalGuests / numberOfTables;
				int remaining = totalGuests % numberOfTables;
				
				seatsPerTable = new Vector<Integer>();
				for(int i = 0; i < numberOfTables; i++){
					seatsPerTable.add(perTable);
				}
				seatsPerTable.set(0, seatsPerTable.get(0) + remaining);
			}
			return;
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
		singlePointCross = rnd.nextInt(parent1.getChromosome().size());
		
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
					subtract += 35;
					break;
				case 2:
					subtract += 20;
					break;
				case 3:
					break;
				case 4:
				case 5:
					fitness += currFrequency*3;
					break;
				case 6:
				case 7:
				case 8:
				case 9:
				case 10:
					fitness += 35;
					break;
				}
			}
			
			if(frequencies.size() <= 2)
				fitness += 10;
			
			chromosomeIndex += seats;
		}
		
		if(subtract < fitness)
			fitness -= subtract;
		else
			fitness = 1;
		
		return fitness;
	}
}