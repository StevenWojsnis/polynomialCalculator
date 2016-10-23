/**
 * A class to be used as nodes in the polynomial DoublyLinkedLists.
 * Each of these nodes can be considered a "term" as they each represent a singular term within a
 * polynomial.
 * 
 * There are two values for each term: a coefficient, and exponent.
 * 
 * This class also contains getter and setter methods for the exponent and coefficient.
 * 
 * @author Steven Wojsnis
 *
 */
public class Terms {
	
	double coefficient;
	int exponent;
	
	public Terms(double c, int e){
		coefficient = c;
		exponent = e;
	}
	
	public double getCoefficient(){
		return coefficient;
	}
	
	public void setCoefficient(double c){
		coefficient = c;
	}
	
	public int getExponent(){
		return exponent;
	}
	
	public void setExponent(int e){
		exponent = e;
	}
}
