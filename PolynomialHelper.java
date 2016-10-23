import java.util.Iterator;
import java.util.StringTokenizer;


/**
 * The PolynomialHelper class contains several formatting and utility methods that are used to help process
 * the original string, turn it into a DoublyLinkedList.
 * 
 * A few of these methods are not crucial to the technical computation of the polynomials, but rather act
 * as a way to make the polynomials more readable. An example of this would be the simplify method, which
 * combines terms with the same exponent within a polynomial.
 * @author Steven Wojsnis
 *
 */
public class PolynomialHelper {
	
	public PolynomialHelper(){

	}
	
	/**
	 * This method is a way of turning an input string into a DoublyLinkedList. Tokenizer is used
	 * to create coefficient and exponent data values, which are in turn used to create "Terms" nodes
	 * and are added to a DoublyLinkedList.
	 * 
	 * Note that while terms are added to "tempPolynomial" the purpose of this is to be used in the
	 * descendingExponentOrder method, to properly format the polynomial. The DoublyLinkedList "polynomial"
	 * actually contains the correctly formatted version of the polynomial linked list, and is used in
	 * future calculations.
	 * 
	 * Takes parameter isFirst to determine which error message should be printed. If the first polynomial
	 * is being processed, isFirst is true. If the second polynomial is being processed, isFirst is false.
	 * 
	 * @param line : The input string, containing numbers separated by spaces, representing a polynomial.
	 * @param polynomial : The DoublyLinkedList that will contain nodes corresponding to the original input line.
	 * @param isFirst: boolean used to determine whether first or second polynomial is being processed.
	 * @return : boolean. If the string was successfully processed, return true. Otherwise, return false.
	 * @throws IllegalArgumentException : Catches any input errors in the string.
	 */
	public boolean parsePolynomialString(String line, DoublyLinkedList<Terms> polynomial, boolean isFirst) throws IllegalArgumentException{
		StringTokenizer token = new StringTokenizer(line, " ");
		DoublyLinkedList<Terms> tempPolynomial = new DoublyLinkedList<Terms>();
		
		//Try block around the reading-in process in case of input error
		try{
			//Checks if there's an even amount of numbers in input line. This ensures that there is an
			//exponent and coefficient for each term in the polynomial.
			if(token.countTokens()%2 == 0){
				while(token.hasMoreTokens()){
					
					//Parses the first number in each two-number pair as a double variable, coefficient
					double coefficient = Double.parseDouble(token.nextToken());
					
					//Parses the second number in each two-number pair as an int variable, exponent
					int exponent = Integer.parseInt(token.nextToken());
					
					//Creates a new term with the recently attained coefficient and exponent
					Terms term = new Terms(coefficient,exponent);
					
					//Adds all the terms to a temporary polynomial, creating an unordered DoublyLinkedList
					tempPolynomial.add(term);
										
				}
				//Takes the terms in tempPolynomial, and stores them in polynomial in descending exponent order.
				descendingExponentOrder(tempPolynomial, polynomial);
				
				//returns true to indicate that the transition between string and LinkedList was successful
				return true;
			}
			else{
				//If there isn't an even amount of numbers, this means an exponent or coefficient is missing
				//and an error is thrown.
				throw new IllegalArgumentException();
			}
		}
		catch(IllegalArgumentException e){
			//Prints out an error message, and returns false, used to indicate to the program to stop
			//processing this polynomial operation, and move to the next one.
			if(isFirst)
				System.out.println("Invalid first polynomial. Every term needs a coefficient and an integer exponent.");
			else
				System.out.println("Invalid second polynomial. Every term needs a coefficient and an integer exponent.");
			return false;
		}
	}
	
	/**
	 * This method takes the terms of one DoublyLinkedList, and inserts them into another DoublyLinkedList
	 * in descending exponent order.
	 * 
	 * Also systematically removes terms from tempPoly, as they get moved over to poly, effectively 
	 * emptying tempPoly.
	 * 
	 * @param tempPoly : The DoublyLinkedList from which terms will be removed and ordered.
	 * @param poly : The DoublyLinkedList which will receive terms from tempPoly, and hold them in descending order.
	 */
	public void descendingExponentOrder(DoublyLinkedList<Terms> tempPoly, DoublyLinkedList<Terms> poly){
		Iterator<Terms> it = tempPoly.iterator();
		
		//index keeps track of how far along the linkedlist the program is, pos holds the index of the highest exponent
		int index = 0, pos = 0;
		while(it.hasNext()){
			
			//The term with the biggest exponent is initially set to the first term.
			Terms biggestTerm = it.next();

			while(it.hasNext()){
				index++;
				Terms nextTerm = it.next();
				
				//If a larger exponent is found, it becomes the biggestTerm, and its position is recorded
				if (biggestTerm.getExponent() < nextTerm.getExponent()){
					biggestTerm = nextTerm;
					pos = index;
				}
			}
			
			//The biggest term is added to the poly DoublyLinkedList.
			poly.add(biggestTerm);
			
			//The term recently added to poly (the term with the largest exponent) is removed.
			tempPoly.remove(pos);
			
			//Values and iterator are reset to find the next term with largest exponent.
			index=pos=0;
			it = tempPoly.iterator();
		}
		
	}
	
