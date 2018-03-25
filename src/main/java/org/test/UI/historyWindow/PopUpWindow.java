package org.test.UI.historyWindow;

import com.vaadin.ui.*;

public class PopUpWindow implements PopupView.Content {

    private final HorizontalLayout layout;
    private final TextField textField = new TextField("Minimized HTML content", "Show history");

    public PopUpWindow(Class<?> chart) {
        layout = new HorizontalLayout();
        layout.setMargin(false);

        ChartInterface o = null;
        try {
            o = (ChartInterface) chart.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        // ChartInterface tc = new TempChart();
        layout.addComponent(o);

    }

    @Override
    public final Component getPopupComponent() {
        return layout;
    }

    @Override
    public final String getMinimizedValueAsHTML() {
        return textField.getValue();
    }
}