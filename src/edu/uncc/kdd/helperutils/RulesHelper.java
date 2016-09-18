package edu.uncc.kdd.helperutils;


public class RulesHelper {
	public String left, right, column;
	public RulesHelper(String _left, String _right, String _column)
	{
		left = _left;
		right = _right;
		column = _column;
	}
	public boolean equals(Object o)
	{
		RulesHelper r = (RulesHelper)o;
		if(this.column.equals(r.column))
			return true;
		return false;
	}
}
