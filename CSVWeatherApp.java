package coursera_weather_assignment;

import edu.duke.*;

import java.io.File;

import org.apache.commons.csv.*;


/*
 * Program analyzes a CSV file and identifies largest or smallest values in a a column of a
 * particular file OR files. Also, find average of a value for a selected file.
 *  Program was made adhering to requirements for practice problem
 * from Coursera course by Duke University.
 * 
 * @author: Zev
 * 
 * @since 27-04-2018
 */

public class CSVWeatherApp {
	
	
	/*
	 * Method finds largest value between two entries in a row name searchColNam and returns the row with largest
	 */
	public CSVRecord getLargestOf(CSVRecord currentRow, CSVRecord largestSoFar, String searchColName) {
		
		if(currentRow.get(searchColName).equals("N/A")) { //prevent number format error
			return largestSoFar; //No data available in that row
		}
		
		if(largestSoFar == null) {
			largestSoFar = currentRow;
		}
		else {
			double currentTemp = Double.parseDouble(currentRow.get(searchColName));
			double largestTemp = Double.parseDouble(largestSoFar.get(searchColName));
			
			if(currentTemp > largestTemp) {
				largestSoFar = currentRow;
			}
		}
		return largestSoFar;
	}
	
	
	/*
	 * Method finds smallest value between two entries in a row name searchColName
	 */
	public CSVRecord getSmallestOf(CSVRecord currentRow, CSVRecord smallestSoFar, String searchColName) {
		
		if(currentRow.get(searchColName).equals("N/A")) { //prevent number format error
			return smallestSoFar; //No data available in that row
		}
		
		if(smallestSoFar == null) {
			smallestSoFar = currentRow;
		}
		else {
			double currentTemp = Double.parseDouble(currentRow.get(searchColName));
			double smallestTemp = Double.parseDouble(smallestSoFar.get(searchColName));
			
			if(currentTemp < smallestTemp && currentTemp != -9999) { //-9999 in CSV designates no value recorded
				smallestSoFar = currentRow;
			}
		}
		return smallestSoFar;
	}
	
	
	/*
	 * Method returns largest value within a file for a specific column in searchColName
	 */
	public CSVRecord largestInFile(CSVParser parser, String searchColName) {
		
		CSVRecord largestSoFar = null;
		
		//Locates coldest temp in CSV file
		for(CSVRecord currentRow : parser) {
			largestSoFar = getLargestOf(currentRow, largestSoFar, searchColName);
		}
		return largestSoFar; //returns largest value in column searchColName 
	}
	
	
	/*
	 * Method returns smallest value within a file for a specific column in searchColName
	 */
	public CSVRecord smallestInFile(CSVParser parser, String searchColName) {

		CSVRecord smallestSoFar = null;
		
		//Locates coldest temp in CSV file
		for(CSVRecord currentRow : parser) {
			smallestSoFar = getSmallestOf(currentRow, smallestSoFar, searchColName);
		}
		return smallestSoFar; //returns largest value in column searchColName 
	}
	
	
	/*
	 * Method returns largest value from a selection of multiple files by calling 
	 * largestInFile method.
	 */
	public void FilesWithLargest(String columnName) {
		
		//String searchColName = "Humidity"; //Desired category to base largest amount on
		String searchColName = columnName;
		
		CSVRecord largestSoFar = null;
		
		DirectoryResource dr = new DirectoryResource();
		
		for(File file : dr.selectedFiles()) {
		
			FileResource fr = new FileResource(file);
			CSVRecord current = largestInFile(fr.getCSVParser(), searchColName); //returns largest value in a file
			
			if(largestSoFar == null) largestSoFar = current;
			
			double currentTemp = Double.parseDouble(current.get(searchColName));
			double largestTemp = Double.parseDouble(largestSoFar.get(searchColName));

			if(currentTemp > largestTemp) {
				largestSoFar = current; //record row with the largest value 
			}
		}
		String row = largestSoFar.get(searchColName); //row in the files contains largest value	
		String time = largestSoFar.get(0); //time is in very first column
		
		System.out.println("Largest value for " + searchColName + " is " + row);
		System.out.println("and it occured at: " + time);
		System.out.println("DateUTC: " + largestSoFar.get(13)); //prints value in column 13 where DateUTC is
	}
	
	
	/*
	 * Method returns smallest value from a selection of multiple files by calling 
	 * smallestInFile method.
	 */
	public void FilesWithSmallest(String columnName) {
		
		//String searchColName = "Humidity"; //Desired category to base largest amount on
		String searchColName = columnName;
		
		CSVRecord smallestSoFar = null;
		
		DirectoryResource dr = new DirectoryResource();
		
		for(File file : dr.selectedFiles()) {
		
			FileResource fr = new FileResource(file);
			CSVRecord current = smallestInFile(fr.getCSVParser(), searchColName); //returns largest value in a file
			
			if(smallestSoFar == null) smallestSoFar = current;
			
			double currentTemp = Double.parseDouble(current.get(searchColName));
			double smallestTemp = Double.parseDouble(smallestSoFar.get(searchColName));

			if(currentTemp < smallestTemp) {
				smallestSoFar = current; //record row with the largest value
			}
		}
		String row = smallestSoFar.get(searchColName); //row in the files contains largest value
		String time = smallestSoFar.get(0); //time is in very first column
		
		System.out.println(); //Space
		System.out.println("Smallest value for " + searchColName + " is " + row);		
		System.out.println("and it occured at: " + time);
		System.out.println("DateUTC: " + smallestSoFar.get(13)); //prints value in column 13 where DateUTC is
	}
	
	
	/*
	 * Method findAverageInFile takes two parameters. First parameter takes name of column to inspect.
	 * The second parameter takes the average over values at and about that parameter. 
	 * Note: If an average of all values is desired, input -1000 for second parameter
	 */
	public void findAverageInFile(String searchColName, double val) {
		
		double total = 0.0;
		
		double temperatureTotal = 0.0;
		
		int count = 0;
		
		FileResource fr = new FileResource();
		
		CSVParser parser = fr.getCSVParser();
		
		for(CSVRecord row : parser) {
			
			double currentVal = Double.parseDouble(row.get(searchColName));
			
			if(val == -1000) { //take average of ALL VALUES
				count++;
				total = total + currentVal;
			}
			else {
				if(currentVal >= val) {
					count++;
					total = total + currentVal;
					temperatureTotal = temperatureTotal + Double.parseDouble(row.get(1));
				}
			}
		}
		if(count != 0) {
			double avg = total / (double)count;
			double avgTemp = temperatureTotal / (double)count;
			
			System.out.println("The average for " + searchColName + " is: " + avg);
			System.out.println("The average Temperature for a humitity of " + val + " or greater is: " + avgTemp);
		}
		else { //avoids division by zero.
			System.out.print("No values found that matched paramters.");
		}
	}
	
	
	/*
	 * Method calls: FilesWithSmallest(), FilesWithLargest() and
	 * parameter input is the name of column in the CSV to inspect
	 */
	public void getFileData(String columnName) {
		
		System.out.println("#1. Choose file(s) to find largest value.");
		FilesWithLargest(columnName);
		
		System.out.println(); //spacing
		System.out.println("#2. Choose file(s) to find smallest value.");
		FilesWithSmallest(columnName);
	}
	
	
	public static void main(String args[]) {
		
		CSVWeatherApp cwa = new CSVWeatherApp();
		
		String columnNameToSearch = "Humidity"; //Change this to search in another column
		//String columnNameToSearch = "TemperatureF";
		
		/*
		 * Used to find average temperature when humidity is greater-equal to this value
		 * IMPORTANT for a value of -1000 method finds average of all values in column
		 */
		int humidityValue = 80;
		
		try { //Catch NullPointException when user doesn't chose a file, object is null.
			
			cwa.getFileData(columnNameToSearch); 
			
			System.out.println();
			System.out.println("Chose file to find average.");
			cwa.findAverageInFile(columnNameToSearch, humidityValue); 
	
		} catch(NullPointerException e) {
			e.printStackTrace();
		}
	}
}
