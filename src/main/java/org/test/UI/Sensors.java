package org.test.UI;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.navigator.View;
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


    public Sensors() {
        setMargin(true);

        Label h1 = new Label("Panels & Layout panels");
        h1.addStyleName(ValoTheme.LABEL_H1);
        addComponent(h1);

        HorizontalLayout row = new HorizontalLayout();
        row.addStyleName(ValoTheme.LAYOUT_HORIZONTAL_WRAPPING);
        row.setSpacing(true);
        addComponent(row);
        TestIcon testIcon = new TestIcon(60);

        Panel panel = new Panel("Normal");
        panel.setIcon(testIcon.get());
        panel.setContent(panelContent());
        row.addComponent(panel);




        final VerticalLayout layout = new VerticalLayout();
        final Label temp = new Label("Температура:");
        final Label press = new Label("Давление:");
        final Label humid = new Label("Влажность:");

        DataProvider dataProvider = DataProvider.getInst();

        Label label = new Label("Hello My New Suite!");
        Label temp_l = new Label("Press button to get");
        Label press_l = new Label("Press button to get");
        Label humid_l = new Label("Press button to get");
        //name.setCaption("Type your name here:");

        Button button = new Button("Update");

        Button button2 = new Button("Generate");

        button.addClickListener(e -> {
            Notification.show("It's work !!!");
            temp_l.setValue(String.format( "%.2f",dataProvider.getTemp()) + " °C");
            press_l.setValue(String.format( "%.2f",dataProvider.getPressure()) + " mmHg");
            humid_l.setValue(dataProvider.getHum() + "%");

//            layout.addComponent(new Label("Thanks " + name.getValue()
//                    + ", it works!"));
        });

        button2.addClickListener(e->{
            LocalDateTime time = LocalDateTime.now();
            dataProvider.updateInfo(time, 35, 40, 785);
            temp_l.setValue(String.format( "%.2f",dataProvider.getTemp()) + " °C");
            press_l.setValue(String.format( "%.2f",dataProvider.getPressure()) + " mmHg");
            humid_l.setValue(dataProvider.getHum() + "%");

        });
        layout.setMargin(true);
        layout.setSpacing(true);
        layout.addComponents(label,temp, temp_l, press, press_l, humid, humid_l, button, button2);


    }

    Component panelContent() {
        VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();
        layout.setMargin(true);
        layout.setSpacing(true);
        Label content = new Label(
                "Suspendisse dictum feugiat nisl ut dapibus. Mauris iaculis porttitor posuere. Praesent id metus massa, ut blandit odio.");
        content.setWidth("10em");
        layout.addComponent(content);
        Button button = new Button("Button");
        button.setSizeFull();
        layout.addComponent(button);
        return layout;
    }

//    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
//    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
//    public static class MyUIServlet extends VaadinServlet {
//    }
}
