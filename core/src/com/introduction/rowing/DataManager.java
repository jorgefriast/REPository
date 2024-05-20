package com.introduction.rowing;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.FileHandler;

public class DataManager {

    private int balance;
    public List<ShopBoat> boats;
    private static final String MONEY_BALANCE_FILE_PATH = "core/data/money_balance.txt";
    private static final String BOATS_FILE_PATH = "data/boats.csv";

    public DataManager() {
        this.balance = readBalance();
        this.boats = readShopBoats();
    }

    // Read the txt file to get the balance
    private int readBalance() {
        int balance = 0;
        BufferedReader reader = null;
        FileHandle file = Gdx.files.local(MONEY_BALANCE_FILE_PATH);
        try {
            reader = new BufferedReader(new InputStreamReader(file.read()));
            String line = file.readString();
            if (line != null) {
                balance = Integer.parseInt(line.trim());
            }
        } catch ( NumberFormatException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return balance;
    }
    public void saveBalance() {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(MONEY_BALANCE_FILE_PATH));
            writer.write(Integer.toString(balance));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
        saveBalance();
    }

    private List<ShopBoat> readShopBoats() {
        List<ShopBoat> boats = new ArrayList<ShopBoat>();
        InputStream is = null;
        BufferedReader reader = null;
        try {
            is = getClass().getClassLoader().getResourceAsStream(BOATS_FILE_PATH);
            if (is == null) {
                throw new IOException("File not found: " + BOATS_FILE_PATH);
            }
            reader = new BufferedReader(new InputStreamReader(is));
            String line;
            reader.readLine(); // Skip the header
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                ShopBoat boat = new ShopBoat(
                        Integer.parseInt(data[0]), data[1], Integer.parseInt(data[2]), data[3],
                        Integer.parseInt(data[4]), Integer.parseInt(data[5]),
                        Integer.parseInt(data[6]), Integer.parseInt(data[7]),
                        Integer.parseInt(data[8]), Integer.parseInt(data[9]),
                        Boolean.parseBoolean(data[10]), Boolean.parseBoolean(data[11])
                );
                boats.add(boat);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println(boats);
        return boats;
    }

}
