package com.mycompany.sample;

import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import org.gillius.jfxutils.chart.JFXChartUtil;

import java.util.ArrayList;
import java.util.List;

public class Controller {
    private List<Point> data = new ArrayList();
    private static Integer iter = 0;

    @FXML
    private TableView<Point> valueTable;
    @FXML
    private TableColumn<Point, Integer> columnNumber;
    @FXML
    private TableColumn<Point, Double> columnX;
    @FXML
    private TableColumn<Point, Double> columnY;
    @FXML
    private LineChart<Number, Number> chart;
    @FXML
    private NumberAxis axisX;
    @FXML
    private NumberAxis axisY;

    @FXML
    private TextField newX;
    @FXML
    private TextField newY;

    @FXML
    private CheckBox lineBox;
    @FXML
    private CheckBox parabolaBox;
    @FXML
    private TextField lagrangeX;
    @FXML
    private TextField partLineX;
    @FXML
    private TextField lagrangeY;
    @FXML
    private TextField partLineY;
    @FXML
    private TextField lineK;
    @FXML
    private TextField lineC;
    @FXML
    private TextField parabolaA0;
    @FXML
    private TextField parabolaA1;
    @FXML
    private TextField parabolaA2;

    @FXML
    private TextField minX;
    @FXML
    private TextField maxX;
    @FXML
    private TextField minY;
    @FXML
    private TextField maxY;

    @FXML
    private Button actionButton;
    @FXML
    private CheckBox drawOnGraphics;
    private Point clicked;
    // if actionFlag=0 - add, actionFlag>0 - update, actionFlag<0 - delete
    private int actionFlag = 0;

    @FXML
    private CheckBox lagrangeBox;

    @FXML
    private CheckBox partLIneBox;

    public Controller() {
    }

    @FXML
    public void initialize() {
        columnNumber.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnX.setCellValueFactory(new PropertyValueFactory<>("x"));
        columnY.setCellValueFactory(new PropertyValueFactory<>("y"));

        axisX.setLowerBound(-50);
        axisX.setUpperBound(50);
        axisX.setTickUnit(10);

        handler();

        fillChart();
        refreshTable();
    }


    @FXML
    public void actionOnPoint() {
        Point inputPoint = new Point(++iter, Double.parseDouble(newX.getText()), Double.parseDouble(newY.getText()));
        if (actionFlag == 0) {
            addPoint(inputPoint);
        } else if (actionFlag > 0) {
            updatePoint(inputPoint);
        } else if (actionFlag < 0) {
            deletePoint();
        }
        fillChart();
        refreshTable();
    }

    private void addPoint(Point inputPoint) {
        data.add(inputPoint);
    }

    private void updatePoint(Point inputPoint) {
        data.set(data.indexOf(clicked), inputPoint);
        clicked = inputPoint;
    }

    private void deletePoint() {
        data.remove(data.indexOf(clicked));
        columnNumber.onEditCancelProperty();
    }


    @FXML
    public void refreshTable() {
        valueTable.setItems(FXCollections.observableList(data));
    }

    private void fillChart() {
        chart.getData().clear();
        XYChart.Series function = new XYChart.Series();
        function.setName(Utils.INPUT_DOTS);
        for (Point point : data) {
            function.getData().add(new XYChart.Data(point.getX(), point.getY()));
        }
       chart.getData().addAll(function);
    }

    @FXML
    public void build() {
        if (lineBox.isSelected()) {
            buildLine();
            lineK.setText(Utils.lineK);
            lineC.setText(Utils.lineC);
        }
        if (parabolaBox.isSelected()) {
            buildParabola();
            parabolaA0.setText(Utils.parabolaA0);
            parabolaA1.setText(Utils.parabolaA1);
            parabolaA2.setText(Utils.parabolaA2);
        }
        if (lagrangeBox.isSelected()) {
            buildLagrange();
        }
        if (partLIneBox.isSelected()) {
            buildPartLine();
        }

    }

    private void buildLine() {
        if (chart.getData().size() > 1) {
            chart.getData().remove(1, chart.getData().size() - 1);
        }
        chart.getData().addAll(Utils.approximatedLine(data));

    }

    private void buildParabola() {
        if (chart.getData().size() > 1) {
            chart.getData().remove(1, chart.getData().size() - 1);
        }
        chart.getData().addAll(Utils.approximatedParabola(data));
    }

    private void buildLagrange(){
        if (chart.getData().size() > 1) {
            chart.getData().remove(1, chart.getData().size() - 1);
        }
        chart.getData().addAll(Utils.interpolatedLagrange(data));
    }

