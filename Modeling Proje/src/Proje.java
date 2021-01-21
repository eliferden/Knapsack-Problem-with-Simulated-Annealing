import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;

import java.awt.List;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Random;

import java.io.PrintWriter;

public class Proje {

	public static void main(String[] args) throws IOException, InvalidFormatException {
		Scanner input = new Scanner(System.in);
		System.out.println("<<<<<< Knapsack Problem with Simulated Annealing >>>>>>\n");

		// System.out.println("Enter the name of the Excel file: ");
		// String fileName = input.nextLine();
		// String file_path = "C:/Users/elif.erden/Downloads/" + fileName + ".xlsx";
		// String file_path = "C:/Users/user/Desktop/Proje/Instance_100_997.xlsx";
		// String file_path = "C:/Users/user/Desktop/Proje/Instance_24_6404180.xlsx";
	    //  String file_path = "C:/Users/user/Desktop/Proje/Instance_100_995.xlsx";
		// String file_path = "C:/Users/user/Desktop/Proje/Instance_200_1008.xlsx";
		String file_path = "C:/Users/Elif Erden/Desktop/Instance_24_6404180.xlsx";
		 
		 
		// Creating a Workbook from an Excel file (.xls or .xlsx)
		Workbook workbook = WorkbookFactory.create(new File(file_path));
		// Getting the first sheet in the workbook
		Sheet sheet = workbook.getSheetAt(0);
		// Creating a DataFormatter to format and get each cell's value as String


		Long capacity = (long) sheet.getRow(1).getCell(1).getNumericCellValue();
		System.out.println("Knapsack capacity: " + capacity + "\n");
		int variableNumber = (int) sheet.getRow(0).getCell(1).getNumericCellValue();
		System.out.println("Variable number:  " + variableNumber + "\n");

		// Array for profit
		double[] profitArray = new double[variableNumber];
		for (int r = 0; r < profitArray.length; r++) {

			profitArray[r] = (int) sheet.getRow(r + 4).getCell(0).getNumericCellValue();
		}

		System.out.print("Profit Vector: " + Arrays.toString(profitArray) + "\n\n");

		// Array for weight
		double[] weightArray = new double[variableNumber];
		for (int r = 0; r < weightArray.length; r++) {

			weightArray[r] = (int) sheet.getRow(r + 4).getCell(1).getNumericCellValue();
		}

		System.out.print("Weight Vector: " + Arrays.toString(weightArray) + "\n\n");

		// Step 1: initial solution
		double[] currentSolution = new double[variableNumber];
		double[] initialSolution = new double[variableNumber];
		//for (int i = 0; i < initialSolution.length; i++) {

			//initialSolution[i] = 0;
		//}
		// x1
		initialSolution = findInitialSolution(profitArray, weightArray, capacity);
		currentSolution = findInitialSolution(profitArray, weightArray, capacity);
		System.out.println("Initial Solution: " + Arrays.toString(currentSolution) + "\n");

		// F* = F(x1)
		long optimumObjective = calculateObjectiveValue(currentSolution, profitArray);
		System.out.println("Objective Value: " + optimumObjective + "\n");

		// method for update x*
		// x* = x1
		double[] optimumSolution = new double[variableNumber];
		for (int i = 0; i < optimumSolution.length; i++) {

			optimumSolution[i] = currentSolution[i];
		}
		double delta=0;
		for (int i = 0; i < profitArray.length; i++) {
			delta=profitArray[i]+delta;
		}
        delta=delta/profitArray.length;
        System.out.println(delta);
        double temperature =(-1*delta)/0.10536051565;
        System.out.println(temperature);
		
        
		System.out.println("Please choose the mod of the simulation: \n Write \n 1 for Fast Simulation (Fast but result may not be satisfying) \n"
				+ " 2 for Normal Simulation (Balanced Performance and Speed) \n 3 for Advanced Simulation (Most accurate results but much more slower)");
		String userInput = input. nextLine();
		int plateauNumber =500;
		if(userInput=="1") {
			 plateauNumber =profitArray.length*200;// L
			
			
		}else if(userInput=="2") {
			 plateauNumber =profitArray.length*2000;// L
			
			
		}else if(userInput=="3") {
			 plateauNumber =profitArray.length*20000;// L
			
		}else 
			System.out.println("Your input is incorrect");

        
	    // double temperature = -18982536; // when delta = 2000000, p0 = 0.9
		// double temperature = -8962840; // when delta = 2000000, p0 = 0.8
		// double temperature = -3796488; // when delta = 40000, p0 = 0.9
		// double temperature = -1792568; // when delta = 40000, p0 = 0.8
		// Excel 2
		// double temperature = -896; // when delta = 200, p0 = 0.8
		// double temperature = -1898; // when delta = 200, p0 = 0.9

		int k = 0;
		double alpha = 0.85;		 
		boolean stoppingCondition = true;
		int moveCounter = 0;
		int objectiveCounter = 0;
		//double temperature =(-1*delta)/0.10536051565;

		File file = new File("C:/Users/Elif Erden/Desktop/objective.txt");
		PrintWriter output = new PrintWriter(file);
 
		
		while (stoppingCondition == true) {
			// while (objectiveCounter < 20)
			System.out.println("<<<<<<< Iteration number " + (k + 1) + " >>>>>>");
			System.out.println("---------------------------------\n");

			// propose a move for the initial solution
			double randomSolution[] = proposeMove(currentSolution, variableNumber, weightArray, capacity);
			System.out.println("Picked Neighbor: " + Arrays.toString(randomSolution) + "\n\n");

			if (calculateObjectiveValue(randomSolution, profitArray) > calculateObjectiveValue(currentSolution,
					profitArray)) {

				// AcceptMove
				for (int i = 0; i < currentSolution.length; i++) {

					currentSolution[i] = randomSolution[i];
				}

				// currentSolution = randomSolution; //accept
				moveCounter++;

				if (calculateObjectiveValue(randomSolution, profitArray) > calculateObjectiveValue(optimumSolution,
						profitArray)) {

					for (int i = 0; i < optimumSolution.length; i++) {

						optimumSolution[i] = randomSolution[i];
					}
					// optimumSolution = randomSolution;
					optimumObjective = calculateObjectiveValue(randomSolution, profitArray);

					objectiveCounter = 0;


				}

			} else {

				objectiveCounter++;

				// Toss
				long deltaObjective = calculateObjectiveValue(randomSolution, profitArray)- calculateObjectiveValue(currentSolution, profitArray);
				System.out.println("Objective of random solution:" + calculateObjectiveValue(randomSolution, profitArray));
				double probability = java.lang.Math.exp(-deltaObjective / temperature);
				System.out.println("Delta Objective: " + deltaObjective);
				System.out.println("Probability: " + probability);
				Random rand = new Random();
				double randomNumber = rand.nextDouble() * 1;
				System.out.println("Random number: " + randomNumber);
				if (randomNumber <= probability) {

					for (int i = 0; i < currentSolution.length; i++) {

						currentSolution[i] = randomSolution[i];
					}
					// currentSolution = randomSolution; //accept
					moveCounter++;

				} else {

					// currentSolution = currentSolution;
				}
			}

			if (k % plateauNumber == 0) {
				temperature = alpha * temperature;
			}

			// if((objectiveCounter > 2*plateauNumber) && (moveCounter < 0.2*plateauNumber))
			// {
			// if((objectiveCounter > 20) && (moveCounter < 0.2*plateauNumber)) {

			System.out.println(
					"Objective function value: " + calculateObjectiveValue(currentSolution, profitArray) + "\n");

			output.println(calculateObjectiveValue(currentSolution, profitArray));
			System.out.println("Objective counter: " + objectiveCounter + "\nMove counter: " + moveCounter + "\n");
			k++;
			if (k % 2 * plateauNumber == 0)
				moveCounter = 0;

			if ((objectiveCounter > 2 * plateauNumber) && (moveCounter < 0.02 * plateauNumber)) {
				// if(objectiveCounter > 2*plateauNumber){

				stoppingCondition = false;

			}

			System.out.println("\nStop: " + stoppingCondition);

		}

		System.out.println("\nOptimum Solution: " + Arrays.toString(optimumSolution));
		System.out.println("Optimum Objective Value: " + calculateObjectiveValue(optimumSolution, profitArray));
		System.out.println("\nInitial Solution: " + Arrays.toString(initialSolution));
		System.out.println("Initial Objective Value: " + calculateObjectiveValue(initialSolution, profitArray));
		output.close();

	}

