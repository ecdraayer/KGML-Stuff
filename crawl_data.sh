# Run Raul's KOSearch program in terminal
#java -cp kgml.jar KOExtraction.KOextraction -f ../pathway/patric_downloads/511145.12.PATRIC.faa -o ../pathway/Bacteria_511145.12.PATRIC/csv/Seq_KO -r
#java -cp kgml.jar KOExtraction.KOextraction -f ../pathway/patric_downloads/1385755.3.PATRIC.faa -o ../pathway/Bacteria_1385755.3.PATRIC/csv/Seq_KO -r
#java -cp kgml.jar KOExtraction.KOextraction -f ../pathway/patric_downloads/1385755.5.PATRIC.faa -o ../pathway/Bacteria_1385755.5.PATRIC/csv/Seq_KO -r
#java -cp kgml.jar KOExtraction.KOextraction -f ../pathway/patric_downloads/1385755.6.PATRIC.faa -o ../pathway/Bacteria_1385755.6.PATRIC/csv/Seq_KO -r

#Human sequence
#java -cp kgml.jar KOExtraction.KOextraction -f ../pathway/Human/Homo_sapiens.GRCh38.pep.abinitio.fa -o ../pathway/Human/csv/Seq_KO -r




# Run Raul's KOSearch program in terminal
#java -cp kgml.jar KOSearch.KOSearch -i ../pathway/Bacteria_511145.12.PATRIC/csv/Seq_KO.csv -o ../pathway/Bacteria_511145.12.PATRIC/
#java -cp kgml.jar KOSearch.KOSearch -i ../pathway/Bacteria_1385755.3.PATRIC/csv/Seq_KO.csv -o ../pathway/Bacteria_1385755.3.PATRIC/
#java -cp kgml.jar KOSearch.KOSearch -i ../pathway/Bacteria_1385755.5.PATRIC/csv/Seq_KO.csv -o ../pathway/Bacteria_1385755.5.PATRIC/
#java -cp kgml.jar KOSearch.KOSearch -i ../pathway/Bacteria_1385755.6.PATRIC/csv/Seq_KO.csv -o ../pathway/Bacteria_1385755.6.PATRIC/

#Human
#java -cp kgml.jar KOSearch.KOSearch -i ../pathway/Human/csv/Seq_KO.csv -o ../pathway/Human/ -r


# Run Raul's TransformToGraph
java -cp kgml.jar TransformToGraph.TransformToGraph -f ../pathway/Bacteria_879462.4.PATRIC -k 50 -nv
java -cp kgml.jar TransformToGraph.TransformToGraph -f ../pathway/Human -k 50 -nv
