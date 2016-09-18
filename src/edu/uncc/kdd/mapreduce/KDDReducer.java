package edu.uncc.kdd.mapreduce;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.uncc.kdd.helperutils.KDDMapper;

public class KDDReducer extends Reducer<LongWritable, Text, LongWritable, Text>{

	private static Logger logger = LoggerFactory.getLogger(KDDReducer.class);
	
	public void reduce(LongWritable key, Iterable<Text> value, Context context)
	{
		Configuration jobConfig = context.getConfiguration();
		String suppConf = jobConfig.get("support:confidence");
		
		Integer support = Integer.parseInt(suppConf.split(":")[0]);
		Integer confidence = Integer.parseInt(suppConf.split(":")[1]);
		
		String[] array = null;
		String[] subarray = null;
		
		 while (value.iterator().hasNext())
	        {
			  String val = String.valueOf((value.iterator().next()));
			  array = val.split("support = ");
				subarray = array[1].split(",confidence=");
				
				if((Double.valueOf(subarray[0])) > Double.valueOf(support))
				{
					if( (Double.valueOf(subarray[1])) > Double.valueOf(confidence ))
					{
						try {
							context.write(key,new Text( val + " <----> "+ "Good"));
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
	        }
		
	}
}
