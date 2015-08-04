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

public class KOSearch {

	public static void main(String[] args) {
		
	     ReadCSV CSV = new ReadCSV("data/csv/Seq_KO.csv");
	     GetPathway Pathway = new GetPathway();
	     ArrayList<String> KOnums = new ArrayList<String>();
	     ArrayList<ArrayList<String>> Pathways = new ArrayList<ArrayList<String>>();
	     WebDriver driver = new HtmlUnitDriver();
	     
	     KOnums=CSV.ReadColDistinctValues(2);
	     System.out.println("Found " +KOnums.size() + " Distinct KO Numbers");
	     
	     
	
	     
	     ExportCsv Csv = new ExportCsv("data/csv/KO_mapid.csv", false,"gene_KO_number,pathway_id, pathway_url, KGML");
	     
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
			    				String  downloadedFileAbsoluteLocation = downloadFile.downloadFile(downloadLink.get(0), Pathways.get(j).get(0)+".xml");
			    				System.out.println("Downloading "+ Pathways.get(j).get(0) + "File to " + downloadedFileAbsoluteLocation);
			    				
			    				//download png file
			    				String  pnglocation = downloadFile.downloadImage(pngdownload, Pathways.get(j).get(0)+".png");
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
		    	     
		    		 } catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
		    		 }	
		    		 
		    	 }
	    	 }
	     
	     }
	     Csv.closeCSV();
	     
	}
}
