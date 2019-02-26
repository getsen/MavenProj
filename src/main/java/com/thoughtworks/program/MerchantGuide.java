package com.thoughtworks.program;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.apache.log4j.Logger;

/**
* The MerchantGuide program handles the input data from file and 
* logs the output accordingly.
* 
* @author  Senthil Kumar
* @version 1.0
* @since   2019-02-03 
*/
public class MerchantGuide {
	
	static final  Logger logger = Logger.getLogger(MerchantGuide.class);
	
	// These constants can be moved to separate file if needed
	private static final String HOW_MUCH = "how much is ";
	private static final String HOW_MANY = "how many Credits is ";
	private static final String EMPTY_SPACE = " ";
	private static final String SPACE_WTIH_IS = " is";
	private static final String QUESTION_MARK = " ?";
	private static final String CREDITS = " Credits";
	private static final String[] ROMAN_LETTER_ARRAY = { "I", "V", "X", "L", "C", "D", "M" };
	private static final int[] ROMAN_VALUES = { 1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1 };
	private static final String[] ROMAN_LETTER_PATTERN = { "M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I" };
	
	public static void main(String[] args) throws IOException {
		MerchantGuide merchantGuide = new MerchantGuide();
		merchantGuide.initMerchantGuide();
	}
	
	/*
	 * This method is used to read the file from resource folder.
	 * Read through the lines and handle each line according to the conditions
	 */
	public void initMerchantGuide() throws FileNotFoundException {
		// Read input test file from resource folder
		ClassLoader classLoader = MerchantGuide.class.getClassLoader();
		File file = new File(classLoader.getResource("test-input.txt").getFile());
		try (Scanner scanner = new Scanner(file)) {
			Map<String, String> unitRomanLetterMap = new HashMap<>();
			Map<String, Double> metalValueMap = new HashMap<>();
			while (scanner.hasNextLine()) {
				// Read inputs line by line
				String line = scanner.nextLine();
				if ((line.contains(HOW_MUCH) || line.contains(HOW_MANY))
						&& line.endsWith(QUESTION_MARK)) {
					// Line with questions to be handled
					String answer = handleInputQuestions(line, unitRomanLetterMap, metalValueMap);
					logger.info(answer);
				} else if (line.endsWith(CREDITS) && line.contains(SPACE_WTIH_IS)) {
					// Line with credit calculation to be handled
					handleCreditCalculation(line, unitRomanLetterMap, metalValueMap);
				} else {
					// handle mapping for inter-galactic units and also process invalid queries
					handleUnitToRomanLetterMapping(line, unitRomanLetterMap);
				}
			}
		}
	}
	
	/*
	 * Method to handle input questions
	 */
	public String handleInputQuestions(String line, Map<String, String> unitRomanLetterMap, 
			Map<String, Double> metalValueMap) {
		StringBuilder romanLetter = new StringBuilder();
		String question = line.contains(HOW_MUCH) ? line.replace(HOW_MUCH, "").replace(QUESTION_MARK, "")
				: line.replace(HOW_MANY, "").replace(QUESTION_MARK, "");
		String metal = null;
		// iterate through the unit/metal names.
		// if the word is not present in unitRomanLetterMap its assumed to be metal name
		for (String word : question.split(EMPTY_SPACE)) {
			if (unitRomanLetterMap.containsKey(word)) {
				romanLetter.append(unitRomanLetterMap.get(word));
			} else if (metalValueMap.containsKey(word)) {
				metal = word;
			}
		}
		int units = convertRomanLetterToInt(romanLetter.toString());
		// If the metal value is null then just log the units value, else log the credits value
		if(metal == null) {
			return question+" is " + units;
		} else {
			return question+" is "+ (int)(metalValueMap.get(metal) * units) + CREDITS;
		}
	}
	
	/*
	 * Method to handle the credits calculation per unit
	 */
	public Map<String, Double> handleCreditCalculation(String line, Map<String, String> unitRomanLetterMap, 
			Map<String, Double> metalValueMap) {
		// derive total no of credits defined in the line
		int credits = Integer.parseInt(
				line.substring(line.lastIndexOf(SPACE_WTIH_IS) + 3, line.length()).trim().replace(CREDITS, ""));
		StringBuilder romanLetter = new StringBuilder();
		// Substring the line by the word 'is' and split them with empty space to iterate
 		for (String word : line.substring(0, line.indexOf(SPACE_WTIH_IS)).split(EMPTY_SPACE)) {
 			// Check if the 'word' is present in the unitRomanLetterMap.  
 			// If so append it to construct the roman letters
			if (unitRomanLetterMap.containsKey(word)) {
				romanLetter.append(unitRomanLetterMap.get(word));
			} else {
				// assign the value for metal 
				// get the int value after converting the roman letter
				double convertedUnit = convertRomanLetterToInt(romanLetter.toString());
				if(convertedUnit > 0) {
					// assign credits to metal per unit -key: metal name, -value: credit per unit
					metalValueMap.put(word, (credits / convertedUnit));
				}
				break;
			}
		}
 		return metalValueMap;
	}

	/*
	 * Method to assign unit mapping to roman letter
	 */
	public Map<String, String> handleUnitToRomanLetterMapping(String line, Map<String, String> unitRomanLetterMap) {
		// Last letter after the word ' is' is considered to be roman letter else log the message
		String lastLetter = line.substring(line.lastIndexOf(EMPTY_SPACE) + 1, line.length());
		if (line.contains(SPACE_WTIH_IS) && Arrays.stream(ROMAN_LETTER_ARRAY).anyMatch(lastLetter::equals)) {
			unitRomanLetterMap.put(line.substring(0, line.indexOf(" is")), lastLetter);
		} else {
			// Last letter is not present in the roman letter array and also the line doesn't satisfy other conditions
			logger.info("I have no idea what you are talking about");
		}
		return unitRomanLetterMap;
	}
	
	/*
	 *  Convert roman letter to int  
	 */
	public int convertRomanLetterToInt(String romanLetter) {
		/*
		 *  Validate roman letter pattern - we can either throw error or return 0 if the validation fails. 
		 *  Preferred the latter for now.
		 */
		if(!romanLetter.matches("^M{0,3}(CM|CD|D?C{0,3})(XC|XL|L?X{0,3})(IX|IV|V?I{0,3})$")) 
			return 0;
		int result = 0;
		for(int i = 0; i < ROMAN_VALUES.length; i++) {
			while (romanLetter.indexOf(ROMAN_LETTER_PATTERN[i]) == 0) {
				result += ROMAN_VALUES[i];
				romanLetter = romanLetter.substring(ROMAN_LETTER_PATTERN[i].length());
			}
		}
		return result;
	}
	
}
