package edu.uncc.kdd.helperutils;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.uncc.kdd.mapreduce.DocumentParser;



public class KDDInputFormat extends FileInputFormat<LongWritable,Text>{

	private static Logger logger = LoggerFactory.getLogger(KDDInputFormat.class);
	
	@Override
	public RecordReader<LongWritable, Text> createRecordReader(InputSplit split,
			TaskAttemptContext context) throws IOException, InterruptedException {
		
		logger.info("inisde KDDINPUT");
		return new DocumentParser();
		}
}