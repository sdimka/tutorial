package org.test.UI;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Random;

import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.*;
import com.vaadin.addon.charts.model.style.SolidColor;
import com.vaadin.addon.charts.model.style.Style;
import com.vaadin.ui.Component;
import com.vaadin.ui.themes.ValoTheme;
import org.test.oshi.GetSystemInfo;

public class CpuGraph extends AbstractVaadinChartExample {

    @Override
    public String getDescription() {
        return "Spline Updating Each Seconds";
    }

    @Override
    protected Component getChart() {
        final Random random = new Random();

        GetSystemInfo sys = GetSystemInfo.getInst();

        final Chart chart = new Chart();
        chart.setWidth("500px");
        chart.setHeight("250px");

        Style style1 = new Style();
        style1.setFontSize("14");

        final Configuration configuration = chart.getConfiguration();
        configuration.getChart().setType(ChartType.SPLINE);
        configuration.getTitle().setText("Current CPU Load");
        configuration.getTitle().setStyle(style1);

        XAxis xAxis = configuration.getxAxis();
        xAxis.setType(AxisType.DATETIME);
        xAxis.setLabels(new Labels(false));
        xAxis.setTickPixelInterval(50);

        YAxis yAxis = configuration.getyAxis();
        yAxis.setTitle(new AxisTitle("%"));
        yAxis.setRange(100);
        yAxis.setPlotLines(new PlotLine(0, 1, new SolidColor("#808080")));

        configuration.getTooltip().setEnabled(false);
        configuration.getLegend().setEnabled(false);

        final DataSeries series = new DataSeries();
        series.setPlotOptions(new PlotOptionsSpline());
        series.setName("Random data");
        for (int i = -19; i <= 0; i++) {
            series.add(new DataSeriesItem(
                    LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
                            + i * 1000, random.nextDouble() * 100));
        }
        runWhileAttached(chart, new Runnable() {

            @Override
            public void run() {
                final long x = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
//                final double y = random.nextDouble();
                final double y = sys.getCpuLoad();
                series.add(new DataSeriesItem(x, y), true, true);
            }
        }, 3000, 1000);

        configuration.setSeries(series);

        chart.drawChart(configuration);
        return chart;
    }
}