	/**
	 * This method checks to make sure that the input operation string is one of the accepted operations.
	 * If the string is an accepted operation, the method returns true, otherwise, false is returned.
	 * 
	 * @param op : The string containing the desired operation.
	 * @return : boolean value
	 */
	public boolean validOperation(String op){
		//Check to see if the input operation is "add","subtract", or "multiply". Space and case is ignored.
		if(op.replace(" ", "").toLowerCase().equals("add") || op.replace(" ", "").toLowerCase().equals("subtract")
				|| op.replace(" ", "").toLowerCase().equals("multiply"))
			return true;
		else 
			return false;	
	}
	
	/**
	 * Returns the number of terms in a polynomial (the number of nodes in the DoublyLinkedList).
	 * 
	 * @param polynomial : The DoublyLinkedList who's terms (nodes) will be counted
	 * @return : The number of terms (nodes) in the DoublyLinkedList
	 */
	public int termCount(DoublyLinkedList<Terms> polynomial){
		int count = 0;
		Iterator<Terms> polyIt = polynomial.iterator();
		
		//Iterates throw the polynomial, increments count for each loop.
		while(polyIt.hasNext()){
			count++;
			polyIt.next();
		}
		
		return count;
	}

	/**
	 * This method reads the operation and, based on the input operation, calls the appropriate operation
	 * method within the polynomial class.
	 * 
	 * If the input operation doesn't match any of the accepted operations, no operation methods are called
	 * and an error message is displayed. This is similar to the validOperation method, but the difference
	 * between these two methods is that validOperation is used gate to determine whether or not the
	 * input terms should be printed (if the operation is invalid, the terms shouldn't be printed). This method
	 * actually implements the operation.
	 * 
	 * Returns an empty DoublyLinkedList if operation is invalid.
	 * 
	 * @param op : The input operation string.
	 * @param polynomials : An instance of the Polynomial class (where this method would normally have been called from).
	 * @return : Returns a DoublyLinkedList containing the result of the desired operation.
	 * @throws IllegalArgumentException : Error message is displayed if invalid operation input.
	 */
	public DoublyLinkedList<Terms> parseOperationString(String op, Polynomial polynomials)throws IllegalArgumentException{
		//Attempts to match the input operation with one of the existing operation methods
		try{
			//The operation methods are called with the first and second polynomials from the Polynomial class
			//instance as parameters.
			
			if (op.replace(" ", "").toLowerCase().equals("add")){
				return polynomials.add(polynomials.getFirstPolynomial(), polynomials.getSecondPolynomial());
			}
			else if (op.replace(" ", "").toLowerCase().equals("subtract")){
				return polynomials.subtract(polynomials.getFirstPolynomial(), polynomials.getSecondPolynomial());
			}
			else if  (op.replace(" ", "").toLowerCase().equals("multiply")){
				return polynomials.multiply(polynomials.getFirstPolynomial(), polynomials.getSecondPolynomial());
			}
			
			//If no matches are made
			else{
				throw new IllegalArgumentException();
			}
		}
		
		catch(IllegalArgumentException e){
			//Prints an error message and returns an empty DoublyLinkedList.
			System.out.println("Invalid operation type");
			return new DoublyLinkedList<Terms>();
		}
	}
	
	/**
	 * Combines terms that have the same exponent within a polynomial. For example, if there exist two
	 * terms in a polynomial with an exponent of 2, their coefficients are added, and set to be one of 
	 * the term's coefficient. The other term is removed.
	 * 
	 * @param polynomial : The DoublyLinkedList which will be checked for duplicate exponent terms.
	 */
	public void simplify(DoublyLinkedList<Terms> polynomial){
		Iterator<Terms> it = polynomial.iterator();
		Iterator<Terms> it2 = polynomial.iterator();
		//While loop runs through first iterator
		while(it.hasNext()){
			Terms testTerm = it.next();
			
			//The second iterator is reinitialized, to make sure it hits every term.
			it2 = polynomial.iterator();
			
			//While loop runs through second iterator, to compare every value in the second iterator with
			//the current one in the first iterator
			while(it2.hasNext()){
				Terms testAgainstTerm = it2.next();
				
				//If two terms have the same exponent, combine their coefficients and remove the duplicate.
				//Note that the if statement checks if the terms that are being compared are the same term nodes,
				//this is to prevent an error where terms are being combined with themselves.
				if (testAgainstTerm.getExponent() == testTerm.getExponent() && testTerm != testAgainstTerm){
					testTerm.setCoefficient(testAgainstTerm.getCoefficient() + testTerm.getCoefficient());
					it2.remove();
					
					//The first iterator is reset, to reset the expectedModCount.
					it = polynomial.iterator();
				
				}
			}
		}
	}
	
