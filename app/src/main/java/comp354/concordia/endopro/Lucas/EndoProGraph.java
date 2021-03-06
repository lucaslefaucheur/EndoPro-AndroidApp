package comp354.concordia.endopro.Lucas;

import android.graphics.Color;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import comp354.concordia.endopro.Common.EndoProWorkout;
import comp354.concordia.endopro.Common.User;

public class EndoProGraph {

    ArrayList<EndoProWorkout> workouts;
    GraphView graph;

    double min_speed_average = 0;
    double max_speed_average = 0;
    double min_distance = 0;
    double max_distance = 0;

    public EndoProGraph(GraphView graph, User user) {
        this.graph = graph;
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setYAxisBoundsManual(true);

        workouts = user.getWorkouts_filtered();

        for (int i = 0; i < workouts.size(); i++) {
            if (workouts.get(i).getSpeedAverage() > max_speed_average)
                max_speed_average = workouts.get(i).getSpeedAverage();
            if (workouts.get(i).getDistance() > max_distance)
                max_distance = workouts.get(i).getDistance();
        }

        min_speed_average = max_speed_average;
        min_distance = max_distance;

        for (int i = 0; i < workouts.size(); i++) {
            if (workouts.get(i).getSpeedAverage() < min_speed_average)
                min_speed_average = workouts.get(i).getSpeedAverage();
            if(workouts.get(i).getDistance() < min_distance)
                min_distance = workouts.get(i).getDistance();
        }

        min_distance -= 10;
        min_speed_average -= 2;
        max_distance += 10;
        max_speed_average += 2;
    }


