package edu.uncc.kdd.helperutils;

public class RuleClass 
{
	public String left, right, column;
	public RuleClass(String _left, String _right, String _column)
	{
		left = _left;
		right = _right;
		column = _column;
	}
	public boolean equals(Object o)
	{
		RuleClass r = (RuleClass)o;
		if(this.column.equals(r.column))
			return true;
		return false;
	}
}
