package KOExtraction;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;


public class KOextraction {

	public static void main(String[] args) {
			
		String fastaname="";
		String output="";
		String k="10";
		Integer c=0;
		int j=0;
		boolean contd=false; 
		if (args.length >= 4)
		{
			while (j < args.length)
			{
			 
				if (args[j].equals("-f"))
				{		
					if (args[j+1].contains("-")==false)
						fastaname=args[j+1];
			
				}
				if (args[j].equals("-o"))
				{
					if (args[j+1].contains("-")==false)
						output=args[j+1] + ".csv";
				}
				if (args[j].equals("-c"))
				{
					//option to indicate on which index to restart the program
					if (args[j+1].contains("-")==false)
					{
						c=Integer.parseInt(args[j+1]);
						contd=true;
					}
				}
				if (args[j].equals("-k"))
				{
					if (args.length ==6 || args.length==8)
					{
						if (args[j+1].contains("-")==false)
							k=args[j+1];
					}
				}
				j++;
			}
				
		
		
			if (output.isEmpty()==false && fastaname.isEmpty()==false)
			{
		        ParseFasta fasta = new ParseFasta(fastaname); 
				
		       
		        
				BlastSearch Bsearch = new BlastSearch();
				ExportCsv Csv = new ExportCsv(output, contd,"Sequence id,Sequence Description,gene_KO_number,organism id");
				
		        for (int i=c; i < fasta.sequences.size() ; i++)
		        {
		        	//System.out.println(fasta.sequences.get(i));
		        	Bsearch.Search(fasta.sequences.get(i),k);
		        	
		        	ArrayList<String> DistKOs = new  ArrayList<String> ();
		        	DistKOs =Bsearch.ReturnDistinctValues(2);
		        	ArrayList<String> KONums = new  ArrayList<String> ();
		        	KONums=Bsearch.Returncolumn(2);
		        	ArrayList<String> OrgIds = new  ArrayList<String> ();
		        	OrgIds=Bsearch.Returncolumn(0);
		        	
		        	
		        	System.out.println(i +": KO Number: " + Arrays.toString(DistKOs.toArray()) + " for " + fasta.descriptions.get(i) );
		        	
		        	try {
		        		for (int i1=0 ; i1 < KONums.size();i1++)
			        	{
							Csv.WriteFieldCSV(Integer.toString(i), 0);
							Csv.WriteFieldCSV(fasta.descriptions.get(i), 0);
							Csv.WriteFieldCSV(KONums.get(i1), 0);
							Csv.WriteFieldCSV(OrgIds.get(i1) ,0);
							Csv.WriteFieldCSV("" ,1);
						
							
			        	}
						
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}	        	

		        }
		        Csv.closeCSV();
			}
			else
				System.out.println("Please check parameters.");
		}
		else			
		{
			System.out.println("Please provide approtiate arguments");
			System.out.println("-f fasta file -o output filename -k top matches(default 10) -c continue at the specified index");
			
		}
		
	}
}
