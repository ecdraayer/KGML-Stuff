package KGMLFunctions;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ReadCVSNodes {
	public static void main(String[] args) {

		ReadCVSNodes obj = new ReadCVSNodes();
		obj.run();

	  }

	  public void run() {

		String csvFile = "/home/edraa/workspace/KGML/Nodes.csv";
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";

		try {

			br = new BufferedReader(new FileReader(csvFile));
			line = br.readLine();
			while ((line = br.readLine()) != null) {

			    // use comma as separator
				String[] Entry = line.split(cvsSplitBy);

				System.out.println("Entry [name= " + Entry[0] 
	                                 + " , type=" + Entry[1]
	                                 + " , reaction=" + Entry[2] + "]");
				

			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	  }

}
