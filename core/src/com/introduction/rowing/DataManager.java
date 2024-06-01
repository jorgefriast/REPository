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
    private static final String BOATS_FILE_PATH = "core/data/boats.csv";
    private static final String INVENTORY_FILE_PATH = "core/data/inventory";

    public DataManager() {
        this.balance = readBalance();
        this.boats = readShopBoats();
    }

    // Read the txt file to get the balance
    private int readBalance() {
        int balance = 0;
        FileHandle file = Gdx.files.local(MONEY_BALANCE_FILE_PATH);

        if (!file.exists()) {
            file.writeString("0", false);
        }

        String line = file.readString();
        if (line != null) {
            balance = Integer.parseInt(line.trim());
        }

        return balance;
    }

    public void saveBalance() {
        FileHandle file = Gdx.files.local(MONEY_BALANCE_FILE_PATH);
        file.writeString(Integer.toString(balance), false);
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
        FileHandle fileHandle = null;
        BufferedReader reader = null;
        try {
            fileHandle = new FileHandle(BOATS_FILE_PATH);
            reader = new BufferedReader(new FileReader(fileHandle.file()));
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
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println(boats);
        return boats;
    }


    public void saveShopBoats() {
        FileHandle file = Gdx.files.local(BOATS_FILE_PATH);
        StringBuilder sb = new StringBuilder();
        sb.append("id,name,price,imageName,speedFactor,acceleration,robustness,maneuverability,momentumFactor,fatigue,unlocked,selected\n");
        for (ShopBoat boat : boats) {
            sb.append(boat.getId()).append(",");
            sb.append(boat.getName()).append(",");
            sb.append(boat.getPrice()).append(",");
            sb.append(boat.getImageName()).append(",");
            sb.append(boat.getSpeedFactor()).append(",");
            sb.append(boat.getAcceleration()).append(",");
            sb.append(boat.getRobustness()).append(",");
            sb.append(boat.getManeuverability()).append(",");
            sb.append(boat.getMomentumFactor()).append(",");
            sb.append(boat.getFatigue()).append(",");
            sb.append(boat.isUnlocked()).append(",");
            sb.append(boat.isSelected()).append("\n");
        }
        file.writeString(sb.toString(), false);
    }

    public ShopBoat getSelectedBoat() {
        for (ShopBoat boat : boats) {
            if (boat.isSelected()) {
                return boat;
            }
        }
        return null;
    }

    public Powerup getPowerup(MyRowing myRowing) {
        FileHandle file = Gdx.files.local(INVENTORY_FILE_PATH);
        if (!file.exists()) {
            return null;
        }
        int id = Integer.parseInt(file.readString());
        switch (id) {
            case 0:
                return new FishPowerup(myRowing);
            case 1:
                return new CookiePowerup(myRowing);
            case 2:
            case 3:
            default:
                return null;
        }
    }
}
