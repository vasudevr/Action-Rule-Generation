package edu.uncc.kdd.mapreduce;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.uncc.kdd.helperutils.ActionRules;
import edu.uncc.kdd.helperutils.InputDocumentParser;
import edu.uncc.kdd.helperutils.KDDInputFormat;

public class DocumentParser extends RecordReader<LongWritable, Text> {
	private LongWritable key;
	private Text value;
	private InputStream inputStream;
	private String[] strArrayofLines;
	

	private static Logger logger = LoggerFactory.getLogger(DocumentParser.class);
	

	@Override
	public LongWritable getCurrentKey() throws IOException,InterruptedException {
		
		return key;
	}

	@Override
	public Text getCurrentValue() throws IOException, InterruptedException {
		
		return value;
	}

	@Override
	public float getProgress() throws IOException, InterruptedException {

		return 0;
		

	}

	@Override
	public void close() throws IOException {
		if (inputStream != null) {
			inputStream.close();
		}

	}


	@Override
	public void initialize(InputSplit splitVar, TaskAttemptContext contextObj)
			throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		logger.info("inisde DocumentParser");
		
		
		FileSplit splitObj = (FileSplit) splitVar;
		Configuration jobConfig = contextObj.getConfiguration();
		final Path filePath = splitObj.getPath();
		
		String stableValue = jobConfig.get("stableAttr");
		String flexibleValue = jobConfig.get("flexibleAttr");
		String decisionValue = jobConfig.get("decisionAttr");
		String fromTo = jobConfig.get("from:to");
		String suppConf = jobConfig.get("support:confidence");
		
		FileSystem fileSys = filePath.getFileSystem(jobConfig);
		FSDataInputStream fileInput = fileSys.open(splitObj.getPath());
		

		logger.info("before split");
		
		String[] fromToVal = fromTo.split(":");
		
		String from = fromTo.split(":")[0];
		String to = fromTo.split(":")[1];
		
		Integer support = Integer.parseInt(suppConf.split(":")[0]);
		Integer confidence = Integer.parseInt(suppConf.split(":")[1]);
		
		List<String> stableAttrList  = null;
		stableAttrList = Arrays.asList(stableValue.split(":"));
		
		List<String> flexibleAttrList = null;
		flexibleAttrList = Arrays.asList(flexibleValue.split(":"));
		
		List<String> decisionVarList  = null;
		decisionVarList = Arrays.asList(decisionValue.split(":"));
		
		
		inputStream = fileInput;
		
		HashMap<String, HashMap<String,HashMap<String, ArrayList<Integer>>>> varNameTypeContentMap = new InputDocumentParser().parseExcelData(inputStream, stableAttrList, flexibleAttrList, decisionVarList);
		ArrayList<Integer> decisionFromList = varNameTypeContentMap.get("Decision").get(decisionValue).get(from);
		ArrayList<Integer> decisionToList = varNameTypeContentMap.get("Decision").get(decisionValue).get(to);
		
		logger.info("before action rules generation");
		
		
		ActionRules action = new ActionRules();
		StringBuilder listModelResult = new StringBuilder();
		String line = action.kdd_lers_getFrequent(varNameTypeContentMap, support, confidence, from, to, decisionFromList, decisionToList, listModelResult, false);
		//logger.info(line);
		this.strArrayofLines = line.split("\n");
	
		
	}

	@Override
	public boolean nextKeyValue() throws IOException, InterruptedException {
		if (key == null) {
			key = new LongWritable(0);
			value = new Text(strArrayofLines[0]);

		} else {

			if (key.get() < (this.strArrayofLines.length - 1)) {
				long pos = (int) key.get();

				key.set(pos + 1);
				value.set(this.strArrayofLines[(int) (pos + 1)]);
				//logger.info("inside nextkeyValue");
				pos++;
			} else {
				return false;
			}

		}

		if (key == null || value == null) {
			return false;
		} else {
			return true;
		}
	}

}
