package org.test.UI;

import com.vaadin.addon.charts.Chart;
import com.vaadin.navigator.View;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import org.test.oshi.GetSystemInfo;

public class System extends VerticalLayout implements View {

    GetSystemInfo sys;

    public System(){
        sys = GetSystemInfo.getInst();

        setMargin(true);

        Label h1 = new Label("Что у нас есть:");
        h1.addStyleName(ValoTheme.LABEL_H1);
        addComponent(h1);

        Label mt = new Label("Uptime: " + sys.getSysUpTime());
        mt.addStyleName(ValoTheme.LABEL_H3);
        addComponent(mt);

        CpuGraph chart = new CpuGraph();
        addComponent(chart);
    }
}
