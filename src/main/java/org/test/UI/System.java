package org.test.UI;

import com.vaadin.navigator.View;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import org.test.oshi.GetSystemInfo;

public class System extends VerticalLayout implements View {

    GetSystemInfo sys;

    public System(){
        sys = GetSystemInfo.getInst();

        setMargin(false);
        setSpacing(false);

        Label h1 = new Label("Что у нас есть:");
        h1.addStyleName(ValoTheme.LABEL_H2);
        addComponent(h1);

        Label mt = new Label("System: " + sys.getOS());
        mt.addStyleName(ValoTheme.LABEL_H3);
        addComponent(mt);

        Label mt1 = new Label("Uptime: " + sys.getSysUpTime());
        mt1.addStyleName(ValoTheme.LABEL_H3);
        addComponent(mt1);

        Label mt2 = new Label(sys.getCPUTemp() + " CPU Temp");
        mt2.addStyleName(ValoTheme.LABEL_H3);
        addComponent(mt2);

        CpuGraph chart = new CpuGraph();
        addComponent(chart);

        MemGraph chart2 = new MemGraph();
        addComponent(chart2);
    }
}
