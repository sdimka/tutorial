package org.test.UI;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.test.getData.DataProvider;

public class Settings extends VerticalLayout implements View {

    private DataProvider dataProvider;

    public Settings() {

        dataProvider = DataProvider.getInst();

        Label h1 = new Label("Настройки:");
        h1.addStyleName(ValoTheme.LABEL_H2);
        addComponent(h1);

//        HorizontalLayout row = new HorizontalLayout();
//        row.addStyleName(ValoTheme.LAYOUT_HORIZONTAL_WRAPPING);
//        row.setSpacing(true);
//        addComponent(row);

        Panel panel = new Panel("Sensor status:");
        panel.setIcon(VaadinIcons.SUN_O);
        panel.addStyleName("color1");
        panel.setContent(panelSensors());
        addComponent(panel);


    }

    Component panelSensors() {
        VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();
        layout.setMargin(true);
        layout.setSpacing(true);

        Label  labelStat;
        if (dataProvider.isSensorUse()){
            labelStat = new Label("Sensor connected!");
            labelStat.addStyleName(ValoTheme.LABEL_SUCCESS);
        } else {
            labelStat = new Label("Sensor disconnected!");
            labelStat.addStyleName(ValoTheme.LABEL_FAILURE);
        }
        layout.addComponent(labelStat);

        Button button = new Button("Connect sensors");
        button.addStyleName(ValoTheme.BUTTON_PRIMARY);
        layout.addComponent(button);

        button.addClickListener(click ->{
            dataProvider.enableSensor();
            dataProvider.detTempData();
            labelStat.removeStyleName(ValoTheme.LABEL_FAILURE);
            labelStat.addStyleName(ValoTheme.LABEL_SUCCESS);
            labelStat.setValue("Sensor connected!");
            Notification.show("Worked!");
        });


        return layout;
    }
}
