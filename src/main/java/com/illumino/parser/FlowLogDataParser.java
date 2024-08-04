package com.illumino.parser;

import com.illumino.domain.FlowLogData;
import com.illumino.domain.LookupTableData;
import com.opencsv.bean.CsvToBeanBuilder;
import org.apache.commons.lang3.tuple.Pair;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * This is a plain java class which read two csv files containing "flowlog data" and "lookup table data" and
 * based on data generates two csv files one with tag counts and other with port protocol counts.
 */
public class FlowLogDataParser {

    private static final String flowLogfileName = "flowlog.csv";//"flowlog_small.csv"//to test with larger data use flowlog.csv
    private static final String lookupTableFileName = "lookupTable.csv"; //"lookupTable_small.csv"//to test with larger data use lookupTable.csv

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService es = Executors.newFixedThreadPool(2);
        //read flow log data from csv
        long begin = System.currentTimeMillis();
        Future<List<FlowLogData>> flowLogDataList = es.submit(()->  readFlowLogCsvUsingBufferedReader());

        //read lookup table data from csv
        Future<List<LookupTableData>> lookupTableDataList = es.submit(()->readLookupTableCsvUsingBufferedReader());

        //map to hold tags based on port and protocol pair
        Map<Pair<Integer, String>, String> tagLookupMap = new HashMap<>();
        //map to hold count of port and protocol combination
        Map<Pair<Integer, String>, Integer> portProtocolCountMap = new HashMap<>();

        //read once and populate the map
        lookupTableDataList.get().forEach(lookupTableData -> {
            Pair<Integer, String> pair = Pair.of(lookupTableData.getDstPort(),lookupTableData.getProtocol()!=null?  lookupTableData.getProtocol().toLowerCase(): "");
            tagLookupMap.put(pair, lookupTableData.getTag());
        });

        //map to hold tag counts
        Map<String, Integer> tagCountMap = new HashMap<>();
        //populate both the maps
        flowLogDataList.get().forEach(flowLogData -> {
            Pair<Integer, String> pair = Pair.of(flowLogData.getDstPort(), flowLogData.getProtocol()!=null ?  flowLogData.getProtocol().toLowerCase(): "");
            String tag = tagLookupMap.get(pair);
            tagCountMap.put(tag, tagCountMap.getOrDefault(tag,0)+1);
            portProtocolCountMap.put(pair, portProtocolCountMap.getOrDefault(pair,0)+1);
        });
        //assuming both outputs needs to written into separate csvs
        writeTagsCountFile(tagCountMap);
        writePortProtocolCountFile(portProtocolCountMap);
        System.out.println("timeTaken to complete ="+ (System.currentTimeMillis()-begin));
    }

    /**
     * Generic method to read csv using opencsv api and generate List of java objects
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

    /**
     * Reads flowlog csv using Buffered Reader
     * reads only required fields i.e dstport and protocol
     * @return
     */
    private static List<FlowLogData> readFlowLogCsvUsingBufferedReader(){
        String line;
        List<FlowLogData> flowLogDataList= new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FlowLogDataParser.class.getClassLoader().getResource(flowLogfileName).getFile()))) {
            System.out.println("Header " + br.readLine());
            while ((line = br.readLine()) != null) {

                String[] list = line.split(",");
                FlowLogData flowLogData= new FlowLogData();
                flowLogData.setDstPort(Integer.valueOf(list[10]));//dstPort index in csv
                flowLogData.setProtocol(list[13]);//protocol index in csv
                flowLogDataList.add(flowLogData);
            }
        }catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {

        }
        return flowLogDataList;
    }

    /**
     * Reads LookupTableData csv using Buffered Reader
     * reads only required fields i.e dstport, protocol and tag
     * @return
     */
    private static List<LookupTableData> readLookupTableCsvUsingBufferedReader(){
        String line;
        List<LookupTableData> lookupTableDataList= new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FlowLogDataParser.class.getClassLoader().getResource(lookupTableFileName).getFile()))) {
            System.out.println("Header " + br.readLine());
            while ((line = br.readLine()) != null) {

                String[] list = line.split(",");
                LookupTableData lookupTableData= new LookupTableData();
                lookupTableData.setDstPort(Integer.valueOf(list[0]));//dstPort index in csv
                lookupTableData.setProtocol(list[1]);//protocol index in csv
                lookupTableData.setTag(list[2]);// tag index in csv
                lookupTableDataList.add(lookupTableData);
            }
        }catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return lookupTableDataList;
    }

    /**
     * Generates tag_counts.csv from the map
     * @param tagCountMap
     */
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

    /**
     * Generates port_protocol_count.csv from the map
     * @param portProtocolCountMap
     */
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
