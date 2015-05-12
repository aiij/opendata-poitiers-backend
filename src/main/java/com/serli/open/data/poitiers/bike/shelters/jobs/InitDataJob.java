package com.serli.open.data.poitiers.bike.shelters.jobs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.serli.open.data.poitiers.bike.shelters.jobs.model.JsonFromFile;
import com.serli.open.data.poitiers.bike.shelters.repository.ElasticRepository;
import com.serli.open.data.poitiers.bike.shelters.repository.InMemoryRepository;
import com.serli.open.data.poitiers.bike.shelters.rest.model.Shelter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by chris on 04/05/15.
 */
public class InitDataJob {
    public static void main(String[] args) throws IOException {
        loadData();
    }

    public static void loadData() throws IOException {
        InputStream inputData = InitDataJob.class.getResourceAsStream("/bike-shelters.json");
        Map<Integer, String> sheltersAdressesMap = createSheltersAdressMapFromFile();
        ObjectMapper objectMapper = new ObjectMapper();

        JsonFromFile[] jsonFromFiles = objectMapper.readValue(inputData, JsonFromFile[].class);
        Arrays.stream(jsonFromFiles).forEach(
                jsonFromFile -> {
                    indexShelter(jsonFromFile, sheltersAdressesMap);
                });
    }

    private static Map<Integer, String> createSheltersAdressMapFromFile() throws IOException {
        Map<Integer, String> sheltersAdressesMap = new HashMap<>();
        InputStream sheltersAdresses = InitDataJob.class.getResourceAsStream("/shelters-adresses.txt");
        try(BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(sheltersAdresses));){
            bufferedReader.lines().forEach(line -> {
                String[] split = line.split(" : ");
                sheltersAdressesMap.put(Integer.valueOf(split[0].substring(3)), split[1]);
            });
        }
        return sheltersAdressesMap;
    }

    private static void indexShelter(JsonFromFile jsonFromFile, Map<Integer, String> sheltersAdressesMap) {
        Shelter shelter = new Shelter(
                jsonFromFile.properties.shelterType,
                jsonFromFile.properties.capacity,
                jsonFromFile.geometry.coordinates,
                jsonFromFile.properties.objectId,
                sheltersAdressesMap.get(jsonFromFile.properties.objectId));
        InMemoryRepository.add(shelter);
        ElasticRepository.INSTANCE.index(shelter);
        System.out.println(shelter);
    }
}
