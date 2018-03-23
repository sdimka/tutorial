package org.test.UI.historyWindow;

import com.vaadin.ui.*;

public class PopUpWindow implements PopupView.Content {

    private final HorizontalLayout layout;
    private final TextField textField = new TextField("Minimized HTML content", "Show history");

    public PopUpWindow() {

        layout = new HorizontalLayout();
        layout.setMargin(false);

        TempChart tc = new TempChart();
        layout.addComponent(tc);

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