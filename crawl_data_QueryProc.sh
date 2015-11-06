# Run Raul's TransformToGraph
# Read graph from csv 
java -cp kgml.jar TransformToGraph.TransformToGraph -c ../pathway/csv/Bacteria/ -k 50 -nv
java -cp kgml.jar TransformToGraph.TransformToGraph -c ../pathway/csv/Human/ -k 50 -nv

# Get shortest path from GenesToQuery.csv
java -cp kgml.jar TransformToGraph.QueryGenerator -f ../pathway/csv/Bacteria/ -g ../pathway/csv/Bacteria/GenesToQuery.csv -nv
java -cp kgml.jar TransformToGraph.QueryGenerator -f ../pathway/csv/Human/ -g ../pathway/csv/Human/GenesToQuery.csv -nv
