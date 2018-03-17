package org.test.UI;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.icons.*;
import com.vaadin.navigator.View;
import com.vaadin.server.Resource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.test.getData.DataProvider;
import org.test.getData.GetSensData;

import java.time.LocalDateTime;


/**
 * T
 *
 *
 *
 *
 */

@Title("Sensor Info")
@Theme("mytheme")
public class Sensors extends VerticalLayout implements View {

    DataProvider dataProvider;
    public Sensors() {

        dataProvider = DataProvider.getInst();


        setMargin(true);

        Label h1 = new Label("Panels & Layout panels");
        h1.addStyleName(ValoTheme.LABEL_H1);
        addComponent(h1);

        HorizontalLayout row = new HorizontalLayout();
        row.addStyleName(ValoTheme.LAYOUT_HORIZONTAL_WRAPPING);
        row.setSpacing(true);
        addComponent(row);
        TestIcon testIcon = new TestIcon(60);

        Panel panel = new Panel("Температура");
        panel.setIcon(VaadinIcons.SUN_O);
        panel.addStyleName("color1");
        panel.setContent(panelContentTemp());
        row.addComponent(panel);

        Panel panel1 = new Panel("Давление");
        panel1.setIcon(VaadinIcons.CONTROLLER);
        panel1.setContent(panelContentPress());
        row.addComponent(panel1);

        Panel panel2 = new Panel("Влажность");
        panel2.setIcon(VaadinIcons.DROP);
        panel2.setContent(panelContentHum());
        row.addComponent(panel2);
    }

    Component panelContentTemp() {
        VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();
        layout.setMargin(true);
        layout.setSpacing(true);
        Label content = new Label(
                String.format( "%.2f", dataProvider.getTemp()) + " °C");
        content.setWidth("10em");
        layout.addComponent(content);
//        Button button = new Button("Button");
//        button.setSizeFull();
//        layout.addComponent(button);
        return layout;
    }

    Component panelContentHum() {
        VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();
        layout.setMargin(true);
        layout.setSpacing(true);
        Label content = new Label(
                dataProvider.getHum() + "%");
        content.setWidth("10em");
        layout.addComponent(content);
        //Button button1 = new Button("Button1");
        //button1.setSizeFull();
        //layout.addComponent(button1);
        return layout;
    }
    Component panelContentPress() {
        VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();
        layout.setMargin(true);
        layout.setSpacing(true);
        Label content = new Label(
                String.format( "%.2f",dataProvider.getPressure()) + " mmHg");
        content.setWidth("10em");
        layout.addComponent(content);
//        Button button2 = new Button("Button2");
//        button2.setSizeFull();
//        layout.addComponent(button2);
        return layout;
    }
}
