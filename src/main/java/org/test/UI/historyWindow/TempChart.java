package org.test.UI.historyWindow;

import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.*;
import com.vaadin.addon.charts.model.style.SolidColor;
import com.vaadin.addon.charts.model.style.Style;
import com.vaadin.ui.Component;
import org.test.getData.DataProvider;

import java.time.*;

public class TempChart extends ChartInterface  {

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
        LocalDateTime date = LocalDateTime.now();
//        plotOptions.setPointStart(date.atStartOfDay().toInstant(ZoneOffset.UTC));

        plotOptions.setPointStart(date.minusDays(1).toInstant(OffsetDateTime.now().getOffset()));
        ListSeries ls = dataProvider.get24HData("temp");
        ls.setName("Temp");
        configuration.addSeries(ls);

        ListSeries ls2 = dataProvider.get24HData("temp2");
        ls2.setName("Temp-outside");
        configuration.addSeries(ls2);

        chart.drawChart(configuration);
        return chart;
    }
}
