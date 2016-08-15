import java.util.*;
public class Clause
{
	ArrayList<Literal> literals;
	
	
	public Clause()
	{
		this.literals = new ArrayList<Literal>();
	}
	
	
	void addLiteral(Literal literal)
	{
		literals.add(literal);
	}
	
	
	String printClause()
	{
		String clause = "[";
		boolean first = true;
		for(Literal l : literals)
		{
			if(first)
			{
				clause += l;
				first = false;
			}
			else
			{
				clause += " || "+l;
			}
		}
		return clause+"]";
	}


	public void addLiteral(int nextInt, boolean nextBoolean) {
		Literal literal = new Literal(nextInt, nextBoolean);
		literals.add(literal);
		
	}
}
