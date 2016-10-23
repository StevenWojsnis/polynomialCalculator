import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * The driver class for the process of performing an add, subtract or multiply operation on two polynomials.
 * This class reads in lines from a text file, stores them as String values, and then passes them through
 * to the Polynomial class to have an operation performed on them.
 * 
 * @author Steven Wojsnis
 * Polynomial Operations Project
 * 		CS313, Dr.Svitak
 */
public class Project1 {
	public static void main(String[] args){
		boolean skip = false;
		//Accepts only a text file named "project1.txt"
		try (BufferedReader br = new BufferedReader(new FileReader("project1.txt")))
		{
			String line = br.readLine();
			//While loop runs for as long their is a line with characters on it
			while(line != null){
				skip = false;
				
				//The first line in the three-line sequence is stored as firstPoly
				String firstPoly = line;
				
				//The second line is stored as secondPoly
				String secondPoly = br.readLine();
				
				//The third line is stored as op
				String op = br.readLine();
				
				//If any of the inputs were null, throw an exception.
				if(firstPoly == null || secondPoly == null || op == null){
					skip = true;
					System.out.println("Each operation requires three lines of input.");
				}
					
				line = br.readLine();
				
				if(!skip){
					//Creates an instance of the Polynomial class, with the firstPoly, secondPoly and op values
					Polynomial poly = new Polynomial(firstPoly,secondPoly,op);
				
					//Prints the polynomials in the correct format, as well as the answer.
					System.out.println(poly.toString() + "\n");
				}
			}
		}
		//If a file with an invalid name is used, an error message is shown and the program ends.
		catch(IOException e){
			System.out.println("Please use a text filed named 'project1.txt'");
		}
	}
}