	private static long calculateObjectiveValue(double[] solution, double[] profit) {

		long objectiveValue = 0;
		for (int i = 0; i < solution.length; i++) {

			objectiveValue = objectiveValue + (long) (solution[i] * profit[i]);
		}
		return objectiveValue;

	}

	private static double[] proposeMove(double[] solution, int vNumber, double[] weight, long capacity) {

		// Array List type
		// ArrayList<ArrayList<Double>> neighbor = new ArrayList<ArrayList<Double>>();
		// neighbor.add(new ArrayList<Double>());
		// neighbor.add(new ArrayList<Double>());

		double[][] neighborhood = new double[vNumber][vNumber];
		double[] neighbor = new double[vNumber];
		int flag = 0;
		int counter = 0;

		for (int j = 0; j < vNumber; j++) {
			for (int i = 0; i < vNumber; i++) {

				if (flag == i) {

					if (solution[flag] == 0) {
						neighbor[flag] = 1;
					} else {
						neighbor[flag] = 0;
					}

				} else {
					neighbor[i] = solution[i];
				}

			}

			// checking feasibility
			boolean isFeasible = checkFeasibility(neighbor, weight, capacity);
			if (isFeasible == true) {
				// adding feasible neighbor to neighborhood
				for (int z = 0; z < vNumber; z++) {
					neighborhood[j][z] = neighbor[z];
				}
				counter++;
			} else {

				for (int z = 0; z < vNumber; z++) {
					neighborhood[j][z] = 5;
				}
			}
			flag++;
		}

		// counter = counter - 1;
		int c = 0;
		double[][] feasibleNeighborhood = new double[counter][vNumber];
		for (int i = 0; i < vNumber; i++) {

			if (neighborhood[i][0] != 5) {

				for (int j = 0; j < vNumber; j++) {

					feasibleNeighborhood[c][j] = neighborhood[i][j];
				}
				c++;
			}
		}

		// System.out.println("Neighborhood: " + Arrays.deepToString(neighborhood) +
		// "\nRow Number of Neighborhood: "
		// + neighborhood.length + "\n");
		System.out.println("Number of infeasible neighbors: " + (vNumber - counter) + "\n");
		// System.out.println("Feasible Neighborhood: " +
		// Arrays.deepToString(feasibleNeighborhood)
		// + "\nRow Number of Feasible Neighborhood: " + feasibleNeighborhood.length +
		// "\n");

		// pick a random neighbor from neighborhood and return this neighbor
		double[] pickedNeighbor = new double[vNumber];
		Random rand = new Random();
		int randomNumber = rand.nextInt(counter);
		for (int z = 0; z < vNumber; z++) {
			pickedNeighbor[z] = feasibleNeighborhood[randomNumber][z];
		}
		System.out.println(">>>>>> Neighbor number " + (randomNumber + 1) + " was picked");
		return pickedNeighbor;

	}

