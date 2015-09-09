package KOSearch;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import com.lazerycode.selenium.filedownloader.FileDownloader;

import KOExtraction.ExportCsv;

/**
 * This programs takes a csv as input(which includes a list of KO Numbers), then it generates a output csv file
 * with a list of each KO Number, the pathway id and URL.
 * 
 * It also downloads the appropriate KGML file and PNG for each pathway and stores it in the data folder.
 * @author Raul Alvarado
 *
 */
public class KOSearch {

	public static void main(String[] args) {
		String InputCSV="";
		String OutputPath="";
		int r=0;
		boolean contd=false; 
		if (args.length >= 4)
		{
			while (r < args.length)
			{
				if (args[r].equals("-i"))
				{		
					if (args[r+1].contains("-")==false)
						InputCSV=args[r+1];
			
				}
				if (args[r].equals("-o"))
				{
					if (args[r+1].contains("-")==false)
					{
						OutputPath=args[r+1];
						if (OutputPath.endsWith("\\")==false)
							OutputPath=OutputPath+"/";
						
					}
				}
				if (args[r].equals("-r"))
				{
					contd=true;
				}
				r++;
			}
		}
		else
			System.out.println("Please check parameters.");
		
		if (InputCSV.isEmpty()==false && InputCSV.isEmpty()==false)
		{
		     ReadCSV CSV = new ReadCSV(InputCSV);
		     GetPathway Pathway = new GetPathway();
		     ArrayList<String> KOnums = new ArrayList<String>();
		     ArrayList<ArrayList<String>> Pathways = new ArrayList<ArrayList<String>>();
		     WebDriver driver = new HtmlUnitDriver();
		     
		     KOnums=CSV.ReadColDistinctValues(2);
		     //print distinct values for debugging
		     /*for (int i=0; i<KOnums.size(); i ++)
		     {
		    	 System.out.println(KOnums.get(i));
		     }*/
		     if (contd==true)
		     {
		    	 ReadCSV UsedPathways = new ReadCSV(OutputPath + "KO_mapid.csv");
			     ArrayList<String> KOnumsUsed = new ArrayList<String>();
			     KOnumsUsed = UsedPathways.ReadColDistinctValues(0);
			     KOnums.removeAll(KOnumsUsed);
		     }
		     System.out.println("Found " +KOnums.size() + " Distinct KO Numbers");
		     
		     
		
		     
		     ExportCsv Csv = new ExportCsv(OutputPath + "/KO_mapid.csv", contd,"gene_KO_number,pathway_id, pathway_url, KGML");
		     
		     for (int i=0; i < KOnums.size(); i++)
		     {
		    	 if (!KOnums.get(i).equals("N.A."))
		    	 {
			    	 System.out.println(KOnums.get(i));
			    	 Pathways= Pathway.Search(KOnums.get(i));
			    	 
			    	 //when no pathways are found
			    	 if (Pathways.size()==0)
			    	 {
			    		 System.out.println(i + " For KO: " + KOnums.get(i) + " no Pathway was found." );
			    		 
						 try {
							Csv.WriteFieldCSV(KOnums.get(i),0);
							Csv.WriteFieldCSV("No pathway found",0);
							Csv.WriteFieldCSV("" ,1);
							Csv.FlushCSV();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
			    	 }
			    		 
			    	 for (int j=0; j < Pathways.size(); j++)
			    	 {
			    		 System.out.println("For KO: " + KOnums.get(i) + " with pathwayid: " + Pathways.get(j).get(0) + "and URL: " + Pathways.get(j).get(1) );
			    		 try {
							Csv.WriteFieldCSV(KOnums.get(i),0);
							Csv.WriteFieldCSV(Pathways.get(j).get(0),0);
							Csv.WriteFieldCSV(Pathways.get(j).get(1), 0);
										    		 
				    		 
				    		 
				    		 //download KGML file
				    	     FileDownloader downloadFile = new FileDownloader(driver);
				    	     driver.get(Pathways.get(j).get(1));
				    	   
				    	     List<WebElement> downloadLink = driver.findElements(By.linkText("Download KGML"));
				    	     WebElement pngdownload = driver.findElement(By.name("pathwayimage"));
				    	     if (downloadLink.size() > 0)
				    	     {
				    	    	
				    	    	 try {
				    				String  downloadedFileAbsoluteLocation = downloadFile.downloadFile(downloadLink.get(0), "\\" + OutputPath +  Pathways.get(j).get(0).replace("/", "-")+".xml");
				    				System.out.println("Downloading "+ Pathways.get(j).get(0) + "File to " + downloadedFileAbsoluteLocation);
				    				
				    				//download png file
				    				String  pnglocation = downloadFile.downloadImage(pngdownload,"\\" + OutputPath +  Pathways.get(j).get(0).replace("/", "-")+".png");
				    				System.out.println("Downloading PNG "+ Pathways.get(j).get(0) + "File to " + pnglocation);
				    			} catch (Exception e1) {
				    				// TODO Auto-generated catch block
				    				e1.printStackTrace();
				    			}
				    	     }
				    	     else
				    	     {
				    	    	 Csv.WriteFieldCSV("No KGML found", 0);
				    	    	 System.out.println("No KGML found for " + Pathways.get(j).get(0));
				    	     }
				    	     
				    	     Csv.WriteFieldCSV("" ,1);
				    	     Csv.FlushCSV();
				    	  
			    	     
			    		 } catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
			    		 }	
			    		 
			    	 }
		    	 }
		     
		     }
		     Csv.closeCSV();
		     System.out.println("Finished.");
		}
		else			
		{
			System.out.println("Please provide appropriate arguments");
			System.out.println("-i input csv file -o output path(no filename needed) " );
			System.out.println(" -r restart program, it compares the input and output files to determine list of peding KO Number to search");
			
		}
	}
}
