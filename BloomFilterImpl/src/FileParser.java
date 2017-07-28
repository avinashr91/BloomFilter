import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class FileParser {

	/**
	 * @param args
	 */
	public static void writeFile(String input, String output)
	{
		try (BufferedReader br = new BufferedReader(new FileReader(input)))
		{
			BufferedWriter writer = new BufferedWriter(new FileWriter(output));
			String sCurrentLin;
			String newCurrent = "";
			int i = 0;
			while ((sCurrentLin = br.readLine()) != null) 
			{
				String sCurrentLine = sCurrentLin.trim();
				for(i = sCurrentLine.length() -1 ; i>0 && (sCurrentLine.charAt(i) != '}' && sCurrentLine.charAt(i) != ')' ); i-- )
				{
					if(sCurrentLine.charAt(i) != ' ')
					{
						newCurrent+= sCurrentLine.charAt(i);
					}
					
				}
				if(newCurrent.trim().length() == 0)
				{
					newCurrent = ")";
					// System.out.println(sCurrentLin);
					for(i = i-1; i>0 && (sCurrentLine.charAt(i) != '}' && sCurrentLine.charAt(i) != ')' ); i-- )
					{
						
						if(sCurrentLine.charAt(i) != ' ')
						{
							newCurrent+= sCurrentLine.charAt(i);
						}
						
					}
				}
				
				StringBuffer sb = new StringBuffer(newCurrent.trim());
				//System.out.print(sb.reverse());
				newCurrent = "";
				
				for(int j = i; j >= 0 ; j--)
				{
					if(sCurrentLine.charAt(j) != ' ')
					{
						newCurrent+= sCurrentLine.charAt(j);
					}
				}
				//newCurrent.trim();
				StringBuffer sb1 = new StringBuffer(newCurrent.trim());
				//System.out.print(sb1.reverse());
				writer.write(sb1.reverse().toString());
				writer.write("&&");
				
				writer.write(sb.reverse().toString());
				//System.out.println("");
				writer.newLine();
				newCurrent = "";
			}
			writer.flush();
			writer.close();

		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String input1Rel = "D:\\Spring 2016\\COM S 535\\PA1\\movies.list";
		String output1Rel = "D:\\Spring 2016\\COM S 535\\PA1\\movies_Updated.txt";
		String input2Rel = "D:\\Spring 2016\\COM S 535\\PA1\\language.list";
		String output2Rel = "D:\\Spring 2016\\COM S 535\\PA1\\language_Updated.txt";
		String joinRel = "D:\\Spring 2016\\COM S 535\\PA1\\JoinBigData.txt";
		writeFile(input1Rel,output1Rel);
		writeFile(input2Rel,output2Rel);
		BloomJoin bloomJoin = new BloomJoin(output1Rel,output2Rel,joinRel,2000000,15);
		bloomJoin.createBloomFilter("&&");
		bloomJoin.checkFilterEntry("&&");
		bloomJoin.computeJoin();
	}

}
