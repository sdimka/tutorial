package org.test.UI;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.shared.ui.slider.SliderOrientation;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.test.getData.DataProvider;
import org.test.servoManage.PCA9685Servo;

public class Settings extends VerticalLayout implements View {

    private DataProvider dataProvider;
    private PCA9685Servo pca9685Servo;

    public Settings() {

        dataProvider = DataProvider.getInst();
        pca9685Servo = PCA9685Servo.getInstance();

        setMargin(false);

        Label h1 = new Label("Настройки:");
        h1.addStyleName(ValoTheme.LABEL_H3);
        addComponent(h1);

//        HorizontalLayout row = new HorizontalLayout();
//        row.addStyleName(ValoTheme.LAYOUT_HORIZONTAL_WRAPPING);
//        row.setSpacing(true);
//        addComponent(row);

        Panel panel = new Panel("Sensor status:");
        panel.setIcon(VaadinIcons.BOLT);
        panel.addStyleName("color1");
        panel.setContent(panelSensors());
        addComponent(panel);

        Panel panel1 = new Panel("Servo mngmnt:");
        panel1.setIcon(VaadinIcons.AUTOMATION);
        panel1.addStyleName("color1");
        panel1.setContent(panelServo());
        addComponent(panel1);

    }

    private Component panelSensors() {
        VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();
     //   layout.setMargin(true);
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
            labelStat.removeStyleName(ValoTheme.LABEL_FAILURE);
            labelStat.addStyleName(ValoTheme.LABEL_SUCCESS);
            labelStat.setValue("Sensor connected!");
            Notification.show("Worked!");
        });


        return layout;
    }

    private Component panelServo() {
        VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();
        //   layout.setMargin(true);
        layout.setSpacing(true);

        Label  labelStat;
        if (pca9685Servo.isReal()){
            labelStat = new Label("Servos connected!");
            labelStat.addStyleName(ValoTheme.LABEL_SUCCESS);
        } else {
            labelStat = new Label("In fake mode!");
            labelStat.addStyleName(ValoTheme.LABEL_FAILURE);
        }
        layout.addComponent(labelStat);

        Slider slider = new Slider("First Servo");
        slider.setValue((double)pca9685Servo.GetPosition(0));
        slider.setWidth("200px");
        slider.addStyleName("color2");
        slider.setOrientation(SliderOrientation.HORIZONTAL);
        layout.addComponent(slider);

        slider.addValueChangeListener(event -> {Notification.show("Value changed:",
                String.valueOf(event.getValue()),
                Notification.Type.TRAY_NOTIFICATION);
                pca9685Servo.Move(0, event.getValue().intValue());
        });

        Slider slider1 = new Slider("Second Servo");
        slider1.setValue((double)pca9685Servo.GetPosition(1));
        slider1.setWidth("200px");
        slider1.addStyleName("color2");
        slider1.setOrientation(SliderOrientation.HORIZONTAL);
        layout.addComponent(slider1);

        slider1.addValueChangeListener(event -> {Notification.show("Value changed:",
                String.valueOf(event.getValue()),
                Notification.Type.TRAY_NOTIFICATION);
            pca9685Servo.Move(1, event.getValue().intValue());
        });

        return layout;
    }
}
