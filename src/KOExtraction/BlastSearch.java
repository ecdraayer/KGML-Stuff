package KOExtraction;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
/**
 * 
 * This class is used to search for protein sequences in Blast Search tool 
 * found at http://www.genome.jp/tools-bin/search_sequence?prog=blastp
 * 
 * It returns a list of results found, Number of results depends on ktop variable.
 * @author Raul Alvarado
 *
 */
public class BlastSearch {
	ArrayList<ArrayList<String>> table = new ArrayList<ArrayList<String>>();

	public BlastSearch ()
	{
		
	}
	public ArrayList<ArrayList<String>> Search(String sequence, String ktop)
	{
		 WebDriver driver = new HtmlUnitDriver();
		 driver.get("http://www.genome.jp/tools-bin/search_sequence?prog=blastp");
		 driver.manage().timeouts().pageLoadTimeout(180, TimeUnit.SECONDS);
		 WebElement org = driver.findElement(By.name("sequence"));
		 org.sendKeys(sequence);
		 WebElement maxnumber = driver.findElement(By.name("V_value"));
		 maxnumber.clear();
		 maxnumber.sendKeys(ktop);	 
	
		 maxnumber.submit();


		
		 WebElement a 	 = driver.findElement(By.tagName("form"));
		 
		 String Fulltext="";
		 Fulltext= a.getText();
		 Fulltext = Fulltext.substring(Fulltext.indexOf("definitions")+12, Fulltext.length());
		 		 
		 
		 
		 table.clear();
		 String[] lines = Fulltext.split("\n");			
		 for (int i=0; i < lines.length; i ++)
		 {			
			 String[] cells = lines[i].split("  ");
			 String[] cellsb = new String[5]; 
	
			 //remove empty cells
			 int x=0;
			 for (int h=0; h < cells.length; h++)
			 {
				 if (cells[h].isEmpty()==false)
				 {
					 
					 cellsb[x]=cells[h];
					 x++;
				 }
			
		
			 }
			 //if no ko number 
			 if (cellsb[4]==null)
			 {
				 cellsb[4] = cellsb[3];
				 cellsb[3] = cellsb[2];
				 cellsb[2]="N.A.";
				
			 }	 
			 
			 if (cells.length > 1)
			 {
				 table.add(new ArrayList<String>());
				 for (int j=0; j < cellsb.length; j++)
				 {
					 try
					 {
					 table.get(i).add(cellsb[j].trim());
					 }
					 catch  (Exception e)
					 {
						 System.out.println(table.get(i));
						 System.out.println(e.getMessage());
					 }
				 }
			 }
		 }
		driver.close();
		
		return table;
		 
		
	}
	public ArrayList<String> Returncolumn(int idx)
	{
		 //[0] entry
		 //[1] name
		 //[2] K number
		 //[3] bits
		 //[4] E-eval
		 ArrayList<String> cols = new  ArrayList<String> ();
		
		 for (int i=0 ; i < table.size(); i++)
		 {
			// System.out.println(table.get(i).get(idx));
			 cols.add(table.get(i).get(idx));
			 
			 
		 }
		return cols;
		
	}
	public ArrayList<String> ReturnDistinctValues(int idx)
	{
		 //[0] entry
		 //[1] name
		 //[2] K number
		 //[3] bits
		 //[4] E-eval
		 ArrayList<String> cols = new  ArrayList<String> ();
		
		 for (int i=0 ; i < table.size(); i++)
		 {
			 cols.add(table.get(i).get(idx));			 
		 }
		 Set<String> hs = new HashSet<>();
		 hs.addAll(cols);
		 cols.clear();
		 cols.addAll(hs);
		return cols;
		
	}
	
}
