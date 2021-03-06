package KOSearch;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * This class reads a comma separated file.
 * 
 * It reads the file by column and return array of the specified index of the column.
 * It can also return a list of the distinct values per column.
 * 
 * @author Raul Alvarado
 *
 */
public class ReadCSV {

	String Filename;
	private BufferedReader br = null;
	private FileReader fileReader = null;
	public ReadCSV (String filename)
	{
		this.Filename=filename;
		
		try {
			fileReader = new FileReader(Filename);
			br = new BufferedReader(fileReader);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public ArrayList<String> ReadCol(int colidx) 
	{
		String line="";
	
		ArrayList<String> column = new ArrayList<String>();
		
		int i =0;
		try {
			while ((line = br.readLine()) != null) {
				
				if (i>0)
				{
					String[] Cells = line.split(",");
					column.add(Cells[colidx]);
				}
				i++;
				
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return column;
	}
	public ArrayList<String> ReadColDistinctValues(int colidx)
	{
		String line="";
		ArrayList<String> column = new ArrayList<String>();
		int i =0;
		try {
			while ((line = br.readLine()) != null) {
				
				if (i>0)
				{
					String[] Cells = line.split(",");
					
					
					
					
					column.add(Cells[colidx]);
				}
				i++;
				
				
			}
			System.out.println("Read " + i + " lines");
		
			
			 Set<String> hs = new HashSet<>();
			 hs.addAll(column);
			 column.clear();
			 column.addAll(hs);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return column;
	}

	
	
}
