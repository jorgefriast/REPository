package com.introduction.rowing;

import java.io.*;

public class MoneyBalance {

    private int balance;
    private static final String FILE_PATH = "com/introduction/data/money_balance.txt";

    public MoneyBalance() {
        this.balance = readBalance();
    }

    // Read the txt file to get the balance
    private int readBalance() {
        int balance = 0;
        InputStream is = null;
        BufferedReader reader = null;
        try {
            is = getClass().getClassLoader().getResourceAsStream(FILE_PATH);
            if (is == null) {
                throw new IOException("File not found: " + FILE_PATH);
            }
            reader = new BufferedReader(new InputStreamReader(is));
            String line = reader.readLine();
            if (line != null) {
                balance = Integer.parseInt(line.trim());
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
        return balance;
    }
    public void saveBalance() {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(FILE_PATH));
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
}
