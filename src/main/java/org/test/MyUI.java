package org.test;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.*;

/**
 * This UI is the application entry point. A UI may either represent a browser window 
 * (or tab) or some part of an HTML page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be 
 * overridden to add component to the user interface and initialize non-component functionality.
 */

@Title("Sensor Info")
@Theme("mytheme")
public class MyUI extends UI {

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        final VerticalLayout layout = new VerticalLayout();
        final Label temp = new Label("Температура:");
        final Label press = new Label("Давление:");
        final Label humid = new Label("Влажность:");
        GetSensData gsd = GetSensData.getInst();
        Label label = new Label("Hello My New Suite!");

        Label temp_l = new Label("Press button to get");
        Label press_l = new Label("Press button to get");
        Label humid_l = new Label("Press button to get");
        //name.setCaption("Type your name here:");

        Button button = new Button("Update");
        button.addClickListener(e -> {
            Notification.show("It's work !!!");
            temp_l.setValue(String.format( "%.2f",gsd.getTemp()) + " °C");
            press_l.setValue(String.format( "%.2f",gsd.getPressure()) + " mmHg");
            humid_l.setValue(gsd.getHum() + "%");

//            layout.addComponent(new Label("Thanks " + name.getValue()
//                    + ", it works!"));
        });
        layout.setMargin(true);
        layout.setSpacing(true);
        layout.addComponents(label,temp, temp_l, press, press_l, humid, humid_l, button);
        
        setContent(layout);
    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}