	private static boolean checkFeasibility(double[] solution, double[] weight, long capacity) {

		long total = 0;
		for (int i = 0; i < solution.length; i++) {

			total = total + (long) (solution[i] * weight[i]);
		}
		if (total > capacity) {
			return false;
		} else {
			return true;
		}
	}

	public static double[] findInitialSolution(double[] profitArray, double[] weightArray, double capacity) {

		double[] weight = new double[weightArray.length];
		for (int i = 0; i < weightArray.length; i++)
			weight[i] = weightArray[i];

		double[] initialSolution = new double[profitArray.length];
		double[] priority = new double[profitArray.length];
		double[] index = new double[profitArray.length];
		double temp;
		double temp2;
		double temp3;

		double capacityCounter = 0;

		for (int i = 0; i < priority.length; i++)
			index[i] = i + 1;

		for (int i = 0; i < priority.length; i++)
			priority[i] = profitArray[i] / weight[i];

		for (int j = 0; j < priority.length; j++) {
			for (int i = j; i < priority.length; i++) {
				if (priority[i] >= priority[j]) {

					temp = priority[j];
					priority[j] = priority[i];
					priority[i] = temp;

					temp2 = index[j];
					index[j] = index[i];
					index[i] = temp2;

					temp3 = weight[j];
					weight[j] = weight[i];
					weight[i] = temp3;
				}
			}
		}

		for (int i = 0; capacityCounter < capacity; i++) {
			capacityCounter = capacityCounter + weight[i];
			initialSolution[i] = 1;
			if (capacityCounter + weight[i] > capacity)
				break;
		}
		for (int j = 0; j < index.length; j++) {
			for (int i = j; i < index.length; i++) {
				if (index[i] <= index[j]) {

					temp = index[i];
					index[i] = index[j];
					index[j] = temp;

					temp2 = initialSolution[i];
					initialSolution[i] = initialSolution[j];
					initialSolution[j] = temp2;
				}
			}
		}
		for (int j = 0; j < index.length; j++) {
			for (int i = j; i < index.length; i++) {
				if (index[i] <= index[j]) {
					temp2 = initialSolution[i];
					initialSolution[i] = initialSolution[j];
					initialSolution[j] = temp2;
				}
			}
		}

		return initialSolution;
	}

}
