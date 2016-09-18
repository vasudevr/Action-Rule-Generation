package edu.uncc.kdd.helperutils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Team 7
 * 
 * @CreatedOn 4/3/2016 
 * 
 * ## The class fetches the data from the input file and 
 * ## parses it into a format required for the map reduce
 * ## algorithm
 *
 */

public class InputDocumentParser {

	private static final Log LOG = LogFactory.getLog(InputDocumentParser.class);
	
	private static Logger logger = LoggerFactory.getLogger(InputDocumentParser.class);

	public HashMap<String, HashMap<String,HashMap<String,ArrayList<Integer>>>> parseExcelData(InputStream is, List<String> stableVarList , List<String> flexVarList, List<String> decisionVarList) {
		try {
			HSSFWorkbook workbook = new HSSFWorkbook(is);

			// Taking first sheet from the workbook
			HSSFSheet sheet = workbook.getSheetAt(0);

			// Iterate through each rows from first sheet
			Iterator<Row> rowIterator = sheet.iterator();
			
			Map<Integer, String> indexTypeMap = new HashMap<Integer, String>();
			Map<Integer,String> varColMap = new HashMap<Integer,String>();
			HashMap<String, HashMap<String,HashMap<String,ArrayList<Integer>>>> varMap = new HashMap<String, HashMap<String,HashMap<String,ArrayList<Integer>>>>();
			while (rowIterator.hasNext()) {
				Row row = rowIterator.next();
				
				// For each row, iterate through each columns
				Iterator<Cell> cellIterator = row.cellIterator();

				while (cellIterator.hasNext()) {

					Cell cell = cellIterator.next();

					if(row.getRowNum()==0 && cell.getStringCellValue()!=null && !cell.getStringCellValue().trim().equals("")){
						
						for(String stableVar: stableVarList){
							if(stableVar.trim().equalsIgnoreCase(cell.getStringCellValue())){
								varColMap.put(cell.getColumnIndex(),cell.getStringCellValue());
								indexTypeMap.put(cell.getColumnIndex(),"Stable");
							}
						}
						
						for(String flexVar: flexVarList){
							if(flexVar.trim().equalsIgnoreCase(cell.getStringCellValue())){
								varColMap.put(cell.getColumnIndex(),cell.getStringCellValue());
								indexTypeMap.put(cell.getColumnIndex(),"Flexible");
							}
						}
						
						for(String decisonVar: decisionVarList){
							if(decisonVar.trim().equalsIgnoreCase(cell.getStringCellValue())){
								varColMap.put(cell.getColumnIndex(),cell.getStringCellValue());
								indexTypeMap.put(cell.getColumnIndex(),"Decision");
							}
						}
						
					}
					else {
						if(cell.getColumnIndex()>=0){
							String colType = indexTypeMap.get(cell.getColumnIndex());
							String colName = varColMap.get(cell.getColumnIndex());
							if(colType!=null && !colType.trim().equals("")){
								colType = colType.trim();
								HashMap<String,HashMap<String,ArrayList<Integer>>> colNameContentsMap= varMap.get(colType.trim());
								if(colNameContentsMap==null || colNameContentsMap.size()<=0){
									colNameContentsMap = new HashMap<String, HashMap<String,ArrayList<Integer>>>();
								}
								HashMap<String,ArrayList<Integer>> colNameContentList = colNameContentsMap.get(colName.trim());
								if(colNameContentList==null || colNameContentList.size()<=0){
									colNameContentList = new HashMap<String,ArrayList<Integer>>();
								}
								
								ArrayList<Integer> colNameContentPosition =null;
								
								
								//try to handle the format exception
								try{
									if(!colNameContentList.keySet().contains(String.valueOf(Double.valueOf(cell.getNumericCellValue()).intValue()).trim())){
										colNameContentPosition = new ArrayList<Integer>();
									}
									else{
										colNameContentPosition = colNameContentList.get(String.valueOf(Double.valueOf(cell.getNumericCellValue()).intValue()).trim());
									}
									
								}catch(Exception e){
									if(!colNameContentList.keySet().contains(cell.getStringCellValue().trim())){
										colNameContentPosition = new ArrayList<Integer>();
									}
									else{
										colNameContentPosition = colNameContentList.get(cell.getStringCellValue().trim());
									}
								}
								
								/*if(!colNameContentList.keySet().contains(cell.getStringCellValue().trim())){
									colNameContentPosition = new ArrayList<Integer>();
								}
								else{
									colNameContentPosition = colNameContentList.get(cell.getStringCellValue().trim());
								}*/
								
								colNameContentPosition.add(row.getRowNum());
								//Handle nuneric type error
								
								try{
									colNameContentList.put(String.valueOf(Double.valueOf(cell.getNumericCellValue()).intValue()).trim(),colNameContentPosition);
								}catch(Exception e){
									colNameContentList.put(cell.getStringCellValue(),colNameContentPosition);
								}
								
								
								colNameContentsMap.put(colName.trim(), colNameContentList);
								varMap.put(colType.trim(), colNameContentsMap);
							}
						}
					}
				}
			}
			is.close();
			return varMap;
		} catch (IOException e) {
			LOG.error("IO Exception : File not found " + e);
		}
		return null;

	}
}
