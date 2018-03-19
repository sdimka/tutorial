package org.test.getData;

import com.pi4j.platform.Platform;
import com.pi4j.platform.PlatformAlreadyAssignedException;
import com.pi4j.platform.PlatformManager;
import com.pi4j.system.NetworkInfo;
import com.pi4j.system.SystemInfo;

import java.io.*;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.stream.Collectors;

public class GetSysData {

    private static GetSysData instance = new GetSysData();

    private GetSysData(){

    }

    public static GetSysData getInst() {
        return instance;
    }

    public int GetCpuTemp(){
        byte[] bw = new byte[50];
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        int length;
        String command = "cat /sys/devices/virtual/thermal/thermal_zone0/temp";
        Process p = null;
        try {
            p = Runtime.getRuntime().exec(command);
            InputStream in = p.getInputStream();
            while ((length = in.read(bw)) != -1) {
                result.write(bw, 0, length);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return Integer.parseInt(result.toString())/1000;
    }

    public long GetMemTotal(){
        long mt = -1;
        try {
            mt = SystemInfo.getMemoryTotal();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return mt;
    }
}