	/**
	 * Checks to see if the second polynomial DoublyLinkedList has more terms than the first. If so, 
	 * it sets the first polynomial to the second, and the second to the first.
	 * 
	 * This is done to expedite the adding process. Starting with the polynomial with the most terms
	 * limits the amount of loops performed during the operation methods.
	 * 
	 * If a switch is performed, true is returned to indicate the switch, otherwise false.
	 * 
	 * @param polynomials : Instance of the Polynomial class
	 * @return : boolean value
	 */
	public boolean largerPolyFirst(Polynomial polynomials){
		
		//If the firstPolyTermCount from the Polynomial class is less than the secondPolyTermCount,
		//firstPolynomial is set equal to secondPolynomial, and vice versa, with the help of a temporary
		//DoublyLinkedList, to store the value of firstPolynomial after it is overwritten.
		if(polynomials.firstPolyTermCount < polynomials.secondPolyTermCount){
			DoublyLinkedList<Terms> tempPoly = polynomials.getFirstPolynomial();
			int tempCount = polynomials.getFirstPolyTermCount();
			polynomials.setFirstPolynomial(polynomials.getSecondPolynomial());
			polynomials.setSecondPolynomial(tempPoly); 
			polynomials.setFirstPolyTermCount(polynomials.getSecondPolyTermCount());
			polynomials.setSecondPolyTermCount(tempCount);
			
			//Returns true if a switch was performed
			return true;
		}
		//Returns false if a switch was not performed
		return false;
	}
	
	/**
	 * Prints the operation process in the specified format. Four lines are printed, the first polynomial,
	 * followed by the symbol of the operation, followed by the second polynomial, and finally, an equals sign.
	 * 
	 * @param firstPoly : The first polynomial DoublyLinkedList.
	 * @param secondPoly : The second polynomial DoublyLinkedList.
	 * @param op : the input operation string.
	 */
	public void printPolynomialsFormat(DoublyLinkedList<Terms> firstPoly, DoublyLinkedList<Terms> secondPoly, String op){
		String opSign = "";
		
		//Determines what sign to use for the operation, based on the input string.
		if(op.replace(" ", "").toLowerCase().equals("add")) opSign = "+";
		else if(op.replace(" ", "").toLowerCase().equals("subtract")) opSign = "-";
		else if(op.replace(" ", "").toLowerCase().equals("multiply")) opSign = "*";
		
		//Prints the formatted firstPoly with the correct operation symbol, formatted secondPoly, and equals sign.
		System.out.println(format(firstPoly) + "\n" + opSign + "\n" + format(secondPoly) + "\n" + "=");
	}
	
	/**
	 * This is similar to the toString method in Polynomial. The difference between these two methods is
	 * toString needs no input, it simply formats the operation result polynomial as a string. This method
	 * accepts a parameter, and therefore, you can format a polynomial that isn't the operation result polynomial.
	 * 
	 * @param poly : The polynomial DoublyLinkedList that is to be formatted.
	 * @return : A formatted string version of the polynomial DoublyLinkedList.
	 */
	private String format(DoublyLinkedList<Terms> poly){
		String formattedPoly = "";
		
		//A flag to determine if we are processing the first term in the polynomial
		boolean first = true;
		
		//Adds each term to the string, and adds in an "x^" or "x" when appropriate.
		Iterator<Terms> polyIterator = poly.iterator();
		while(polyIterator.hasNext()){
			Terms term = polyIterator.next();
			if(term.getCoefficient() >= 0 && !first){
				formattedPoly += "+ ";
			}
			else if(term.getCoefficient() < 0 && !first){
				formattedPoly += "- ";
			}
			
			//Note that each of these conditions also checks for a negative coefficient.
			//If one is found, it is made positive by multiplying by -1, so that it can be printed
			//in the format of "- k" for some coefficient k, rather than "-k"
			
			//We use the absolute values of the exponents, in case of a negative exponent
			if(Math.abs(term.getExponent()) >1 || term.getExponent() == -1){
				if(term.getCoefficient() < 0  && !first)
					formattedPoly += (term.getCoefficient() * -1);
				else
					formattedPoly += term.getCoefficient();
				
				formattedPoly += "x^" + term.getExponent() +" ";
			}
			else if(Math.abs(term.getExponent())==1){
				if(term.getCoefficient() < 0  && !first)
					formattedPoly += (term.getCoefficient() * -1);
				else
					formattedPoly += term.getCoefficient();
				
				formattedPoly += "x" +" ";
			}
			else if(term.getExponent() == 0){
				if(term.getCoefficient() < 0  && !first)
					formattedPoly += (term.getCoefficient() * -1);
				else
					formattedPoly += term.getCoefficient();
				
				formattedPoly += " ";
			}
			
			first = false;
		}
		
		return formattedPoly;
	}
}
