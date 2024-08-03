package com.illumino.parser;

import com.illumino.domain.FlowLogData;
import com.illumino.domain.LookupTableData;
import com.opencsv.bean.CsvToBeanBuilder;
import org.apache.commons.lang3.tuple.Pair;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FlowLogDataParser {
    public static void main(String[] args) throws IOException {

        String flowLogfileName = "flowlog.csv";

        List<FlowLogData> flowLogDataList = new CsvToBeanBuilder<FlowLogData>(new FileReader(FlowLogDataParser.class.getClassLoader().getResource(flowLogfileName).getFile()))
                .withType(FlowLogData.class)
                .build()
                .parse();

        flowLogDataList.forEach(System.out::println);


        String lookupTableFileName = "lookupTable.csv";

        List<LookupTableData> lookupTableDataList = new CsvToBeanBuilder<LookupTableData>(new FileReader(FlowLogDataParser.class.getClassLoader().getResource(lookupTableFileName).getFile()))
                .withType(LookupTableData.class)
                .build()
                .parse();

        lookupTableDataList.forEach(System.out::println);


        Map<Pair<Integer, String>, String> tagLookupMap = new HashMap<>();
        Map<Pair<Integer, String>, Integer> portProtocolCountMap = new HashMap<>();
        lookupTableDataList.forEach(lookupTableData -> {
            Pair<Integer, String> pair = Pair.of(lookupTableData.getDstPort(),lookupTableData.getProtocol());
            tagLookupMap.put(pair, lookupTableData.getTag());
            portProtocolCountMap.put(pair, portProtocolCountMap.getOrDefault(pair,0)+1);
        });

        Map<String, Integer> tagCountMap = new HashMap<>();

        flowLogDataList.forEach(flowLogData -> {
            Pair<Integer, String> pair = Pair.of(flowLogData.getDstPort(), flowLogData.getProtocol().toLowerCase());
            String tag = tagLookupMap.get(pair);
            tagCountMap.put(tag, tagCountMap.getOrDefault(tag,0)+1);
        });

        tagCountMap.entrySet().forEach(stringIntegerEntry -> System.out.println(stringIntegerEntry.getKey() + stringIntegerEntry.getValue()));

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
            // Close the csvWriter to save the data
            csvWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

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
            // Close the csvWriter to save the data
            csvWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }




    }
}
