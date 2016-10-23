import java.util.Iterator;
import java.util.StringTokenizer;

/**
 * The Polynomial class primarily consists of four methods: add, subtract, multiply, and toString.
 * The constructor for the Polynomial class will take in three strings, which will be stored as the first
 * polynomial, second polynomial, and the operation. These first two strings are then passed through 
 * various methods (mainly from the PolynomialHelper class) to perform some action on them. This action is
 * in turn determined by the third string, operation.
 * 
 * 
 * @author Steven Wojsnis
 *
 */

public class Polynomial {
	
	//These strings are used to store the strings passed in through the constructor
	String firstPolynomialString, secondPolynomialString, operation;
	
	//DoublyLinkedLists to store the first and second polynomials, along with their unformatted and
	//formatted answers (based on the operation string)
	DoublyLinkedList<Terms> firstPolynomial = new DoublyLinkedList<Terms>();
	DoublyLinkedList<Terms> secondPolynomial = new DoublyLinkedList<Terms>();
	DoublyLinkedList<Terms> unformattedAnswerPolynomial = new DoublyLinkedList<Terms>();
	DoublyLinkedList<Terms> answerPolynomial = new DoublyLinkedList<Terms>();
	
	//Declarations for the number of terms in each polynomial, along with various flags that will be used
	//to determine if the order that the polynomials were passed in was switched, and that the polynomials
	//are valid
	int firstPolyTermCount, secondPolyTermCount;
	boolean didSwitch = false, validFirstPoly = true, validSecondPoly = true, fromSubtract = false, fromMultiply = false;
	
	//A helper class that contains several formatting and utility methods
	PolynomialHelper polyHelp = new PolynomialHelper();
	
	/**
	 * The constructor for the Polynomial class. It takes three String variables, the first two of which
	 * are turned into doublylinkedlists of term nodes, and the third is used to determine the operation
	 * to be performed on the two polynomials.
	 * 
	 * 
	 * @param fp : The first line in the three-line entry. Also the first polynomial
	 * @param sp : The second line in the three-line entry. Also the second polynomial
	 * @param op : The third line in the three-line entry. Also the operation
	 */
	public Polynomial(String fp, String sp, String op){
		firstPolynomialString = fp;
		secondPolynomialString = sp;
		operation = op;
		
		//Turns the firstPolynomialString and secondPolynomialString strings into linkedlists, counts
		//the number of terms in each, and prints them in the specified order (if valid polynomials and
		//operations were entered).
		validFirstPoly = polyHelp.parsePolynomialString(firstPolynomialString, firstPolynomial, true);
		validSecondPoly = polyHelp.parsePolynomialString(secondPolynomialString, secondPolynomial, false);
		firstPolyTermCount = polyHelp.termCount(firstPolynomial);
		secondPolyTermCount = polyHelp.termCount(secondPolynomial);
		polyHelp.simplify(firstPolynomial);
		polyHelp.simplify(secondPolynomial);
		if(validFirstPoly && validSecondPoly && polyHelp.validOperation(operation))
			polyHelp.printPolynomialsFormat(firstPolynomial, secondPolynomial, operation);
		
		//Determines if the first the second polynomials were switched
		didSwitch = polyHelp.largerPolyFirst(this);
		
		//Calls methods and uses flags to determine if valid polynomials/operations were passed in.
		//If not, an error message is printed and no answer is printed
		if(validFirstPoly && validSecondPoly && polyHelp.validOperation(operation))
			unformattedAnswerPolynomial = polyHelp.parseOperationString(operation, this);
		else if((!validFirstPoly || !validSecondPoly) && !polyHelp.validOperation(operation))
			System.out.println("This equation also contains an invalid operation. Please use 'add' 'subtract' or 'multiply'");
		else if((validFirstPoly && validSecondPoly) && !polyHelp.validOperation(operation))
			System.out.println("Invalid Operation. Please use 'add' 'subtract' or 'multiply'");
		
		//Another formatting method
		polyHelp.descendingExponentOrder(unformattedAnswerPolynomial, answerPolynomial);
	}
	
	//Various getter and setter methods for important data values in the Polynomial class
	
	public DoublyLinkedList<Terms> getFirstPolynomial(){
		return firstPolynomial;
	}
	public void setFirstPolynomial(DoublyLinkedList<Terms> poly){
		firstPolynomial = poly;
	}
	public DoublyLinkedList<Terms> getSecondPolynomial(){
		return secondPolynomial;
	}
	public void setSecondPolynomial(DoublyLinkedList<Terms> poly){
		secondPolynomial = poly;
	}
	public int getFirstPolyTermCount(){
		return firstPolyTermCount;
	}
	public void setFirstPolyTermCount(int x){
		firstPolyTermCount = x;
	}
	public int getSecondPolyTermCount(){
		return secondPolyTermCount;
	}
	public void setSecondPolyTermCount(int x){
		secondPolyTermCount = x;
	}

