import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

/* Helper Class to find the join of two relations that uses bloom filter */
/* Requires google-collections-1.0-rc2.jar*/
public class BloomJoin 
{
	private String relation1, relation2;
	private String outputJoinRelation;
	private BloomFilterDet bloomFilter;
	private Multimap<String, String> multimapRelation1;
	private Multimap<String, String> multimapRelation2;
	public BloomJoin(String relation1Path, String relation2Path, String joinRelation, int setSize, int bitsPerRecord)
	{
		// Relations for which bloom join needs to be computed
		this.relation1 = relation1Path;
		this.relation2 = relation2Path;
		this.outputJoinRelation = joinRelation;
		this.bloomFilter = new BloomFilterDet(setSize,bitsPerRecord);
		this.multimapRelation1 = ArrayListMultimap.create();
		this.multimapRelation2 = ArrayListMultimap.create();
	}
	/* Adds the 1st relation strings to Bloom Filter*/
	public void createBloomFilter(String splitFactor)
	{
		//BufferedReader fileReader = getFileReader(table);
		String sCurrentLine;
		String joinAttr;
		String tupleList;
		try(BufferedReader br = new BufferedReader(new FileReader(relation1)))
		{
			while ((sCurrentLine = br.readLine()) != null) 
			{
				// Assuming the first column has the join attribute. Adding it to the Bloom filter if it is not already present
				String[] splitArr = sCurrentLine.split(splitFactor);
				joinAttr = splitArr[0];
				tupleList = splitArr[splitArr.length-1];
				if(!bloomFilter.appears(joinAttr))
				{
					bloomFilter.add(joinAttr);
				}
				multimapRelation1.put(joinAttr, tupleList);
			}
			
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		
	}
	/* Checks the filter for the join attribute of second relation*/
	public void checkFilterEntry(String splitFactor)
	{
		//BufferedReader fileReader = getFileReader(relation);
		String sCurrentLine;
		String joinAttr;
		String tupleList;
		try(BufferedReader br = new BufferedReader(new FileReader(relation2)))
		{
			while ((sCurrentLine = br.readLine()) != null) 
			{
				// Assuming the first column has the join attribute. Checking it with the Bloom filter
				String[] splitArr = sCurrentLine.split(splitFactor);
				joinAttr = splitArr[0];
				tupleList = splitArr[splitArr.length-1];
				
				if(bloomFilter.appears(joinAttr))
				{
					multimapRelation2.put(joinAttr, tupleList);
				}
			}
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		System.out.println(multimapRelation2.keySet().size());
	}
	
	/* Computes the join of the two relations */
	
	public void computeJoin()
	{
		Collection<String> keys = multimapRelation2.keySet();
		Collection  relation1Val = new ArrayList();
		Collection  relation2Val = new ArrayList();
		String firstRelVal = "";
		String secondRelVal = "";
		try 
		{
		    BufferedWriter writer = new BufferedWriter(new FileWriter(outputJoinRelation));
			for(Object key: keys)
			{
			   relation1Val = multimapRelation1.get((String)key);
			   relation2Val = multimapRelation2.get((String)key);
			   Iterator it1 = relation1Val.iterator();
			   while(it1.hasNext())
			   {
				   firstRelVal = it1.next().toString();
				   Iterator it2 = relation2Val.iterator();
				   while(it2.hasNext())
				   {
					  secondRelVal = it2.next().toString();
					  writer.write(firstRelVal + " ");
					  writer.write(key + " ");
					  writer.write(secondRelVal + " ");
					  //System.out.println(firstRelVal + " " + key + " " + secondRelVal);
					  writer.newLine();
					 
				   }
			   }
			}
			 writer.flush();
			 writer.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
}
