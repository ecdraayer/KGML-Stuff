package BuildIndex;

import java.io.IOException;

import KOExtraction.ExportCsv;

public class WriteIndex {
	
	 public WriteIndex()
	 {
		 
	 }
	
	 public void WriteIdx(String[][] Distances)
	 {
		ExportCsv Csv = new ExportCsv("_idx.csv", false,"Landamark, Destination, Distance");
		for (String[] s: Distances)
		{
			try {
				Csv.WriteFieldCSV(s[0] ,0);
			
				Csv.WriteFieldCSV(s[1] ,0);
				Csv.WriteFieldCSV(s[2] ,0);
				Csv.WriteFieldCSV("" ,1);
				Csv.FlushCSV();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println(e.toString());
			}
		}
		Csv.closeCSV();
		
	 }
}