	/**
	 * The job of the toString method is to essentially convert the polynomial back into string form,
	 * and format it so that it prints an easier to read representation of the polynomial.
	 */
	public String toString(){
		
		String formattedPoly = "";
		
		//A flag to determine if we are processing the first term in the polynomial
		boolean first = true;
		
		//Adds each term to the string, and adds in an "x^" or "x" when appropriate.
		Iterator<Terms> polyIterator = answerPolynomial.iterator();
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
				if(term.getCoefficient() < 0 && !first)
					formattedPoly += (term.getCoefficient() * -1);
				else
					formattedPoly += term.getCoefficient();
				
				formattedPoly += "x^" + term.getExponent() +" ";
			}
			else if(term.getExponent()==1){
				if(term.getCoefficient() < 0 && !first)
					formattedPoly += (term.getCoefficient() * -1);
				else
					formattedPoly += term.getCoefficient();
				
				formattedPoly += "x" +" ";
			}
			else if(Math.abs(term.getExponent()) == 0){
				if(term.getCoefficient() < 0 && !first)
					formattedPoly += (term.getCoefficient() * -1);
				else
					formattedPoly += term.getCoefficient();
				
				formattedPoly += " ";
			}
			first = false;
		}
		
		return formattedPoly;
	}
	
	/**
	 * This method take two polynomials, in the form of DoublyLinkedLists, and adds them together.
	 * This is done by finding terms in both polynomials that have the same exponent, and adding their
	 * coefficients. The result of that sum then replaces the coefficient of that term in the first polynomial
	 * DoublyLinkedList, firstPoly, which effectively becomes are new "answer polynomial."
	 * If there's a term in the second polynomial (secondPoly) that doesn't share an exponent with another
	 * term in the first polynomial, then it is manually added onto the end of the first polynomial.
	 * 
	 * The altered first polynomial is then returned.
	 * 
	 * @param firstPoly : The DoublyLinkedList that represents the first polynomial
	 * @param secondPoly : The DoublyLinkedList that represents the second polynomial
	 * @return : A DoublyLinkedList containing the result of adding the two polynomials
	 */
	public DoublyLinkedList<Terms> add(DoublyLinkedList<Terms> firstPoly, DoublyLinkedList<Terms> secondPoly){
		boolean continueFlag = true;
		
		Iterator<Terms> firstPolyIterator = firstPoly.iterator();
		Iterator<Terms> secondPolyIterator = secondPoly.iterator();
		
		//These first two if statements check for an empty polynomial. If one is found, then the other is
		//returned.
		if(secondPolyTermCount == 0){
			return firstPoly;
		}
		
		else if(firstPolyTermCount == 0){
			return secondPoly;
		}
		
		else{
			//The first while loop iterates through the first polynomial
			while(firstPolyIterator.hasNext()){
				Terms firstPolyAdder = firstPolyIterator.next();
				secondPolyIterator = secondPoly.iterator();
				continueFlag = true;
				
				//The second while loop iterates through the second polynomial
				while(secondPolyIterator.hasNext() && continueFlag){
					Terms secondPolyAdder = secondPolyIterator.next();
					
					//Checks if the current exponent being checked in the first polynomial matches with
					//any exponents in the second polynomial. If so, their coefficients are added, and
					//the term is removed from the second polynomial.
					if(secondPolyAdder.getExponent() == firstPolyAdder.getExponent()){
						firstPolyAdder.setCoefficient(firstPolyAdder.getCoefficient() + secondPolyAdder.getCoefficient());
						secondPolyIterator.remove();
						
						//Note the use of the cotinueFlag allows for a quicker adding process.
						//Since these polynomials are already simplified, once a term with a like exponent
						//is found in the second polynomial, we can break out of the second while loop
						continueFlag = false;
					}
				}
			}
			
			//If there was a term in the second polynomial with a unique exponent, it is manually added
			//to the first polynomial, which is returned.
			secondPolyIterator = secondPoly.iterator();
			while(secondPolyIterator.hasNext()){
				firstPoly.add(secondPolyIterator.next());
			}
			return firstPoly;
		}
	}
	
	/**
	 * This method subtracts two polynomials, in the forms of DoublyLinkedLists, and returns an answer
	 * also in the form of a DoublyLinkedList.
	 * 
	 * If the first polynomial and second polynomial were switched for the sake of a shorter runtime (if
	 * the second polynomial is larger than the first, they are switched so that there aren't an excess
	 * of loops in the add method) then this negates every term in the "firstPoly" DoublyLinkedList,
	 * otherwise it negates every term in the "secondPoly" DoublyLinkedList.
	 * 
	 * Finally, it returns the result of the add method of these two DoublyLinkedLists
	 * 
	 * @param firstPoly : The DoublyLinkedList that represents the first polynomial
	 * @param secondPoly : The DoublyLinkedList that represents the second polynomial
	 * @return : A DoublyLinkedList containing the result of subtracting the two polynomials
	 */
	public DoublyLinkedList<Terms> subtract(DoublyLinkedList<Terms> firstPoly, DoublyLinkedList<Terms> secondPoly){
		
		//If the polynomial DoublyLinkedLists were not switched, the second DoublyLinkedList, secondPoly,
		//is negated by setting each term's coefficient to itself * -1.
		if(!didSwitch){
			Iterator<Terms> secondPolyIterator = secondPoly.iterator();
			while(secondPolyIterator.hasNext()){
				Terms tempTerm = secondPolyIterator.next();
				double tempCoefficient = tempTerm.getCoefficient();
				tempTerm.setCoefficient(tempCoefficient*-1);
			}
		}
		
		//Otherwise, if they were switched, the firstPoly DoublyLinkedList is negated in the same manner.
		else{
			Iterator<Terms> firstPolyIterator = firstPoly.iterator();
			while(firstPolyIterator.hasNext()){
				Terms tempTerm = firstPolyIterator.next();
				double tempCoefficient = tempTerm.getCoefficient();
				tempTerm.setCoefficient(tempCoefficient*-1);
			}
		}
		
		//Adds and returns the sum of the two polynomials (one of which is now negated). Effectively 
		//subtracting them.
		return add(firstPoly, secondPoly);
	}
	
	/**
	 * This method multiples two polynomials in the form of DoublyLinkedLists, and returns their product
	 * in DoublyLinkedList form.
	 * 
	 * This method works by creating two new DoublyLinkedLists, one of which, tempPoly, holds the product of
	 * one term in firstPoly with every term in secondPoly, and the other, newPoly, progressively adds
	 * itself with the tempPoly. This process is repeated until the end of firstPoly is reached.
	 * 
	 * Essentially this is an addition of several products, yielding a full product of the two polynomials.
	 * 
	 * @param firstPoly : The DoublyLinkedList that represents the first polynomial
	 * @param secondPoly : The DoublyLinkedList that represents the second polynomial
	 * @return : A DoublyLinkedList containing the result of multiplying the two polynomials
	 */
	public DoublyLinkedList<Terms> multiply(DoublyLinkedList<Terms> firstPoly, DoublyLinkedList<Terms> secondPoly){
		
		Iterator<Terms>firstPolyIterator = firstPoly.iterator();
		Iterator<Terms>secondPolyIterator = secondPoly.iterator();
		
		//Two new DoublyLinkedLists, tempPoly holds the product of one term in firstPoly with each term in
		//secondPoly, and newPoly holds the sum of tempPolys.
		DoublyLinkedList<Terms> tempPoly = new DoublyLinkedList<>();
		DoublyLinkedList<Terms> newPoly = new DoublyLinkedList<>();
		
		//The first while loop iterates through the first polynomial.
		while(firstPolyIterator.hasNext()){
			Terms firstPolyTerm = firstPolyIterator.next();
			secondPolyIterator = secondPoly.iterator();	
			
			//The second while loop iterates through the second polynomial.
			while(secondPolyIterator.hasNext()){
				Terms secondPolyTerm = secondPolyIterator.next();
				
				//Declares new variables of the product of coefficients and sum of exponents.
				double coefficient = firstPolyTerm.getCoefficient() * secondPolyTerm.getCoefficient();
				int exponent = firstPolyTerm.getExponent() + secondPolyTerm.getExponent();
				
				//Uses the new variables to make a new term node and add it to tempPoly.
				tempPoly.add(new Terms(coefficient, exponent));
			}
				//newPoly adds itself with tempPoly, and stores that value in newPoly (itself).
				newPoly = add(newPoly, tempPoly);

			//tempPoly is cleared so the next term in firstPoly can be multiplied with each term in
			//secondPoly and be stored in an empty, or "fresh" tempPoly.
			tempPoly.clear();
		}
		//newPoly, basically a sum of tempPolys, is returned.
		return newPoly;
	}
}
