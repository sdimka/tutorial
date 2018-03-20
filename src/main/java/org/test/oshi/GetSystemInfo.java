package org.test.oshi;

import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.OperatingSystem;
import oshi.util.FormatUtil;

public class GetSystemInfo {
    private SystemInfo si;
    private HardwareAbstractionLayer hal;
    private OperatingSystem os;
    private CentralProcessor processor;
    private GlobalMemory memory;

    private static GetSystemInfo instance = new GetSystemInfo();

    private GetSystemInfo(){
        si = new SystemInfo();
        hal = si.getHardware();
        os = si.getOperatingSystem();
        processor = hal.getProcessor();
        memory = hal.getMemory();
    }

    public static GetSystemInfo getInst() {
        return instance;
    }

    public float getCpuLoad(){
        return (float)(processor.getSystemCpuLoadBetweenTicks() * 100);
    }

    public float getFreeMem(){
       return  memory.getTotal() / 1073741824;
    }

    public float getAvailMem(){
        return memory.getAvailable()/ 1073741824;
    }

    public String getSysUpTime(){
        return FormatUtil.formatElapsedSecs(processor.getSystemUptime());
    }

    public String getOS(){
        return os.toString();
    }
}
