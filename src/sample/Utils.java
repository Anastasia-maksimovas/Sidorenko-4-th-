package sample;

import javafx.scene.chart.XYChart;

import java.util.List;

public class Utils {

    public static final String INPUT_DOTS = "input dots";
    public static final String APROXIMATED_LINE = "approximated line";
    public static final String APROXIMATED_PARABOLA = "approximated parabola";
    public static String lineX = "";
    public static String parabolaX = "";
    public static String lineY = "";
    public static String parabolaY = "";
    public static String lineK = "";
    public static String lineC = "";
    public static String parabolaA0 = "";
    public static String parabolaA1 = "";
    public static String parabolaA2 = "";

    public static XYChart.Series approximatedLine(List<Point> data) {
        double sumXY = 0;
        double sumXi = 0;
        double sumYi = 0;
        double sumXi2 = 0;
        for (Point point : data) {
            sumXY += point.getX() * point.getY();
            sumXi += point.getX();
            sumYi += point.getY();
            sumXi2 += Math.pow(point.getX(), 2);
        }

        double delt = data.size() * sumXi2 - sumXi * sumXi;
        double delt0 = sumYi * sumXi2 - sumXY * sumXi;
        double delt1 = data.size() * sumXY - sumXi * sumYi;

        double c = delt0 / delt;
        double k = delt1 / delt;

        XYChart.Series function = new XYChart.Series();
        function.setName(APROXIMATED_LINE);
        for (Point point : data) {
            function.getData().add(new XYChart.Data(point.getX(), k * point.getX() + c));
        }
        lineK = k + "";
        lineC = c + "";
        return function;
    }

    public static XYChart.Series approximatedParabola(List<Point> data) {
        double sumXY = 0;
        double sumXi = 0;
        double sumYi = 0;
        double sumXi2 = 0;
        double sumXi3 = 0;
        double sumXi4 = 0;
        double sumXi2Y = 0;
        for (Point point : data) {
            sumXY += point.getX() * point.getY();
            sumXi += point.getX();
            sumYi += point.getY();
            sumXi2 += Math.pow(point.getX(), 2);
            sumXi3 += Math.pow(point.getX(), 3);
            sumXi4 += Math.pow(point.getX(), 4);
            sumXi2Y += Math.pow(point.getX(), 2) * point.getY();
        }

        double delt = data.size() * sumXi2 * sumXi4 + sumXi * sumXi3 * sumXi2 + sumXi * sumXi3 * sumXi2 - sumXi2 *
                sumXi2 * sumXi2 - sumXi3 * sumXi3 * data.size() - sumXi * sumXi * sumXi4;
        double delt1 = sumYi * sumXi2 * sumXi4 + sumXY * sumXi3 * sumXi2 + sumXi * sumXi3 * sumXi2Y - sumXi2 * sumXi2 *
                sumXi2Y - sumXi3 * sumXi3 * sumYi - sumXi * sumXY * sumXi4;
        double delt2 = data.size() * sumXY * sumXi4 + sumYi * sumXi3 * sumXi2 + sumXi * sumXi2Y * sumXi2 - sumXi2 *
                sumXY * sumXi2 - sumYi * sumXi * sumXi4 - sumXi3 * sumXi2Y * data.size();
        double delt3 = data.size() * sumXi2 * sumXi2Y + sumXi * sumXi3 * sumYi + sumXi * sumXY * sumXi2 - sumYi *
                sumXi2 * sumXi2 - sumXY * sumXi3 * data.size() - sumXi * sumXi * sumXi2Y;

        double a0 = delt3 / delt;
        double a1 = delt2 / delt;
        double a2 = delt1 / delt;
        XYChart.Series function = new XYChart.Series();
        function.setName(APROXIMATED_PARABOLA);
        for (Point point : data) {
            function.getData().add(new XYChart.Data(point.getX(), a0 * Math.pow(point.getX(), 2) + a1 * point.getX() + a2));
        }
        parabolaA0 = a0 + "";
        parabolaA1 = a1 + "";
        parabolaA2 = a2 + "";
        return function;
    }

