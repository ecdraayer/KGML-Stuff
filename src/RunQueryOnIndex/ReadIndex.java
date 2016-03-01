package RunQueryOnIndex;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeMap;

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

	public TreeMap<String, TreeMap<String, ArrayList<String>>>  ReadRow()
	{
		String line="";

		TreeMap<String ,TreeMap<String, ArrayList<String>>> tm = new TreeMap<String, TreeMap<String, ArrayList<String>>>();
		TreeMap<String, ArrayList<String>> content =  new TreeMap<String, ArrayList<String>>();
		String Landmark= "";
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
					if (i==1)
						Landmark=Cells.get(0);
					
					if (Landmark.equals(Cells.get(0)))
						content.put(Cells.get(1), Cells);
					else
					{
						tm.put(Landmark, content);
						Landmark=Cells.get(0);
						content =  new TreeMap<String, ArrayList<String>>();
						content.put(Cells.get(1), Cells);
					}					
				
				}
				i++;				
				
			}
			//add last landmark
			tm.put(Landmark, content);
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return tm;
	}
}
