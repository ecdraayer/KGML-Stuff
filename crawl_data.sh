# Run Raul's KOSearch program in terminal
java -cp kgml.jar KOExtraction.KOextraction -f ../pathway/patric_downloads/511145.12.PATRIC.faa -o ../pathway/Bacteria_511145.12.PATRIC/csv/Seq_KO -r
java -cp kgml.jar KOExtraction.KOextraction -f ../pathway/patric_downloads/1385755.3.PATRIC.faa -o ../pathway/Bacteria_1385755.3.PATRIC/csv/Seq_KO -r
java -cp kgml.jar KOExtraction.KOextraction -f ../pathway/patric_downloads/1385755.5.PATRIC.faa -o ../pathway/Bacteria_1385755.5.PATRIC/csv/Seq_KO -r
java -cp kgml.jar KOExtraction.KOextraction -f ../pathway/patric_downloads/1385755.6.PATRIC.faa -o ../pathway/Bacteria_1385755.6.PATRIC/csv/Seq_KO -r

# Run Raul's KOSearch program in terminal
#java -cp kgml.jar KOSearch.KOSearch -i Bacteria_879462.4.PATRIC/CSV/Seq_KO.csv -o Bacteria_879462.4.PATRIC/



# Run Raul's TransformToGraph
java -cp kgml.jar TransformToGraph.TransformToGraph -f ../pathway/Bacteria_879462.4.PATRIC
