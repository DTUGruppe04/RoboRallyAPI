package com.example.RoboRallyAPI;

import com.google.gson.*;
import org.springframework.web.bind.annotation.*;

import java.io.*;

@RestController
public class GameConfigController {

    GameConfigController(){
        File file = new File("Servers");
        if (!file.exists()) {
            file.mkdir();
        }
    }

    @PostMapping(path = "/Servers")
    public void newServer(@RequestBody String JSONString, @RequestParam String serverID, @RequestParam String playerNumber){
        String path = "Servers/" + serverID;
        File file = new File("Servers/" + serverID);
        file.mkdir();
        File file1 = new File(path, "mapConfig.json");
        File file2 = new File(path, "playerNumberHandler.txt");
        try {
            file1.createNewFile();
            file2.createNewFile();
        } catch(IOException e) {
            System.out.println(e);
        }
        writeToJSONFile(JSONString, "Servers/" + serverID + "/mapConfig.json");
        writeToJSONFile(playerNumber, "Servers/" + serverID + "/playerNumberHandler.txt");
    }

    @GetMapping(path = "/Servers")
    public String joinServer(@RequestParam String serverID){
        String path = "Servers/" + serverID + "/mapConfig.json";
        return readFromJSONFile(path);
    }
    @GetMapping(path = "/Servers/playerNumber")
    public String getPlayerNumber(@RequestParam String serverID){
        String path = "Servers/" + serverID + "/playerNumberHandler.txt";
        int playerNumber = Integer.parseInt(readFromJSONFile(path));
        if (playerNumber != 1) {
            writeToJSONFile(String.valueOf(playerNumber-1), path);
            return String.valueOf(playerNumber-1);
        } else {
            return "0";
        }
    }
    @GetMapping(path = "/Servers/mapConfig")
    public String getMapConfig(@RequestParam String serverID){
        String path = "Servers/" + serverID + "/mapConfig.json";
        return readFromJSONFile(path);
    }

    @PutMapping(path = "/Servers/Player")
    public void updateSpecificPlayer(@RequestBody String JSONString, @RequestParam String serverID, @RequestParam String playerNumber) {
        String path = "Servers/" + serverID + "/mapConfig.json";
        JsonObject mapConfigFromClient = new JsonParser().parse(JSONString).getAsJsonObject();
        JsonObject mapConfigFromServer = new JsonParser().parse(readFromJSONFile(path)).getAsJsonObject();
        JsonArray jsonArrayClient = mapConfigFromClient.getAsJsonArray("players");
        JsonElement clientPlayer = jsonArrayClient.get(Integer.parseInt(playerNumber));
        mapConfigFromServer.getAsJsonArray("players").set(Integer.parseInt(playerNumber), clientPlayer);
        writeToJSONFile(mapConfigFromServer.toString(), path);
    }

    static void writeToJSONFile(String JSONString, String writeFilePath) {
        try {
            try (FileWriter fileWriter = new FileWriter(writeFilePath)) {
                fileWriter.write(JSONString);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static String readFromJSONFile(String readFilePath) {
        String JSONString;
        try (FileReader fileReader = new FileReader(readFilePath)){

            BufferedReader bufferedReader = new BufferedReader(fileReader);
            JSONString = bufferedReader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return JSONString;
    }


}