    public void createHistorySpeedGraph() {
        LineGraphSeries<DataPoint> series1 = new LineGraphSeries<DataPoint>();
        series1.setDrawDataPoints(true);

        double x1 = 0, y1;

        for (int i = 0; i < workouts.size(); i++) {
            x1++;
            y1 = workouts.get(i).getSpeedAverage();
            series1.appendData(new DataPoint(x1, y1), true, workouts.size());
        }

        LineGraphSeries<DataPoint> series2 = new LineGraphSeries<DataPoint>();
        series2.setColor(Color.GREEN);

        double x2 = 0.0, y2 = 0.0;

        for (int i = 0; i < workouts.size(); i++) {
            x2++;

            double count = 0.0;
            y2 = 0.0;
            for (int j = 0; j < 10; j++) {
                if (i - j >= 0) {
                    count++;
                    y2 += workouts.get(i - j).getSpeedAverage();
                }
            }
            y2 /= count;

            series2.appendData(new DataPoint(x2, y2), true, workouts.size());
        }

        graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX) {
                    return null;
                } else {
                    return super.formatLabel(value, isValueX) + " km/h";
                }
            }
        });

        graph.removeAllSeries();
        graph.setTitle("History of all average speeds");
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(x1+1);
        graph.getViewport().setMinY(min_speed_average);
        graph.getViewport().setMaxY(max_speed_average);

        graph.addSeries(series1);
        graph.addSeries(series2);
    }

    public void createYearlySpeedGraph() {
        BarGraphSeries<DataPoint> series = new BarGraphSeries<DataPoint>();

        double [] sum = new double [5];
        Arrays.fill(sum, 0.0);
        int [] quantity = new int [5];
        Arrays.fill(quantity, 0);

        for (int i = 0; i < workouts.size(); i++) {
            int year = Integer.parseInt(workouts.get(i).getStartTime().substring(0, 4));
            if (year <= 2018 && year >= 2014) {
                sum[year-2014] += workouts.get(i).getSpeedAverage();
                quantity[year-2014] ++;
            }
        }

        for (int i = 0; i < 5; i++) {
            series.appendData(new DataPoint((i+2014), (sum[i]/quantity[i])), true, 5);
        }

        graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX) {
                    return super.formatLabel(value, isValueX);
                } else {
                    return super.formatLabel(value, isValueX) + " km/h";
                }
            }
        });

        graph.removeAllSeries();
        graph.setTitle("Yearly averages of average speeds");
        graph.getViewport().setMinX(2013);
        graph.getViewport().setMaxX(2019);
        graph.getViewport().setMinY(min_speed_average);
        graph.getViewport().setMaxY(max_speed_average);
        graph.addSeries(series);
    }

    public void createMonthlySpeedGraph() {
        BarGraphSeries<DataPoint> series = new BarGraphSeries<DataPoint>();

        double [] sum = new double [12];
        Arrays.fill(sum, 0.0);
        int [] quantity = new int [12];
        Arrays.fill(quantity, 0);

        for (int i = 0; i < workouts.size(); i++) {
            int month = Integer.parseInt(workouts.get(i).getStartTime().substring(5, 7));
            int year = Integer.parseInt(workouts.get(i).getStartTime().substring(0, 4));

            if (year == 2018 && month >= 1 && month <=12) {
                sum[month-1] += workouts.get(i).getSpeedAverage();
                quantity[month-1] ++;
            }
        }

        for (int i = 0; i < 12; i++) {
            series.appendData(new DataPoint((i+1), (sum[i]/quantity[i])), true, 12);
        }

        graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX) {
                    if (value == 1) return "JAN";
                    else if (value == 2) return "FEB";
                    else if (value == 3) return "MAR";
                    else if (value == 4) return "APR";
                    else if (value == 5) return "MAY";
                    else if (value == 6) return "JUN";
                    else if (value == 7) return "JUL";
                    else if (value == 8) return "AUG";
                    else if (value == 9) return "SEP";
                    else if (value == 10) return "OCT";
                    else if (value == 11) return "NOV";
                    else if (value == 12) return "DEC";
                    else return null;

                } else {
                    return super.formatLabel(value, isValueX) + " km/h";
                }
            }
        });

        graph.removeAllSeries();
        graph.setTitle("Monthly averages of average speeds");
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(13);
        graph.getViewport().setMinY(min_speed_average);
        graph.getViewport().setMaxY(max_speed_average);
        graph.addSeries(series);
    }

    public void createHistoryDistanceGraph() {
        LineGraphSeries<DataPoint> series1 = new LineGraphSeries<DataPoint>();
        series1.setDrawDataPoints(true);

        double x1 = 0, y1;

        for (int i = 0; i < workouts.size(); i++) {
            x1++;
            y1 = workouts.get(i).getDistance();
            series1.appendData(new DataPoint(x1, y1), true, workouts.size());
        }

        LineGraphSeries<DataPoint> series2 = new LineGraphSeries<DataPoint>();
        series2.setColor(Color.GREEN);

        double x2 = 0.0, y2 = 0.0;

        for (int i = 0; i < workouts.size(); i++) {
            x2++;

            double count = 0.0;
            y2 = 0.0;
            for (int j = 0; j < 10; j++) {
                if (i - j >= 0) {
                    count++;
                    y2 += workouts.get(i - j).getDistance();
                }
            }
            y2 /= count;

            series2.appendData(new DataPoint(x2, y2), true, workouts.size());
        }

        graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX) {
                    return null;
                } else {
                    return super.formatLabel(value, isValueX) + " km";
                }
            }
        });

        graph.removeAllSeries();
        graph.setTitle("History of all distances");
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(x1+1);
        graph.getViewport().setMinY(min_distance);
        graph.getViewport().setMaxY(max_distance);
        graph.addSeries(series1);
        graph.addSeries(series2);
    }

    public void createYearlyDistanceGraph() {
        BarGraphSeries<DataPoint> series = new BarGraphSeries<DataPoint>();

        double [] sum = new double [5];
        Arrays.fill(sum, 0.0);
        int [] quantity = new int [5];
        Arrays.fill(quantity, 0);

        for (int i = 0; i < workouts.size(); i++) {
            int year = Integer.parseInt(workouts.get(i).getStartTime().substring(0, 4));
            if (year <= 2018 && year >= 2014) {
                sum[year-2014] += workouts.get(i).getDistance();
                quantity[year-2014] ++;
            }
        }

        for (int i = 0; i < 5; i++) {
            series.appendData(new DataPoint((i+2014), (sum[i]/quantity[i])), true, 5);
        }

        graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX) {
                    return super.formatLabel(value, isValueX);
                } else {
                    return super.formatLabel(value, isValueX) + " km";
                }
            }
        });

        graph.removeAllSeries();
        graph.setTitle("Yearly averages of distances");
        graph.getViewport().setMinX(2013);
        graph.getViewport().setMaxX(2019);
        graph.getViewport().setMinY(min_distance);
        graph.getViewport().setMaxY(max_distance);
        graph.addSeries(series);
    }

    public void createMonthlyDistanceGraph() {
        BarGraphSeries<DataPoint> series = new BarGraphSeries<DataPoint>();

        double [] sum = new double [12];
        Arrays.fill(sum, 0.0);
        int [] quantity = new int [12];
        Arrays.fill(quantity, 0);

        for (int i = 0; i < workouts.size(); i++) {
            int month = Integer.parseInt(workouts.get(i).getStartTime().substring(5, 7));
            int year = Integer.parseInt(workouts.get(i).getStartTime().substring(0, 4));

            if (year == 2018 && month >= 1 && month <=12) {
                sum[month-1] += workouts.get(i).getDistance();
                quantity[month-1] ++;
            }
        }

        for (int i = 0; i < 12; i++) {
            series.appendData(new DataPoint((i+1), (sum[i]/quantity[i])), true, 12);
        }

        graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX) {
                    if (value == 1) return "JAN";
                    else if (value == 2) return "FEB";
                    else if (value == 3) return "MAR";
                    else if (value == 4) return "APR";
                    else if (value == 5) return "MAY";
                    else if (value == 6) return "JUN";
                    else if (value == 7) return "JUL";
                    else if (value == 8) return "AUG";
                    else if (value == 9) return "SEP";
                    else if (value == 10) return "OCT";
                    else if (value == 11) return "NOV";
                    else if (value == 12) return "DEC";
                    else return null;

                } else {
                    return super.formatLabel(value, isValueX) + " km";
                }
            }
        });

        graph.removeAllSeries();
        graph.setTitle("Monthly averages of distances");
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(13);
        graph.getViewport().setMinY(min_distance);
        graph.getViewport().setMaxY(max_distance);
        graph.addSeries(series);
    }

}
