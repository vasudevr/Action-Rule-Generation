package edu.uncc.kdd.helperutils;

import java.util.ArrayList;


public class NodeHelper {

	public double confidence = -1;
	public int isItActionRule = 0;
	public int isItNew;
	public RulesHelper data;
    public NodeHelper parent;
    public ArrayList<NodeHelper> children;
        
    NodeHelper()
    {
    	children = new ArrayList<NodeHelper>();
    }

}
