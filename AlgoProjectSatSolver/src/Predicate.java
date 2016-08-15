import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;


public class Predicate{
	
	// We will store the value of literals in this structure as we go along.
	static HashMap<Literal,Boolean> literalMap = new HashMap<Literal,Boolean>();
	
	 
	//For counting backtracks
	static int backTrackCounts = 0;


	private static Scanner scan;
	public static void main(String args[]) throws IOException
	{
		ArrayList<Clause> Clauses = new ArrayList<Clause>();
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		Random random = new Random();
		
		
		// Creating data structure.
		System.out.println("How many clauses does your formula in the Conjuctive Normal Form have?");
		int nClauses = Integer.parseInt(in.readLine());
		System.out.println("How many variables can your predicate have?");
		int nvariables = Integer.parseInt(in.readLine());
		System.out.println("How many Literals can your clauses have?");
		int nliterals = Integer.parseInt(in.readLine());
		
		
		int[] variables = new int[nvariables];
		for(int z = 0; z< nvariables;z++){
			variables[z] = random.nextInt(2*nvariables)+ 1; // (Randomly generated variables from range 1 to 2
		}
		System.out.println("The variables are "+Arrays.toString(variables));
		
		for(int i=0;i<nClauses;i++)
		{
			
			Clause clause = new Clause();
			for(int i1=0;i1<nliterals;i1++)
			{
				int select = random.nextInt(variables.length);
				clause.addLiteral(variables[select], random.nextBoolean());
				
			}
			Clauses.add(clause);
		}
		
		System.out.print("your predicate is ");
		printClauses(Clauses);
		// End of data structure
		if(DLL(Clauses))
		{
			System.out.println("Result: The formula is satisfiable.");
			System.out.println("Number of bactracks -> " + backTrackCounts);
			System.out.println("The values assigned to literals are as follows " +literalMap.toString());
			//System.out.println(st.toString());
		}
		else
		{
			System.out.println("Result: The formula is  not satisfiable.");
			System.out.println("Number of bactracks -> " + backTrackCounts);
		}
		
	}
	/*  Returns first literal found.
	 * (To be improved by choosing literal with most ocurrences
	 * wich will allow to remove more clauses at once.)
	 */
	static Literal pickLiteral(ArrayList<Clause> Clauses)
	{
		for(Clause c: Clauses)
		{
			return c.literals.get(0);
		}
		return null;
	}
	/*	Checks if the formula has any empty clauses. 
	 *  It should not happen. If a clause with one literal is deminished/cut
	 *  then the formula is not satisfactable.
	 */
	static boolean hasEmptyClause(ArrayList<Clause> Clauses)
	{
		for(Clause c: Clauses)
		{
			if(c.literals.size() == 0)
			{
				return true;
			}
		}
		return false;
	}
	// This is a simple function to print a formula in a readable fashion.
	static void printClauses(ArrayList<Clause> Clauses)
	{
		String formula = "{";
		boolean first = true;
		if(Clauses.size() == 0)formula = "{EMPTY}";
		else
		{
			for(Clause c: Clauses)
			{
				if(first)
				{
					formula += c.printClause();
					first = false;
				}
				else formula += " && "+c.printClause();
			}
			formula += "}";
		}
		System.out.println(formula);
	}
	/*	This is the main algorithm.
	 * 	It receives a formula and processes it until 
	 * 	it can return true or false. 
	 */
	static boolean DLL(ArrayList<Clause> Clauses)
	{
		//Unitary Propagation
		while(true)
		{	
			Literal literalToRemove = searchSingleLiteral(Clauses);
			if(!(literalToRemove.getmValue() == -1))
			{
				System.out.println("Performing unitary propagation with: "+literalToRemove);
				removeClauses(literalToRemove,Clauses);
				cutClauses(literalToRemove,Clauses);
				printClauses(Clauses);
				if(Clauses.size() == 0) 
				{
					System.out.println("All clauses removed. Returning true.");
					return true;
				}
				if(hasFalsehood(Clauses)) 
				{
					System.out.println("Falsehood detected. Returning false.");
					System.out.println("BackTracking to previous node.");
					backTrackCounts++;
					//st.pop();
					return false;
					
				}
				else if(hasEmptyClause(Clauses))
				{
					System.out.println("Empty clause detected. Returning false.");
					return false;
					
				}
			}
			else
			{
				System.out.println("No single literals.");
				System.out.println("Cannot perform unitary propagation.");
				break;
			}
		}
		ArrayList<Clause> copy1 = new ArrayList<Clause>();
		ArrayList<Clause> copy2 = new ArrayList<Clause>();
		for(Clause c: Clauses)
		{
			Clause c2 = new Clause();
			for(Literal s: c.literals)
			{
				c2.addLiteral(s);
			}
			copy1.add(c2);
		}
		for(Clause c: Clauses)
		{
			Clause c2 = new Clause();
			for(Literal s: c.literals)
			{
				c2.addLiteral(s);
			}
			copy2.add(c2);
		}
		Clause clause1 = new Clause();
		Clause clause2 = new Clause();
		Literal l1 = pickLiteral(Clauses);
		Literal l2 ;
		
		if(l1.ismIsPositive() == true) l2 = l1;
		
		else l2 = l1.negation();
		clause1.addLiteral(l1);
		clause2.addLiteral(l2);
		copy1.add(clause1);
		copy2.add(clause2);
		
		//Moment of the truth
		System.out.println("Adding clause: ["+l1+"]");
		if(DLL(copy1) == true)
		{
			return true;
		}
		else
		{
			System.out.println("Trying opposite clause: ["+l2+"]");
			return DLL(copy2);
		}
	}
	/* This function gathers all single literals and then searches all
	 * the clauses for single opposites. If one is found, then the whole
	 * formula must be false. (True is returned).
	 */
	static boolean hasFalsehood(ArrayList<Clause> Clauses)
	{
		ArrayList<Literal> singleLiterals = new ArrayList<Literal>();
		for(Clause c: Clauses)
		{
			if(c.literals.size() == 1)
			{
				singleLiterals.add(c.literals.get(0));
			}
		}
		for(Literal sl : singleLiterals)
		{
			Literal sl_opposite;
			if(sl.ismIsPositive() == true) sl_opposite = sl;
			else sl_opposite = sl.negation();
			for(Clause c: Clauses)
			{
				if(c.literals.size() == 1)
				{
					if(c.literals.get(0).equals(sl_opposite))
					{
						return true;
					}
				}
			}
		}
		return false;
	}
	/*	This function takes in a literal and removes -literal
	 * 	from all clauses that have it.
	 * 	It should be noted that if a clause is left empty by this function
	 * 	then the formula is not satisfiable.
	 */
	static void cutClauses(Literal literalToRemove,ArrayList<Clause> Clauses)
	{
		Literal cutLiteral;
		if(literalToRemove.ismIsPositive() == true) cutLiteral = literalToRemove.negation();
		else cutLiteral = literalToRemove;
		//System.out.println("Cut literal is "+cutLiteral);
		for(Clause c: Clauses)
			
		{
			c.literals.remove(cutLiteral);
		}
	}
	/*	This function takes in a literal and removes all 
	 * 	clauses where it occurs.  
	 */
	static void removeClauses(Literal literalToRemove,ArrayList<Clause> Clauses)
	{
		ArrayList<Clause> clausesToRemove = new ArrayList<Clause>();
		for(Clause c: Clauses)
		{
			for(Literal l: c.literals)
			{
				if(l.equals(literalToRemove))  // write .equals
				{
					clausesToRemove.add(c);
				}	
			}
		}
		for(Clause c : clausesToRemove)
		{
			Clauses.remove(c);
		}	
	}
	/*	This function finds a single literal.
	 * (To be optimized by finding the single literal with most occurrences).	 *
	 */
	static Literal searchSingleLiteral(ArrayList<Clause> Clauses)
	{
		
		Literal literalR = new Literal(-1, true);
		//String literalToRemove = literalR.toString();
		for(Clause c: Clauses)
		{
			if(c.literals.size() == 1)
			{
				literalR = c.literals.get(0);
				if(literalR.ismIsPositive() == false)
				{
						literalMap.put(literalR,false);
				}
				else
				{
						literalMap.put(literalR,true);
				}
				break;
			}
		}
		
		return literalR;
	}
}
