package org.test.servoManage;

import java.io.*;
import java.nio.file.Files;

public class ServoCoordinates {

    private static ServoCoordinates servoCoordinates = new ServoCoordinates();

    int[] coordinates = new int[16];

    private ServoCoordinates(){
        try(FileReader fr = new FileReader("./servoData.bin");
        BufferedReader br = new BufferedReader(fr);
        ){
            String[] str = br.readLine().split(" ");
            for (int i = 0; i < coordinates.length; i++) {
                coordinates[i] = Integer.parseInt(str[i]);
            }

        } catch (Exception e) {
            e.printStackTrace();
            buildCoordinates();
        }
    }

    public static ServoCoordinates getInstance(){
        return servoCoordinates;
    }

    private void buildCoordinates() {
        for (int i = 0; i < coordinates.length; i++) {
            coordinates[i] = 1500;
        }
        new Thread(new Save()).start();
    }

    public int getCoord(int PinNumber){
        return coordinates[PinNumber];
    }

    public void setCoord(int PinNumber, int Coordinates){
        coordinates[PinNumber] = Coordinates;
        new Thread(new Save()).start();
    }

    private class Save implements Runnable{
        @Override
        public void run() {
            try (FileWriter fw = new FileWriter("./servoData.bin");
                 BufferedWriter bw = new BufferedWriter(fw);
            ) {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < coordinates.length; i++) {
                    sb.append(coordinates[i] + " ");
                }
                bw.write(sb.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