    public static XYChart.Series customLine(String k, String c, List<Point> data) {
        XYChart.Series function = new XYChart.Series();
        function.setName("line");
        for (Point point : data) {
            function.getData().add(new XYChart.Data(point.getX(),
                    Double.parseDouble(k) * point.getX() + Double.parseDouble(c)));
        }
        return function;
    }

    public static XYChart.Series customParabola(String a0, String a1, String a2, List<Point> data) {
        XYChart.Series function = new XYChart.Series();
        function.setName("parabola");
        double min = getMinX(data);
        double max = getMaxX(data);
        double step = (max - min) / 30;
        for (double d = min; d <= max+step; d += step) {
            function.getData().add(new XYChart.Data(d, Double.parseDouble(a0) * Math.pow(d, 2) +
                    Double.parseDouble(a1) * d + Double.parseDouble(a2)));
        }
        return function;
    }

    private static double getMinX(List<Point> data) {
        double min = data.get(0).getX();
        for (Point point : data) {
            if (point.getX() < min) {
                min = point.getX();
            }
        }
        return min;
    }

    private static double getMaxX(List<Point> data) {
        double max = data.get(0).getX();
        for (Point point : data) {
            if (point.getX() > max) {
                max = point.getX();
            }
        }
        return max;
    }

    public static XYChart.Series interpolatedLagrange(List<Point> data) {
        return interpolationLagrangeCount(data);
    }
    private static XYChart.Series interpolationLagrangeCount(List<Point> data) {
        double min = getMinX(data);
        double max = getMaxX(data);
        double step = (max - min) / 30;
        XYChart.Series function = new XYChart.Series();
        for (double x = min; x <= max+step; x += step) {
            function.getData().add(new XYChart.Data(x,interpolatedLagrange(data, x)));
        }
        function.setName("lagrange");
        return function;
    }
    public static double interpolatedLagrange(List<Point> data, double x) {
        double y = 0;
        for (int i = 0; i < data.size(); i++) {
            double[] l = new double[data.size()];
            l[i] = 1;
            for (int j = 0; j < data.size(); j++) {
                if (i == j) continue;
                l[i] *= (x - data.get(j).getX()) / (data.get(i).getX() - data.get(j).getX());
            }
            y += data.get(i).getY() * l[i];
        }
        return y;
    }

    public static XYChart.Series interpolatedPartLine(List<Point> data){
        double min = getMinX(data);
        double max = getMaxX(data);
        double step = (max - min) / 30;
        XYChart.Series function = new XYChart.Series();
        for (double x = min+step; x <= max; x += step) {
            function.getData().add(new XYChart.Data(x,interpolatedPartLineCount(data, x)));
        }
        return function;
    }

    public static double interpolatedPartLineCount(List<Point> data,double x){
        Point prev=findPrevious(data,x);
        Point next=findNext(data,x);
        double a1=(next.getY()-prev.getY())/(next.getX()-prev.getX());
        double a0=prev.getY()-a1*prev.getX();
        System.out.println(a0+" "+a1+" "+x);
        return a0+a1*x;
    }

    private static Point findPrevious(List<Point> data,double x){
        Point prev=data.get(0);
        for(Point point : data) {
            if (point.getX() <= x) {
                prev = point;
                break;
            }
        }
        for(Point point : data){
            System.out.println((x-prev.getX())+" "+(x-point.getX()));
            if(point.getX()<=x && (x-prev.getX())>(x-point.getX())){
                prev=point;
            }
        }
        System.out.println(prev.getX());
        return prev;
    }
    private static Point findNext(List<Point> data,double x){
        Point next=data.get(0);
        for(Point point : data) {
            if (point.getX() >= x) {
                next = point;
                break;
            }
        }
        for(Point point : data){
            System.out.println((next.getX()-x)+" "+(point.getX()-x));
            if(point.getX()>=x && (next.getX()-x)>(point.getX()-x)){
                next=point;
            }
        }
        System.out.println(next.getX());
        return next;
    }
}
