package com.illumino.parser;

import com.illumino.domain.FlowLogData;
import com.illumino.domain.LookupTableData;
import com.opencsv.bean.CsvToBeanBuilder;
import org.apache.commons.lang3.tuple.Pair;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FlowLogDataParser {

    private static final String flowLogfileName = "flowlog.csv";
    private static final String lookupTableFileName = "lookupTable.csv";
    public static void main(String[] args)  {
        //read flow log data from csv
        List<FlowLogData> flowLogDataList= readCsvData(flowLogfileName, FlowLogData.class);
        //read lookup table data from csv
        List<LookupTableData> lookupTableDataList= readCsvData(lookupTableFileName, LookupTableData.class);

        //map to hold tag based on port and protocol pair
        Map<Pair<Integer, String>, String> tagLookupMap = new HashMap<>();
        //map to hold count of port and protocol combination
        Map<Pair<Integer, String>, Integer> portProtocolCountMap = new HashMap<>();

        //read once and populate both the maps
        lookupTableDataList.forEach(lookupTableData -> {
            Pair<Integer, String> pair = Pair.of(lookupTableData.getDstPort(),lookupTableData.getProtocol());
            tagLookupMap.put(pair, lookupTableData.getTag());
            portProtocolCountMap.put(pair, portProtocolCountMap.getOrDefault(pair,0)+1);
        });

        //map to hold tag counts
        Map<String, Integer> tagCountMap = new HashMap<>();

        flowLogDataList.forEach(flowLogData -> {
            Pair<Integer, String> pair = Pair.of(flowLogData.getDstPort(), flowLogData.getProtocol().toLowerCase());
            String tag = tagLookupMap.get(pair);
            tagCountMap.put(tag, tagCountMap.getOrDefault(tag,0)+1);
        });

        //assuming both outputs needs to written into separate csvs
        writeTagsCountFile(tagCountMap);
        writePortProtocolCountFile(portProtocolCountMap);
    }

    /**
     * Generic method to read csv and generate List of java objects
     * @param fileName in class path
     * @param T Type of the object
     * @return
     * @param <T>
     */
    private static <T> List<T> readCsvData(String fileName, Class<T> T) {
        List<T> flowLogDataList = new ArrayList<>();
        try {
             flowLogDataList = new CsvToBeanBuilder<T>(new FileReader(FlowLogDataParser.class.getClassLoader().getResource(fileName).getFile()))
                    .withType(T)
                    .build()
                    .parse();

        } catch (FileNotFoundException fileNotFoundException) {
            //add logs or throw exception based on use case
        }
        return flowLogDataList;
    }

    private static void writeTagsCountFile(Map<String, Integer> tagCountMap){
        try (FileWriter csvWriter = new FileWriter("tag_counts.csv")) {
            // Write header row
            csvWriter.append("Tag,Count\n");
            tagCountMap.entrySet().forEach(stringIntegerEntry -> {
                try {
                    csvWriter.append(stringIntegerEntry.getKey()==null?"Untagged" : stringIntegerEntry.getKey()).append(",");
                    csvWriter.append(String.valueOf(stringIntegerEntry.getValue())).append("\n");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            });

        } catch (IOException e) {
            e.printStackTrace();
            //add logs or throw exception based on use case
        }
    }

    private static void writePortProtocolCountFile(Map<Pair<Integer, String>, Integer> portProtocolCountMap){
        try (FileWriter csvWriter = new FileWriter("port_protocol_count.csv")) {
            // Write header row
            csvWriter.append("Port,Protocol,Count\n");
            portProtocolCountMap.entrySet().forEach(stringIntegerEntry -> {
                try {
                    if(stringIntegerEntry.getKey()!=null) {
                        csvWriter.append(String.valueOf(stringIntegerEntry.getKey().getLeft())).append(",").append(String.valueOf(stringIntegerEntry.getKey().getRight())).append(",");
                        csvWriter.append(String.valueOf(stringIntegerEntry.getValue())).append("\n");
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            });
        } catch (IOException e) {
            e.printStackTrace();
            //add logs or throw exception based on use case
        }
    }
}
