import java.io.File;

public class ParserTester {
	public static void main(String[] args) {
		Pathway[] pathways = new Pathway[100];
		Pathway pathway = new Pathway();
    	final File folder = new File("/home/edraa/Desktop/Writer_Input");
    	int i = 1;
    	for(final File fileEntry : folder.listFiles()){
    		System.out.println(fileEntry.toString());
    		pathways[i] = new Pathway();
    		new MySaxParser(fileEntry.toString(), pathways[i]);
    		System.out.println(pathways[i]);
            new KGMLWrite(pathways[i]);
            i++;
    	}
    	
	}
	
	public static void listFilesForFolder(final File folder) {
	    for (final File fileEntry : folder.listFiles()) {
	        if (fileEntry.isDirectory()) {
	            listFilesForFolder(fileEntry);
	        } else {
	            System.out.println(fileEntry.getName());
	        }
	    }
	}



}
