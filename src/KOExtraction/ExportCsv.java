package KOExtraction;


import java.io.FileWriter;
import java.io.IOException;


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
			Field = Field.replace(",","-");
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
