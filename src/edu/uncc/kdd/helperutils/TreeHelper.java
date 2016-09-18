package edu.uncc.kdd.helperutils;

import java.util.ArrayList;


public class TreeHelper {
	
	NodeHelper treeNode = new NodeHelper();
	
	
	int findRules(ArrayList<RulesHelper> listOfRulesToBeFound)
	{
		NodeHelper startNode = treeNode;
		
		for(RulesHelper currRule : listOfRulesToBeFound)
		{
			int indexOfstartNode = -1;	
			for(int jx = 0; jx < startNode.children.size(); jx++)
			{
				// Can I do that (equal for datatype currRule)?
				//
				if(startNode.children.get(jx).data == currRule)
				{
					indexOfstartNode = jx;
					break;
				}
			}
			if(indexOfstartNode == -1)
			{
				// This should never happen
				//
				return -1;
				
				//Node tempNode = new Node();
				//tempNode.data = currRule;
				//tempNode.parent = startNode;
				//startNode.children.add(tempNode);
				//startNode = startNode.children.getLast();
				//startNode.isItNew = 1;
			}
			else
			{
				startNode = startNode.children.get(indexOfstartNode);
				//startNode.isItNew = 0;
			}
		}
		
		//return 0;
		return startNode.isItActionRule;
	}
	
	
	NodeHelper addRule(ArrayList<RulesHelper> ruleToBeAdded)
	{
		NodeHelper startNode = treeNode;
		
		for(RulesHelper currRule : ruleToBeAdded)	
		{
			int indexOfstartNode = -1;	
			for(int jx = 0; jx < startNode.children.size(); jx++)
			{
				// Can I do that (equal for datatype currRule)?
				//
				if(startNode.children.get(jx).data == currRule)
				{
					indexOfstartNode = jx;
					break;
				}
			}
			if(indexOfstartNode == -1)
			{
				NodeHelper tempNode = new NodeHelper();
				
				tempNode.data = currRule;
				tempNode.parent = startNode;
				
				startNode.children.add(tempNode);
				
				startNode = startNode.children.get(startNode.children.size()-1);
			
				startNode.isItNew = 1;
			}
			else
			{
				startNode = startNode.children.get(indexOfstartNode);
				startNode.isItNew = 0;
			}
		}
		return startNode;
	}

}
