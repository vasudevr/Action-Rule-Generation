package edu.uncc.kdd.mapreduce;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.Mapper.Context;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.metrics2.sink.FileSink;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.uncc.kdd.helperutils.ActionRules;
import edu.uncc.kdd.helperutils.InputDocumentParser;
import edu.uncc.kdd.helperutils.KDDInputFormat;
import edu.uncc.kdd.helperutils.KDDMapper;
import edu.uncc.kdd.mapreduce.KDDReducer;

public class MapReduce {
	
	private static Logger logger = LoggerFactory.getLogger(MapReduce.class);
	
	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
		logger.info("Driver started");
		Configuration conf = new Configuration();
		conf.set("stableAttr", args[2]);
		conf.set("flexibleAttr", args[3]);
		conf.set("decisionAttr", args[4]);
		conf.set("from:to", args[5]);
		conf.set("support:confidence",args[6]);
		
		Job job = new Job(conf);
		job.setJarByClass(MapReduce.class);
		job.setJobName("Map Reduce");
		
		job.setMapperClass(KDDMapper.class);
		job.setReducerClass(KDDReducer.class);
		
		job.setOutputKeyClass(LongWritable.class);
		job.setOutputValueClass(Text.class);
		
		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		
		job.setInputFormatClass(KDDInputFormat.class);
		job.waitForCompletion(true);
	}

}


