package com.example.RoboRallyAPI;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@RestController
public class GameConfigController {

    GameConfigController(){
        File file = new File("Servers");
        if (!file.exists()) {
            file.mkdir();
        }
    }

    @PostMapping(path = "/Servers")
    public void newServer(@RequestBody String JSONString, @RequestParam String serverID){
        String path = "Servers/" + serverID;
        File file = new File(path, "mapConfig.json");
        writeToJSONFile(JSONString, "");
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


}
