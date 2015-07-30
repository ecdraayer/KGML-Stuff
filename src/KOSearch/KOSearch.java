package KOSearch;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import com.lazerycode.selenium.filedownloader.FileDownloader;

import KOExtraction.ExportCsv;
import KOExtraction.ParseFasta;

public class KOSearch {

	public static void main(String[] args) {
		
	     ReadCSV CSV = new ReadCSV("Seq_KO.csv");
	     GetPathway Pathway = new GetPathway();
	     ArrayList<String> KOnums = new ArrayList<String>();
	     ArrayList<ArrayList<String>> Pathways = new ArrayList<ArrayList<String>>();
	     WebDriver driver = new HtmlUnitDriver();
	     
	     KOnums=CSV.ReadColDistinctValues(2);
	     System.out.println("Found " +KOnums.size() + " Distinct KO Numbers");
	     
	     
	     FileDownloader downloadTestFile = new FileDownloader(driver);
	     driver.get("http://www.genome.jp/en/gn_ftp.html");
	     WebElement downloadLink = driver.findElement(By.linkText("README"));
	   
	    	 try {
				String  downloadedFileAbsoluteLocation = downloadTestFile.downloadFile(downloadLink);
				System.out.println(downloadedFileAbsoluteLocation);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	
	     
	     ExportCsv Csv = new ExportCsv("KO_mapid.csv", false);
	     
	     for (int i=0; i < KOnums.size(); i++)
	     {
	    	 if (!KOnums.get(i).equals("N.A."))
	    	 {
		    	 System.out.println(KOnums.get(i));
		    	 Pathways= Pathway.Search(KOnums.get(i));
		    	 for (int j=0; j < Pathways.size(); j++)
		    	 {
		    		 System.out.println("For KO: " + KOnums.get(i) + " with pathwayid: " + Pathways.get(j).get(0) + "and URL: " + Pathways.get(j).get(1) );
		    		 try {
						Csv.WriteFieldCSV(KOnums.get(i),0);
						Csv.WriteFieldCSV(Pathways.get(j).get(0),0);
						Csv.WriteFieldCSV(Pathways.get(j).get(1), 0);
						Csv.WriteFieldCSV("",0);
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
