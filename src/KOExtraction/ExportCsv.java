package KOExtraction;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class ExportCsv {

	String Filename;
	private static final String FILE_HEADER = "Sequence id,Sequence Description,gene_KO_number,organism id";
	private static final String COMMA_DELIMITER = ",";
	private static final String NEW_LINE_SEPARATOR = "\n";
	private FileWriter fileWriter = null;
	public ExportCsv (String Fname, boolean contd)
	{
		Filename=Fname;
		
		try {
			fileWriter = new FileWriter(Filename,contd);
			fileWriter.append(FILE_HEADER.toString());
			//Add a new line separator after the header
	        fileWriter.append(NEW_LINE_SEPARATOR);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void WriteFieldCSV(String Field, int endofline) throws IOException	
	{
		if (endofline==1)
		{
			fileWriter.append(NEW_LINE_SEPARATOR);
		}
		else
		{
			fileWriter.append(Field);
			fileWriter.append(COMMA_DELIMITER);

		}	
	}
	public void closeCSV()
	{
		   try {
			fileWriter.flush();
			  fileWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}
}
