# Description
This is a spring-boot project based on flowlog data and lookup table data it generates two csvs with tag counts and port protocol counts.
# How to Run
Run the main method of [FlowLogDataParser.java](https://github.com/praveenadg/illumino/blob/64beb93eb222e13c3cccd09029cca013a1516d10/src/main/java/com/illumino/parser/FlowLogDataParser.java#L25)
to test with a larger flow data file uncomment this [line](https://github.com/praveenadg/illumino/blob/64beb93eb222e13c3cccd09029cca013a1516d10/src/main/java/com/illumino/parser/FlowLogDataParser.java#L23) and use flowlog.csv

# Assumptions
* This could have been a simple java project but assuming there will be more enhancements created it as a spring-boot project.
* The input files are placed under resources folder.
* The output will be generated  into seperate csvs named **tag_counts.csv** and **port_protocol_count.csv** in the target directory.
* Uses Java-11
* Given the timeframe of 2 hours did not add unit-tests.

# Refereces
* [flow-logs-records-examples](https://docs.aws.amazon.com/vpc/latest/userguide/flow-logs-records-examples.html)
* [opencsv](https://javadoc.io/doc/com.opencsv/opencsv/5.7.1/index.html)
