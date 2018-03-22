package org.test.UI;

import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.PointClickEvent;
import com.vaadin.addon.charts.PointClickListener;
import com.vaadin.addon.charts.model.ChartType;
import com.vaadin.addon.charts.model.Configuration;
import com.vaadin.addon.charts.model.Cursor;
import com.vaadin.addon.charts.model.DataLabels;
import com.vaadin.addon.charts.model.DataSeries;
import com.vaadin.addon.charts.model.DataSeriesItem;
import com.vaadin.addon.charts.model.PlotOptionsPie;
import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;
import org.test.oshi.GetSystemInfo;

@SuppressWarnings("serial")
public class MemGraph extends AbstractVaadinChartExample {

    private static GetSystemInfo sys  = GetSystemInfo.getInst();

    @Override
    public String getDescription() {
        return "Pie chart";
    }

    @Override
    protected Component getChart() {

        Component ret = createChart();
        ret.setWidth("500px");
        ret.setHeight("250px");
        return ret;
    }

    public static Chart createChart() {
        Chart chart = new Chart(ChartType.PIE);

        Configuration conf = chart.getConfiguration();

        conf.setTitle("Mem usage");

        PlotOptionsPie plotOptions = new PlotOptionsPie();
        plotOptions.setCursor(Cursor.POINTER);
        DataLabels dataLabels = new DataLabels();
        dataLabels.setEnabled(true);
        dataLabels
                .setFormatter("'<b>'+ this.point.name +'</b>: '+ this.percentage.toFixed(2) +' % '");
        plotOptions.setDataLabels(dataLabels);
        conf.setPlotOptions(plotOptions);

        final DataSeries series = new DataSeries();
        //int totMem = (int)sys.getTotalMem();

        series.add(new DataSeriesItem("Free", sys.getFreeMem()));
        series.add(new DataSeriesItem("Used", sys.getUsedMem()));
//        DataSeriesItem chrome = new DataSeriesItem("Chrome", 12.8);
//        chrome.setSliced(true);
//        chrome.setSelected(true);
//        series.add(chrome);

        conf.setSeries(series);

        chart.addPointClickListener(new PointClickListener() {

            @Override
            public void onClick(PointClickEvent event) {
                Notification.show("Click: "
                        + series.get(event.getPointIndex()).getName());
            }
        });

        chart.drawChart(conf);

        return chart;
    }

}
