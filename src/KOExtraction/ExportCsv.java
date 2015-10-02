package KOExtraction;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


/**
 * This class creates a comma separated file.
 * Filename and header must be provided to generated the csv file.
 * WriteFieldCSV accepts data cell by cell.  Must specify end of line.
 * @author Raul Alvarado
 *
 */
public class ExportCsv {

	String Filename;
	private String FILE_HEADER = "";	
	private static final String COMMA_DELIMITER = ",";
	private static final String NEW_LINE_SEPARATOR = "\n";
	private FileWriter fileWriter = null;
	public ExportCsv (String Fname, boolean contd, String Header)
	{
		this.Filename=Fname;
		this.FILE_HEADER=Header;
		try {
			fileWriter = new FileWriter(Filename,contd);
	
			if (contd==false)
			{
				fileWriter.append(FILE_HEADER.toString());
			
				//Add a new line separator after the header
		        fileWriter.append(NEW_LINE_SEPARATOR);
			}
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
			Field = Field.replace(",","-");
			fileWriter.append(Field);
			fileWriter.append(COMMA_DELIMITER);

		}	
	}
	public void FlushCSV()
	{
		try {
			fileWriter.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
	public void DeleteRows(String idxtoDel)
	{
		FileReader fileReader;
		try {
			fileReader = new FileReader(Filename);
			BufferedReader br = new BufferedReader(fileReader);
			String line="";
			try {
				while ((line = br.readLine()) != null) {
					if (line.startsWith(idxtoDel)==false)
					{
						fileWriter.append(line);
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
}