    private void buildPartLine(){
        if (chart.getData().size() > 1) {
            chart.getData().remove(1, chart.getData().size() - 1);
        }
        chart.getData().addAll(Utils.interpolatedPartLine(data));
    }
    @FXML
    public void zoom() {
        axisX.setAutoRanging(false);
        axisY.setAutoRanging(false);
        axisX.setLowerBound(Double.parseDouble(minX.getText()));
        axisX.setUpperBound(Double.parseDouble(maxX.getText()));
        axisY.setLowerBound(Double.parseDouble(minY.getText()));
        axisY.setUpperBound(Double.parseDouble(maxY.getText()));
    }

    @FXML
    public void autoZoom() {
        axisX.setAutoRanging(true);
        axisY.setAutoRanging(true);
    }


    @FXML
    public void editTable() {
        System.out.println("in");
    }

    @FXML
    public void clear() {
        chart.getData().clear();
        valueTable.getItems().clear();
        iter = 0;
    }

    @FXML
    public void countYbyX() {
        double y;
        if (lineBox.isSelected()) {
            y = Double.parseDouble(lineK.getText() != null ? lineK.getText() : "1") * Double.parseDouble(lagrangeX.getText()) +
                    Double.parseDouble(lineC.getText() != null ? lineC.getText() : "0");
            lagrangeY.setText(y + "");
        }
        if (parabolaBox.isSelected()) {
            y = Double.parseDouble(parabolaA0.getText() != null ? parabolaA0.getText() : "1") * Math.pow(Double.parseDouble(lagrangeX.getText()), 2) +
                    Double.parseDouble(parabolaA1.getText() != null ? parabolaA1.getText() : "1") * Double.parseDouble(lagrangeX.getText()) +
                    Double.parseDouble(parabolaA2.getText() != null ? parabolaA2.getText() : "0");
            lagrangeX.setText(y + "");
            if (drawOnGraphics.isSelected()) {
                chart.getData().addAll(Utils.customParabola(parabolaA0.getText(), parabolaA1.getText(), parabolaA2.getText(), data));
            }
        }
        if(lagrangeBox.isSelected()){
            y=Utils.interpolatedLagrange(data,Double.parseDouble(lagrangeX.getText() != null ? lagrangeX.getText() : "1"));
            lagrangeY.setText(y + "");
        }
        if(partLIneBox.isSelected()){
            y=Utils.interpolatedPartLineCount(data,Double.parseDouble(partLineX.getText() != null ? partLineX.getText() : "1"));
            partLineY.setText(y + "");
        }
    }

    private void handler() {
        chart.setOnMouseClicked(event -> {
            System.out.println(axisY.getUpperBound()-axisX.getLowerBound());
            System.out.println(event.getY());

            addPoint(new Point(++iter, axisX.getValueForDisplay(event.getX()).doubleValue()-0.4, axisY.getValueForDisplay(event.getY()).doubleValue()+2));
            fillChart();
            refreshTable();
        });

        //Zooming works only via primary mouse button without ctrl held down
        JFXChartUtil.setupZooming(chart, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getButton() != MouseButton.PRIMARY || mouseEvent.isShortcutDown()) {
                    mouseEvent.consume();
                }
            }
        });

        columnX.setOnEditStart(editStartEventHandler());
        columnY.setOnEditStart(editStartEventHandler());
        columnX.setOnEditCancel(editEndEventHandler());
        columnY.setOnEditCancel(editEndEventHandler());
        columnNumber.setOnEditStart(deleteStartEventHandler());
        columnNumber.setOnEditCancel(deleteEndEventHandler());
    }

    private EventHandler<TableColumn.CellEditEvent<Point, Double>> editStartEventHandler() {
        return event -> {
            clicked = event.getRowValue();
            newX.setText(clicked.getX() + "");
            newY.setText(clicked.getY() + "");
            actionButton.setText("edit");
            actionFlag = 1;
        };
    }

    private EventHandler<TableColumn.CellEditEvent<Point, Double>> editEndEventHandler() {
        return event -> defaultParameters();
    }

    private EventHandler<TableColumn.CellEditEvent<Point, Integer>> deleteStartEventHandler() {
        return event -> {
            clicked = event.getRowValue();
            newX.setText(clicked.getX() + "");
            newY.setText(clicked.getY() + "");
            actionButton.setText("delete");
            actionFlag = -1;
        };
    }

    private EventHandler<TableColumn.CellEditEvent<Point, Integer>> deleteEndEventHandler() {
        return event -> defaultParameters();
    }

    private void defaultParameters() {
        newX.setText("");
        newY.setText("");
        clicked = null;
        actionButton.setText("add");
        actionFlag = 0;
    }
}
