package RunQueryOnIndex;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class ReadIndex {
	private String indexFile;
	//private BufferedReader br = null;
	private FileReader fileReader = null;
	public ReadIndex (String indexFile)
	{
		this.indexFile=indexFile;
		
		try {
			fileReader = new FileReader(indexFile);
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public ArrayList<ArrayList<String>>  ReadRow()
	{
		String line="";
		ArrayList<ArrayList<String>> table = new ArrayList<ArrayList<String>>();
		try {
			fileReader = new FileReader(indexFile);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		BufferedReader br = new BufferedReader(fileReader);
		int i =0;
	
		try {
		
			while ((line = br.readLine()) != null) {
				
				if (i>0)
				{
					ArrayList<String> Cells = new ArrayList<String>(Arrays.asList(line.split(",")));
					
					table.add(Cells);
				}
				i++;
				
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return table;
	}
}
