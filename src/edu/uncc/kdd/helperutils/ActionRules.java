package edu.uncc.kdd.helperutils;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ActionRules
{

	private static final Log LOG = LogFactory.getLog(ActionRules.class);
	private HashMap<String, HashMap<String, ArrayList<Integer>>> mapStable, mapFlexible,map;
	private ArrayList<Integer> from, to;

	static ArrayList<RuleClass> atomicFrequentItems = new ArrayList<RuleClass>();
	static MapTree mainTrie = new MapTree();

	public String kdd_lers_getFrequent(HashMap<String, HashMap<String,HashMap<String, ArrayList<Integer>>>> varNameTypeContentMap,
			int sup, int confidence, String ff, String tt, ArrayList<Integer> decisionFromList, ArrayList<Integer> decisionToList, StringBuilder listModelResult, boolean flag) throws IOException
	{

		connectorFunction(varNameTypeContentMap, decisionFromList, decisionToList);
		
		LOG.info("from"+ff+"to"+tt);

		int isThereAtLeastOneActionRule = 0;

		int support, supstable, supflex;
		int supportold, old_supstable, old_supflex;

		ArrayList<ArrayList<RuleClass>> frequentItems = new ArrayList<ArrayList<RuleClass>>();
		ArrayList<RuleClass> items = new ArrayList<RuleClass>();

		LOG.info("map-stable attributes");
		
		for(String atts: mapStable.keySet())
		{
			for(String level: mapStable.get(atts).keySet())
			{
				ArrayList<Integer> l = new ArrayList<Integer>();
				ArrayList<Integer> levelList = new ArrayList<Integer>();

				levelList = mapStable.get(atts).get(level);

				l = compare(levelList,to);
				if(flag)
				{
					old_supstable = compare(levelList, from).size();
					supstable=l.size();
				}
				else
				{
					old_supstable = Math.min(l.size(),compare(levelList, to).size());
				}

					ArrayList<RuleClass> frequent = new ArrayList<RuleClass>();
					RuleClass rule = new RuleClass(level,level,atts);
					frequent.add(rule);
					items.add(rule);
					frequentItems.add(frequent);

					double old_confstable= 100 * ((double)compare(levelList, to).size()/(double)levelList.size())*((double)compare(levelList, from).size()/(double)levelList.size());
					double confstable= 100 *(double)compare(levelList, to).size()/(double)levelList.size();


					Root lastNodeAdded = new Root();
					lastNodeAdded = mainTrie.addRule(frequent);

						isThereAtLeastOneActionRule = 1;
						lastNodeAdded.isItActionRule = 1;

						if(!rule.left.equals(rule.right)){
							//remove util if it dont make sense
						listModelResult.append(rule.column +"("+rule.left+"->"+rule.right+") => ("+ff+"->"+tt +")"+","+ "support = "+ old_supstable+","+ "confidence="+ old_confstable+"\n");
						}


			}
		}
		for(String atts: mapFlexible.keySet())
		{
			for(String levelLeft: mapFlexible.get(atts).keySet())
			{
				for(String levelRight: mapFlexible.get(atts).keySet())
				{
					ArrayList<Integer> l= new ArrayList<Integer>();
					ArrayList<Integer> levelListLeft = mapFlexible.get(atts).get(levelLeft);
					ArrayList<Integer> levelListRight = mapFlexible.get(atts).get(levelRight);

					l = compare(levelListRight, to);

					if(flag)
					{
						old_supflex = compare(levelListLeft,from).size();
						supflex=l.size();
					}
					else
					{
						old_supflex = Math.min(compare(levelListLeft,from).size(), compare(levelListRight, to).size());
					}

						ArrayList<RuleClass> frequent = new ArrayList<RuleClass>();
						RuleClass rule = new RuleClass(levelLeft,levelRight,atts);
						frequent.add(rule);
						items.add(rule);
						frequentItems.add(frequent);
						double old_confflex=100*( (double)compare(levelListRight, to).size()/(double)levelListRight.size())
								* ( (double)compare(levelListLeft,from).size()/(double)levelListLeft.size());

						Root lastNodeAdded = new Root();
						lastNodeAdded = mainTrie.addRule(frequent);

							isThereAtLeastOneActionRule = 1;
							lastNodeAdded.isItActionRule = 1;

							if(!rule.left.equalsIgnoreCase(rule.right)){
								//what is util ?
								
								listModelResult.append(rule.column+"("+rule.left+"->"+rule.right+") => ("+ff+"->"+tt +")"+","+ "support = "+ old_supflex + ","+"confidence="+old_confflex+"\n");
							}

				}
			}
		}

		for(int ix = 0; ix < frequentItems.size(); ix++)
		{
			atomicFrequentItems.add(frequentItems.get(ix).get(0));
		}

		ArrayList<ArrayList<Integer>> myNextLastIntegers = new ArrayList<ArrayList<Integer>>();

		ArrayList<Integer> myFrequentIntegers = new ArrayList<Integer>();

		for(int jx = 0; jx < atomicFrequentItems.size(); jx++)
		{
			ArrayList<Integer> temp = new ArrayList<Integer>();
			temp.add(jx);
			myNextLastIntegers.add(temp);
			myFrequentIntegers.add(jx);
		}

		ArrayList<ArrayList<Integer>> myLastIntegers = new ArrayList<ArrayList<Integer>>();

		Boolean continueFlag = false;
		do
		{
			continueFlag = false;

			myLastIntegers.clear();

			for(int ix = 0; ix < myNextLastIntegers.size(); ix++)
			{
				myLastIntegers.add(myNextLastIntegers.get(ix));
			}

			myNextLastIntegers.clear();

			for(int j = 0 ; j < atomicFrequentItems.size(); j++)
			{
				for(int i = 0; i < myLastIntegers.size(); i++)
				{
					Integer justForTesting = myLastIntegers.get(i).get(myLastIntegers.get(i).size()-1);
					if(justForTesting > j)
						break;

					ArrayList<Integer> myCurrNewRule = new ArrayList<Integer>();
					ArrayList<RuleClass> myCurrNewRule_rules = new ArrayList<RuleClass>();

					int numberOfAtomicActions = myLastIntegers.get(i).size();

					boolean itContains = false;

					if(myLastIntegers.get(i).contains(myFrequentIntegers.get(j)))
						itContains = true;
					else
					{
						RuleClass frequentRule = atomicFrequentItems.get(j);
						for(int ix = 0; ix < numberOfAtomicActions; ix++)
						{
							RuleClass currRule = atomicFrequentItems.get(myLastIntegers.get(i).get(ix));
							if(frequentRule.column == currRule.column)
							{
								itContains = true;
								break;
							}
						}
					}

					//}

					if(!itContains)
					{

						ArrayList<Integer> comparLeft = map.get(atomicFrequentItems.get(j).column).get(atomicFrequentItems.get(j).left);
						ArrayList<Integer> comparRight = map.get(atomicFrequentItems.get(j).column).get(atomicFrequentItems.get(j).right);

						int continueAgain = 1;
						for(int k = 0; k < numberOfAtomicActions; k++)
						{
							Integer ruleIndex = myLastIntegers.get(i).get(k);
							RuleClass currRule = atomicFrequentItems.get(ruleIndex);

							comparLeft = compare(map.get(currRule.column).get(currRule.left), comparLeft);
							comparRight = compare(map.get(currRule.column).get(currRule.right), comparRight);
						}

						if(continueAgain == 1)
						{
							supportold = compare(comparLeft,from).size();
							int supTo = compare(comparRight, to).size();
							support = compare(comparRight, to).size();

							boolean continueWithAtomicActions = true;

							for(int k = 0; k < numberOfAtomicActions; k++)
							{
								if(myFrequentIntegers.get(j) < myLastIntegers.get(i).get(k))
								{
									myCurrNewRule_rules.add(atomicFrequentItems.get(myFrequentIntegers.get(j)));

									myCurrNewRule.add(myFrequentIntegers.get(j));

									for(int kx = k; kx < numberOfAtomicActions; kx++)
									{
										myCurrNewRule.add(myLastIntegers.get(i).get(kx));
										myCurrNewRule_rules.add(atomicFrequentItems.get(myLastIntegers.get(i).get(kx)));
									}
									continueWithAtomicActions = false;
									break;
								}
								else
								{
									myCurrNewRule.add(myLastIntegers.get(i).get(k));
									myCurrNewRule_rules.add(atomicFrequentItems.get(myLastIntegers.get(i).get(k)));
								}
							}
							if(continueWithAtomicActions)
							{
								myCurrNewRule.add(myFrequentIntegers.get(j));
								myCurrNewRule_rules.add(atomicFrequentItems.get(myFrequentIntegers.get(j)));
							}

							Root lastNodeAdded = new Root();
							lastNodeAdded = mainTrie.addRule(myCurrNewRule_rules);

							if(lastNodeAdded.isItNew == 1)
							{
								myNextLastIntegers.add(myCurrNewRule);
								continueFlag = true;
							}

							double old_confmix=100 * ( (double)supportold/(double)comparLeft.size()) * ( (double)supTo/(double)comparRight.size());
								isThereAtLeastOneActionRule = 1;

								lastNodeAdded.isItActionRule = 1;

								String result="";
								String col="";
								boolean doAdd = false;
								if(myCurrNewRule_rules.size()==1){
									RuleClass rule = myCurrNewRule_rules.get(0);
									if(!rule.left.equalsIgnoreCase(rule.right)){
										doAdd = true;
									}
								}
								else if(myCurrNewRule_rules.size()>1){
									doAdd = true;
								}
								if(doAdd){
									boolean multiValueDoAdd = false;
									for(RuleClass rr:myCurrNewRule_rules)
									{
											if(!rr.left.equalsIgnoreCase(rr.right)){
												multiValueDoAdd = true;
											}
											col = col + " " + rr.column;
											result = result+"("+rr.left+"->"+rr.right+")";
									}
									result = result + " => (" + ff + "->" + tt + ")";
									if(multiValueDoAdd){
										listModelResult.append(col+" "+result+ ","+ "support = "+supportold+","+"confidence="+old_confmix+"\n");
									}
								}

							}
					}
				}
			}

		}while(continueFlag);

		if(isThereAtLeastOneActionRule == 0)
			listModelResult.append("No Action Rules");

		return listModelResult.toString();
		//PrintWriter out = new PrintWriter(folder+"/ActionRules.txt");
		//out.print(listModelResult.toString());
		//out.close();
	}

	public ArrayList<Integer> compare (ArrayList<Integer> l1, ArrayList<Integer> l2)
	{
		ArrayList<Integer> l= new ArrayList<Integer>();
		for(Integer i:l1)
			if(l2.contains(i))
				l.add(i);
		return l;
	}

	public void connectorFunction(HashMap<String, HashMap<String,HashMap<String, ArrayList<Integer>>>> varNameTypeContentMap,
			ArrayList<Integer> decisionFromList, ArrayList<Integer> decisionToList){

		LOG.debug("Print");

		LOG.debug(varNameTypeContentMap.get("Stable").toString());
		mapStable = varNameTypeContentMap.get("Stable");

		mapFlexible = varNameTypeContentMap.get("Flexible");
		map = new HashMap<String, HashMap<String, ArrayList<Integer>>>();
		map.putAll(mapStable);
		map.putAll(mapFlexible);
		map.putAll(varNameTypeContentMap.get("Decision"));
		from = decisionFromList;
		to = decisionToList;
	}

}