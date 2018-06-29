package org.test.UI.historyWindow;

import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.*;
import com.vaadin.addon.charts.model.style.SolidColor;
import com.vaadin.addon.charts.model.style.Style;
import com.vaadin.ui.Component;
import org.test.getData.DataProvider;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public class PressChart extends ChartInterface  {

    private DataProvider dataProvider;

    private static final int ONE_HOUR = 60 * 60 * 1000;
    private static final int HALF_HOUR = 30 * 60 * 1000;
    private static final SolidColor TRANSPARENT = new SolidColor(0, 0, 0, 0);
    private static final SolidColor LIGHT_BLUE = new SolidColor(68, 170, 213, 0.1);
    private static final SolidColor LIGHT_GRAY = new SolidColor("#606060");

    @Override
    public String getDescription() {
        return "Pressure chart";
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
        style1.setFontSize("14");
        configuration.getTitle().setStyle(style1);
        configuration.getTitle().setText("Pressure during 24 hours");

        configuration.getxAxis().setType(AxisType.DATETIME);

        YAxis yAxis = configuration.getyAxis();
        yAxis.setTitle(new AxisTitle("Pressure mmHg"));
        yAxis.setMin(720);
        yAxis.setMinorGridLineWidth(0);
        yAxis.setGridLineWidth(0);

        // disable alternate grid color from Vaadin theme, disturbs
        // demonstrating plotbands
        yAxis.setAlternateGridColor(TRANSPARENT);

        Style style = new Style();
        style.setColor(LIGHT_GRAY);

        final PlotBand lightAir = new PlotBand();
        lightAir.setFrom(720);
        lightAir.setTo(750);
        lightAir.setColor(LIGHT_BLUE);
        lightAir.setLabel(new Label("Low"));
        lightAir.getLabel().setStyle(style);

        final PlotBand lightBreeze = new PlotBand();
        lightBreeze.setFrom(750);
        lightBreeze.setTo(755);
        lightBreeze.setColor(TRANSPARENT);
        lightBreeze.setLabel(new Label("Normal"));
        lightBreeze.getLabel().setStyle(style);

        final PlotBand gentleBreeze = new PlotBand();
        gentleBreeze.setFrom(755);
        gentleBreeze.setTo(790);
        gentleBreeze.setColor(LIGHT_BLUE);
        gentleBreeze.setLabel(new Label("Hi"));
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
        plotOptions.setPointStart(date.minusDays(1).toInstant(OffsetDateTime.now().getOffset()));

        ListSeries ls = dataProvider.get24HData("press");
        ls.setName("Pressure");

        configuration.addSeries(ls);

        chart.drawChart(configuration);
        return chart;
    }
}

