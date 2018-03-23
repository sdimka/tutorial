package org.test.UI.historyWindow;

import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.*;
import com.vaadin.addon.charts.model.style.SolidColor;
import com.vaadin.addon.charts.model.style.Style;
import com.vaadin.addon.charts.model.style.Color;
import com.vaadin.addon.charts.model.style.Theme;
import com.vaadin.ui.Component;
import com.vaadin.ui.themes.ValoTheme;
import org.test.UI.AbstractVaadinChartExample;
import org.test.getData.DataProvider;

import java.time.LocalDate;
import java.time.ZoneOffset;

public class TempChart extends AbstractVaadinChartExample {

    private DataProvider dataProvider;

    private static final int ONE_HOUR = 60 * 60 * 1000;
    private static final SolidColor TRANSPARENT = new SolidColor(0, 0, 0, 0);
    private static final SolidColor LIGHT_BLUE = new SolidColor(68, 170, 213, 0.1);
    private static final SolidColor LIGHT_GRAY = new SolidColor("#606060");

    @Override
    public String getDescription() {
        return "Temperature chart";
    }

    @SuppressWarnings("deprecation")
    @Override
    protected Component getChart() {

        dataProvider = DataProvider.getInst();

        Chart chart = new Chart();
        chart.setHeight("300px");
       // chart.setWidth("100%");

        Configuration configuration = chart.getConfiguration();
        configuration.getChart().setType(ChartType.SPLINE);

        Style style1 = new Style();
        style1.setFontSize("12");
        configuration.getTitle().setStyle(style1);
        configuration.getTitle().setText("Temp during 24 hours");

        configuration.getxAxis().setType(AxisType.DATETIME);

        YAxis yAxis = configuration.getyAxis();
        yAxis.setTitle(new AxisTitle("Temp °C"));
        yAxis.setMin(-10);
        yAxis.setMinorGridLineWidth(0);
        yAxis.setGridLineWidth(0);

        // disable alternate grid color from Vaadin theme, disturbs
        // demonstrating plotbands
        yAxis.setAlternateGridColor(TRANSPARENT);

        Style style = new Style();
        style.setColor(LIGHT_GRAY);

        final PlotBand lightAir = new PlotBand();
        lightAir.setFrom(-10);
        lightAir.setTo(0);
        lightAir.setColor(LIGHT_BLUE);
        lightAir.setLabel(new Label("Cold"));
        lightAir.getLabel().setStyle(style);

        final PlotBand lightBreeze = new PlotBand();
        lightBreeze.setFrom(0);
        lightBreeze.setTo(15);
        lightBreeze.setColor(TRANSPARENT);
        lightBreeze.setLabel(new Label("Moderate"));
        lightBreeze.getLabel().setStyle(style);

        final PlotBand gentleBreeze = new PlotBand();
        gentleBreeze.setFrom(15);
        gentleBreeze.setTo(30);
        gentleBreeze.setColor(LIGHT_BLUE);
        gentleBreeze.setLabel(new Label("Normal"));
        gentleBreeze.getLabel().setStyle(style);

        yAxis.setPlotBands(lightAir, lightBreeze, gentleBreeze);

        configuration
                .getTooltip()
                .setFormatter(
                        "Highcharts.dateFormat('%e. %b %Y, %H:00', this.x) +': '+ this.y +' m/s'");

        PlotOptionsSpline plotOptions = new PlotOptionsSpline();
        configuration.setPlotOptions(plotOptions);
        plotOptions.setMarker(new Marker(false));
        plotOptions.getMarker().setLineWidth(4);
        plotOptions.getMarker().setSymbol(MarkerSymbolEnum.CIRCLE);
        States states = new States();
        Hover hover = new Hover(true);
        hover.setRadius(5);
        hover.setLineWidth(1);
        states.setHover(hover);
        plotOptions.getMarker().setStates(states);

        plotOptions.setPointInterval(ONE_HOUR);
        LocalDate date = LocalDate.now();
        plotOptions.setPointStart(date.atStartOfDay().toInstant(ZoneOffset.UTC));

        ListSeries ls = dataProvider.getTempData();
        ls.setName("Temp");
//        ls.setData(4.3, 5.1, 4.3, 5.2, 5.4, 4.7, 3.5, 4.1, 5.6, 7.4, 6.9, 7.1,
//                7.9, 7.9, 7.5, 6.7, 7.7, 7.7, 7.4, 7.0, 7.1, 5.8, 5.9, 7.4,
//                8.2, 8.5, 9.4, 8.1, 10.9, 10.4, 10.9, 12.4, 12.1, 9.5, 7.5,
//                7.1, 7.5, 8.1, 6.8, 3.4, 2.1, 1.9, 2.8, 2.9, 1.3, 4.4, 4.2,
//                3.0, 3.0);
        configuration.addSeries(ls);

        chart.drawChart(configuration);
        return chart;
    }
}
