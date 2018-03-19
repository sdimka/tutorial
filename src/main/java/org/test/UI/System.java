package org.test.UI;

import com.vaadin.navigator.View;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import org.test.getData.GetSysData;

public class System extends VerticalLayout implements View {

    GetSysData sys;

    public System(){
        sys = GetSysData.getInst();

        setMargin(true);

        Label h1 = new Label("Что у нас есть:");
        h1.addStyleName(ValoTheme.LABEL_H1);
        addComponent(h1);

        Label t = new Label(sys.GetCpuTemp() + " Темература");
        t.addStyleName(ValoTheme.LABEL_H3);
        addComponent(t);

        Label mt = new Label(sys.GetCpuTemp() + " Пам всего");
        mt.addStyleName(ValoTheme.LABEL_H3);
        addComponent(mt);
    }
}
