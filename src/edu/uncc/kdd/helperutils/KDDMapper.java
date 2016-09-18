package edu.uncc.kdd.helperutils;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Mapper.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.uncc.kdd.mapreduce.MapReduce;

public class KDDMapper extends Mapper<LongWritable, Text, LongWritable, Text> {
	
	private static Logger logger = LoggerFactory.getLogger(KDDMapper.class);
	 
	public void map(LongWritable key, Text value, Context context)
				throws InterruptedException, IOException {
		String line = value.toString();
		context.write(key, new Text(line));
		//logger.info("Map processing finished");
		}
	}
