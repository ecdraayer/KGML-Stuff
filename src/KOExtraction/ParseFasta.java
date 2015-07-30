package KOExtraction;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class ParseFasta {
	ArrayList<String> descriptions = new ArrayList<String>();
	ArrayList<String> sequences = new ArrayList<String>();
	String file;
	public ParseFasta( String File) {
		this.file=File;
		try {
			Readfromfile();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 }
	public ArrayList<String> GetDescriptions() throws FileNotFoundException
	{

		return descriptions;
	
	}
	public ArrayList<String> GetSequences() throws FileNotFoundException
	{

		return sequences;
	
	}
	private void Readfromfile() throws FileNotFoundException
	{
		BufferedReader br = null;
		
		br = new BufferedReader(new FileReader(file));
		StringBuffer   buffer = new StringBuffer();
		String line="";
		boolean closing = false;
	
		try {
	
			while ((line = br.readLine()) != null) 
			{
				if (line.isEmpty()==false)
				{
					if(line.startsWith(">")==true )					
					{
						descriptions.add(line);
						System.out.println( line);
						
						//when there is no * as the end
						if (closing==false)
						{
							sequences.add(buffer.toString());
							buffer.setLength(0);
						}
					}
					else
					{
						buffer.append(line);
						closing=false;
						if(line.endsWith("*"))
						{
							sequences.add(buffer.toString());
							buffer.setLength(0);
							closing=true;
						}
					
					}
				}
					
			}
			sequences.remove(0);
			System.out.println("Found " + descriptions.size() + " Descriptions.");
			System.out.println("Found " + sequences.size() + " Sequences.");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
