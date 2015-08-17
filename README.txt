
src: source code
     * KOExtraction: written by Raul
         * KOextraction.java
           The main function to extract KO numbers of gene sequences
		   It takes the following arguments:
		   -f fasta file 
		   -o output filename (no extension needed)
		   -k top matches(default 10) 
		   -r restart program at the last index found in csv file, new rows will be appended to existing file.
		   
		   For Example:
		   -f TAIR10_pep_20101214.fasta -o data/csv/Seq_KO -r
			
			
     * KOSearch: written by Raul
		KOSearch.java
		The main function takes a csv file as input(from KOExtraction), search for the KO Number in order to 
		retrieve the pathway id, KGML and Png files.  It output a CSV as a results along with the mention files.
		-i input csv file (output of KOExtraction)
		-o output path(no filename needed). It will output the a csv file plus KGML and png files.
		-r restart program, it compares the input and output files to determine list of pending KO Number to searh");
		
		 For Example:
		   -i Data\CSV\Seq_KO.csv -o Data\ -r
		
     * ParserTester: written by Erick Draayer
       The main function is to test the KGML pre-processing files such as parser, writer, and inverted index of gene          listing.
                  ParserTester.java needs the destination of the input files in the main function. ListGenes controls                    where the output of the inverted index of genese will be printed.

Data: contains the output of KOExtraction and KO search
      CSV files: from sequence id to KO number
                 from KO number to pathway map ids. 
      xml files: pathway KGML files.

================================================================================================
Steps to run Raul's code to download pathways from sequence files (by Raul)

PLEASE PUT THE DETAILED STEPS HERE.

================================================================================================
Resource to get sequence (FASTA) files (from 08/06/2015 Meeting)
  - added by Huiping

Get links of FASTA files for sequences of human, mouse, and didi’s organisms
    A. Bacteria. 
       Detailed information see email from Didi on 07/23/2015, title: three organism protein files.
       In bacteria Domain, Escherichia coli is most popular organism. 
       This strain is pathogen for human, and you can download protein.faa file.
       https://www.patricbrc.org/portal/portal/patric/Downloads?cType=taxon&cId=511145
       The downloaded file is patric_downloads.zip (4.8M).
       It contains five fas files, each one is about 2M. 
       We will use 1385755.3.PATRIC.faa. 

    B. For Human (Homo sapiens), detailed information see email from Didi on 07/23/2015, title: three organism protein files.
       http://uswest.ensembl.org/info/data/ftp/index.html (Links to FASTA files didi sent)
       Download “Protein sequence (FASTA)”
       The downloaded zip file has 4 files. 
       We use “Homo_sapiens.GRCh38.pep.abinitio.fa” (28.4M) NOT “Homo_sapiens.GRCh38.pep.all.fa” (57.4M)

    C. Mouse file can also be downloaded from
       http://uswest.ensembl.org/info/data/ftp/index.html (Links to FASTA files didi sent)
       We will work on it later. 
        
    D. Fungi (didi’s category). It cannot be downloaded.
       Didi sent the file “Cparasiticav2.GeneCatalog20091217.proteins.fasta” to Raul before.

    E. Plant: the following file is one that Raul used (from plant)
       https://www.arabidopsis.org/download_files/Proteins/TAIR10_protein_lists/TAIR10_pep_20101214
================================================================================================


