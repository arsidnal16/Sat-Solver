/**
 * Convinience object for returning a variable together with a value
 */
class Literal
{
	  private final int mValue;            // The variable in question
	    private final boolean mIsPositive; // Whether this is X (true) or ~X (false)

	    /**
	     * Constructs a new literal from the specified value and sign.  Null
	     * literals are not supported.
	     *
	     * @param value The value representing the literal.
	     * @param isPositive Whether the value is positive or negative.
	     */
	    public Literal(int value, boolean isPositive) {
	        /* Check that the value is indeed non-null. */
	        mValue = value;
	        mIsPositive = isPositive;
	    }

    
    
    public int getmValue() {
			return mValue;
		}



		public boolean ismIsPositive() {
			return mIsPositive;
		}



	public Literal negation() {
        return new Literal(getmValue(), !isPositive());
    }

    @Override
	public String toString() {
		return  ""+ mValue ;
	}



	/**
     * Returns the object used to represent the literal in this clause.
     *
     * @return The object used to represent the literal in this clause.
     */
   
	private boolean isPositive() {
		// TODO Auto-generated method stub
		return mIsPositive;
	}

	
	@Override
    public boolean equals(Object obj) {
        /* Confirm that the other object has the proper type. */
        if (!(obj instanceof Literal))
            return false;

        /* Downcast, then do a field-by-field comparison. */
        Literal realObj = (Literal) obj;

        return realObj.isPositive() == isPositive() 
        		&& realObj.mValue == mValue;
    }
    
	
	 @Override
	    public int hashCode() {
	        return (isPositive() ? 1 : 31)*mValue;
	    }

}
