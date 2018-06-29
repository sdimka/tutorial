package org.test.UI;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.icons.*;
import com.vaadin.navigator.View;
import com.vaadin.server.*;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.test.UI.historyWindow.PopUpWindow;
import org.test.UI.historyWindow.PressChart;
import org.test.UI.historyWindow.TempChart;
import org.test.getData.DataProvider;
import org.test.getData.GetSensData;

import java.io.File;
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

        setMargin(false);

        Label h1 = new Label("Сводки с полей:");
        h1.addStyleName(ValoTheme.LABEL_H3);
        addComponent(h1);

        HorizontalLayout row = new HorizontalLayout();
        row.addStyleName(ValoTheme.LAYOUT_HORIZONTAL_WRAPPING);
        row.setSpacing(true);
        addComponent(row);

        Panel panel = new Panel("Температура");
        panel.setIcon(VaadinIcons.SUN_O);
        panel.addStyleName("color1");
        panel.setContent(panelContentTemp());
        row.addComponent(panel);

        Panel panel1 = new Panel("Давление");
        panel1.setIcon(VaadinIcons.CONTROLLER);
        panel1.addStyleName("color2");
        panel1.setContent(panelContentPress());
        row.addComponent(panel1);

        Panel panel2 = new Panel("Влажность");
        panel2.setIcon(VaadinIcons.DROP);
        panel2.addStyleName("color3");
        panel2.setContent(panelContentHum());
        row.addComponent(panel2);

    }

    Component panelContentTemp() {
        VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();
        layout.setMargin(true);
        layout.setSpacing(true);

        Page.Styles styles = Page.getCurrent().getStyles();
        styles.add(".mytheme .v-caption-inline-icon img.v-icon {\n" +
                "\tleft: 1px;\n" +
                "\tbottom: 1px;\n" +
                "}");

//        Label content = new Label(
//                String.format( "In:  %.2f °C", dataProvider.getTemp()));
//        content.setStyleName(ValoTheme.LABEL_BOLD);
//        content.setWidth("10em");
//        content.addStyleName("left");
//        content.setIcon(new ThemeResource("icons/1a.png"));
//        layout.addComponent(content);

        TextField tf = new TextField();
        tf.setValue(String.format(" %.2f °C", dataProvider.getTemp()));
        tf.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
        tf.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
        tf.setIcon(new ThemeResource("icons/2a.png"));
        tf.setReadOnly(true);
        tf.setWidth("10em");
        layout.addComponent(tf);

//        Label temp2 = new Label(
//                String.format("Out: %.2f °C", dataProvider.getTemp2()));
//        temp2.setStyleName(ValoTheme.LABEL_BOLD);
//        temp2.setWidth("10em");
//        layout.addComponent(temp2);

        TextField tf1 = new TextField();
        tf1.setValue(String.format(" %.2f °C", dataProvider.getTemp2()));
        tf1.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
        tf1.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
        tf1.setIcon(new ThemeResource("icons/1a.png"));
        tf1.setReadOnly(true);
        tf1.setWidth("10em");
        layout.addComponent(tf1);

        PopupView sample = new PopupView(new PopUpWindow(TempChart.class));
        layout.addComponent(sample);
        sample.setHideOnMouseOut(true);
//        sample.setCaption("Hide on mouse-out");

        return layout;
    }

    Component panelContentHum() {
        VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();
//        layout.setMargin(true);
        layout.setSpacing(true);
        Label content = new Label(
                String.format("%.2f %%",dataProvider.getHum()));
        content.setStyleName(ValoTheme.LABEL_BOLD);
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
//        layout.setMargin(true);
        layout.setSpacing(true);
        Label content = new Label(
                String.format("%.2f",dataProvider.getPressure()) + " mmHg");
        content.setStyleName(ValoTheme.LABEL_BOLD);
        content.setWidth("10em");
        layout.addComponent(content);

        PopupView sample = new PopupView(new PopUpWindow(PressChart.class));
        layout.addComponent(sample);
        sample.setHideOnMouseOut(true);

        return layout;
    }
}
