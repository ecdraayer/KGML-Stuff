# Run Raul's TransformToGraph
# Read graph from csv 
java -cp kgml.jar TransformToGraph.TransformToGraph -c ../pathway/Bacteria_879462.4.PATRIC -k 50 -nv
java -cp kgml.jar TransformToGraph.TransformToGraph -c ../pathway/Human -k 50 -nv

# Get shortest path from GenesToQuery.csv
java -cp kgml.jar TransformToGraph.QueryGenerator -c ../pathway/Bacteria_879462.4.PATRIC/csv -k -g GenesToQuery.csv -nv
java -cp kgml.jar TransformToGraph.QueryGenerator -c ../pathway/Human/csv -g GenesToQuery.csv -nv
