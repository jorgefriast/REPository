package tools;


import com.badlogic.gdx.graphics.Texture;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CSVParser {
    private FileReader reader;
    public CSVParser(FileReader reader) {
        this.reader = reader;
    }

    private String intToString(int n) {
        StringBuilder sb = new StringBuilder();
        sb.append(n);
        return sb.toString();
    }

    /**
     * @param id
     * @return A map with the attributes of the boat with id "id"
     * @throws IOException
     */
    public Map<String, Integer> getBoatAttributes(int id) throws IOException {
        BufferedReader br = new BufferedReader(this.reader);
        String line;
        Map<String, Integer> attributes = new HashMap<>();
        while ((line = br.readLine()) != null) {
            String[] values = line.split(",");
            if (values[0].equals(intToString(id))) {
                attributes.put("speed_factor", Integer.parseInt(values[2]));
                attributes.put("acceleration", Integer.parseInt(values[3]));
                attributes.put("robustness", Integer.parseInt(values[4]));
                attributes.put("maneuverability", Integer.parseInt(values[5]));
                attributes.put("momentum_factor", Integer.parseInt(values[6]));
                attributes.put("bought", Integer.parseInt(values[7]));
                attributes.put("selected", Integer.parseInt(values[8]));
            }
        }
        return attributes;
    }

    public Texture getBoatTexture(int id) throws IOException {
        BufferedReader br = new BufferedReader(this.reader);
        String line;
        Texture texture = new Texture("boats/saoko.png");
        while ((line = br.readLine()) != null) {
            String[] values = line.split(",");
            if (values[0].equals(intToString(id))) {
                texture = new Texture(values[1]);
            }
        }
        return texture;
    }

}
