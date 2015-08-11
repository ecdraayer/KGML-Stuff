
src: source code
     * KOExtraction: written by Raul
         * KOextraction.java
           The main function to extract KO numbers of gene sequences
		   It takes the following argunments:
		   -f fasta file -o output filename -k top matches(default 10) -c continue at the specified index
		   For Example:
		   -f TAIR10_pep_20101214.fasta -o data/csv/Seq_KO -c 500
			
			
     * KOSearch: written by Raul

Data: contains the output of KOExtraction and KO search
      CSV files: from sequence id to KO number
                 from KO number to pathway map ids. 
      xml files: pathway KGML files.


