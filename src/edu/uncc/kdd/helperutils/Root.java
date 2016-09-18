package edu.uncc.kdd.helperutils;

import java.util.ArrayList;

class Root 
	{
		public double confidence = -1;
		public int isItActionRule = 0;
		public int isItNew;
		public RuleClass data;
	    public Root parent;
	    public ArrayList<Root> children;
	        
	    Root()
	    {
	    	children = new ArrayList<Root>();
	    }
	}