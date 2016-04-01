# Run Raul's create Neo4j DB
# Read graph from cv and output shortespaths.
java -cp kgml.jar KGMLFunctions.Neo4j -f ../pathway/csv/Bacteria/Nodes.csv ../pathway/csv/Bacteria/Edges.csv -g ../pathway/csv/Bacteria/GenesToQuery.csv