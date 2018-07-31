package datareader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import deviceManager.DeviceManager;
import deviceManager.GenericDevice;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import observer.DeviceListener;
import observer.RiskObserver;
import riskAssessment.Risk;
import util.Configuration;

public class DynamicLineChart extends Application{

    protected static final int CONFIGURATION_1_INDEX = 0;
	ObservableList<XYChart.Data<String, String>> xyList1 = FXCollections.observableArrayList();
    ObservableList<XYChart.Data<String, Integer>> xyList2 = FXCollections.observableArrayList();

    ObservableList<String> myXaxisCategories = FXCollections.observableArrayList();

    int i;
    private Task<Date> task;
    private LineChart<String, String> lineChart;
    private TextArea riskAlarmsTextArea;
    private TextArea functionalAlarmsTextArea;
    private TextArea controlActionsTextField;
    private XYChart.Series xySeries1;
    private XYChart.Series xySeries2;
    private CategoryAxis xAxis;
    private int lastObservedSize;
    private ListView<String> configurationListView = new ListView<String>();
    DeviceListener deviceListener = null;
    RiskObserver riskObserver = null;
    
	@Override public void start(Stage stage) {
    	
    	// Use a border pane as the root for scene
    	BorderPane border = new BorderPane();
    	
        xyList1.addListener((ListChangeListener<XYChart.Data<String, String>>) change -> {
            if (change.getList().size() - lastObservedSize > 10) {
                lastObservedSize += 10;
                xAxis.getCategories().remove(0, 10);
            }
        });
        

        stage.setTitle("Line Chart Sample");
        xAxis = new CategoryAxis();
        xAxis.setLabel("Time");

        final CategoryAxis yAxis = new CategoryAxis();
        lineChart = new LineChart<>(xAxis,yAxis);

        lineChart.setTitle("Risk");
        lineChart.setAnimated(false);

        task = new Task<Date>() {
            @Override
            protected Date call() throws Exception {
            	int count = 0;
                while (true) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException iex) {
                        Thread.currentThread().interrupt();
                    }

                    if (isCancelled()) {
                        break;
                    }

                    updateValue(new Date());
                    
//					if (count > 10) {
//						lineChart.getData().clear();
//						Thread.sleep(1000);
//						count = 0;
//					} else {
//						count += 1;
//					}
                    
                }
                return new Date();
            }
        };

        task.valueProperty().addListener(new ChangeListener<Date>() {
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
            int count = 0;

            @Override
            public void changed(ObservableValue<? extends Date> observableValue, Date oldDate, Date newDate) {

                String strDate = dateFormat.format(newDate);
                myXaxisCategories.add(strDate);
                
//                if (count > 10) {
//                	myXaxisCategories.clear();
//                	count = 0;
//                }
//                count+=1;
                
                if( xyList1.contains(DataCollectionUtil.getInstance().getNewData()) ) {
                	System.out.println("DynamicLineChart.start(...) - dado repetido");
                } else {
                	xyList1.add(DataCollectionUtil.getInstance().getNewData());
                }
                lineChart.setCreateSymbols(false); //hide dots
            }
        });

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(task);
//        grid.add(lineChart, 1, 0);
        border.setCenter(lineChart);
        border.setRight(addRightNodeVBOX());
        Scene scene  = new Scene(border,1000,600);

        xAxis.setCategories(myXaxisCategories);
//        xAxis.setAutoRanging(false);

        xySeries1 = new XYChart.Series(xyList1);
        xySeries1.setName("Series 1");

//        xySeries2 = new XYChart.Series(xyList2);
//        xySeries2.setName("Series 2");

        lineChart.getData().addAll(xySeries1);

        i = 0;
        
        deviceListener = new DeviceListener() {

        	@Override
        	public void deviceTurnedOn(GenericDevice device) {
        		activateConfigurationSelection();
        		
        	}

        	@Override
        	public void deviceTurnedOff(GenericDevice device) {
        		activateConfigurationSelection();
        	}

			private void activateConfigurationSelection() {
				Configuration activeConfiguration = DeviceManager.getInstance().getActiveConfiguration();
//				System.out.println("DynamicLineChart Active configuration: " + activeConfiguration.getName());
        		if (activeConfiguration.equals(Configuration.Config1)){
        			configurationListView.getSelectionModel().select(0);
        		} else if (activeConfiguration.equals(Configuration.Config2)){
        			configurationListView.getSelectionModel().select(1);
        		} if (activeConfiguration.equals(Configuration.Config3)){
        			configurationListView.getSelectionModel().select(2);
        		} else {
        			configurationListView.getSelectionModel().select(4);
        		}
			}
        	
        };
        
        //TODO: corigir
        DeviceManager.getInstance().getGenericPulseOximeter().registerDeviceObserver(deviceListener);
        DeviceManager.getInstance().getGenericRespirationMonitor().registerDeviceObserver(deviceListener);
        
        //TODO: risk management
        riskObserver = new RiskObserver() {
			
			@Override
			public void notifyRiskChange(Risk newRisk, long currentTimeMilis) {
				SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//dd/MM/yyyy
			    Date now = new Date(currentTimeMilis);
				riskAlarmsTextArea.setText("Risk Level: " + newRisk.getName() + "OVP value: " + newRisk.getRiskvalue()
						+ " // at: " + sdfDate.format(now));
				
				if (Risk.CRITIC_RISK_VALUE.equals(newRisk) || Risk.HIGHTEST_RISK_VALUE.equals(newRisk)) {
					controlActionsTextField.setText("Infusion Stoped | The current Risk higher than critical levels \n");
				} else if (Risk.ALERT_RISK_VALUE.equals(newRisk)) {
					controlActionsTextField.setText(" A Configuration with a higher guarantee level is required \n Bolus: Disabled \n");
				} if (Risk.LOW_ALERT_RISK_VALUE.equals(newRisk) || Risk.LOWER_RISK_VALUE.equals(newRisk)) {
					controlActionsTextField.setText("Bolus: Enabled \n");
				}
			}
		};
		//TODO: refactor
		OpenICEMonitor.getInstance().getRiskCalculator().registerListener(riskObserver);

        stage.setScene(scene);
        stage.show();

        stage.setOnCloseRequest(windowEvent -> {
            task.cancel();
        });
    }
    
    private Node addRightNodeVBOX() {
    	GridPane grid = new GridPane();
		grid.setHgap(1);
		grid.setVgap(1);
		grid.setPadding(new Insets(1, 1, 1, 1));
    	
    	
//    	VBox vbox = new VBox();
//        vbox.setPadding(new Insets(5));
//        vbox.setSpacing(2);
		grid.setStyle("-fx-background-color: DAE6F3;");
        
		grid.add(buildConfigurationsBox(),0,0);
		grid.add(buildControlActions(),0,1);
		grid.add(buildFunctionalAlarmsBOX(),0,2);
		grid.add(buildRiskAlarmsBOX(),0,3);
		
//        vbox.getChildren().add(buildConfigurationsBox());
        
//        vbox.getChildren().add(buildRiskAlarmsBOX());
        
//        vbox.getChildren().add(buildFunctionalAlarmsBOX());

		return grid;
	}

	private Node buildControlActions() {
		VBox controlActionsVBOX = new VBox();
		controlActionsVBOX.setPadding(new Insets(3));
		controlActionsVBOX.setSpacing(2);
        Text controlActionsText = new Text("Control Actions");
        controlActionsText.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        controlActionsVBOX.getChildren().add(controlActionsText);
        
        controlActionsTextField = new TextArea();
        controlActionsTextField.setEditable(false);
        controlActionsTextField.setFont(Font.font("Tahoma", FontWeight.BOLD, 14));
        controlActionsVBOX.getChildren().add(controlActionsTextField);
        
		return controlActionsVBOX;
	}

	private Node buildFunctionalAlarmsBOX() {
		VBox functionalAlarmsVBOX = new VBox();
		functionalAlarmsVBOX.setPadding(new Insets(5));
		functionalAlarmsVBOX.setSpacing(2);
		Text funcAlarmsText = new Text("Functional Alarms");
        funcAlarmsText.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        functionalAlarmsVBOX.getChildren().add(funcAlarmsText);
        
        functionalAlarmsTextArea = new TextArea();
        functionalAlarmsTextArea.setEditable(false);
        functionalAlarmsTextArea.setFont(Font.font("Tahoma", FontWeight.BOLD, 14));
        functionalAlarmsVBOX.getChildren().add(functionalAlarmsTextArea);
        
//        Button btn = new Button("Clear Functional Alarms");
//        HBox hbBtn = new HBox(10);
//        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
//        hbBtn.getChildren().add(btn);
//        functionalAlarmsVBOX.getChildren().add(hbBtn);
        
//        btn.setOnAction(new EventHandler<ActionEvent>() {
//       	 
//            @Override
//            public void handle(ActionEvent e) {
//            	functionalAlarmsTextArea.clear();
//            	functionalAlarmsTextArea.setText("limpou");
//            }
//        });
        
        
		return functionalAlarmsVBOX;
	}

	private VBox buildRiskAlarmsBOX() {
		VBox riskAlarmsVBOX = new VBox();
		riskAlarmsVBOX.setPadding(new Insets(5));
		riskAlarmsVBOX.setSpacing(2);
        Text riskAlarmsText = new Text("Risk Alarms");
        riskAlarmsText.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        riskAlarmsVBOX.getChildren().add(riskAlarmsText);
        
        riskAlarmsTextArea = new TextArea();
        riskAlarmsTextArea.setEditable(false);
        riskAlarmsTextArea.setFont(Font.font("Tahoma", FontWeight.BOLD, 14));
        riskAlarmsVBOX.getChildren().add(riskAlarmsTextArea);
        
        /*Button btn = new Button("Clear Risk Alarms");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(btn);
        riskAlarmsVBOX.getChildren().add(hbBtn);
        
        
        btn.setOnAction(new EventHandler<ActionEvent>() {
          	 
            @Override
            public void handle(ActionEvent e) {
            	riskAlarmsTextArea.clear();
            	riskAlarmsTextArea.setText("limpou");
            }
        });*/
        
		return riskAlarmsVBOX;
	}

	private VBox buildConfigurationsBox() {
		VBox vboxConfigurations = new VBox();
        vboxConfigurations.setPadding(new Insets(10));
        vboxConfigurations.setSpacing(8);
		
		Text title = new Text("Running Configuration");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        vboxConfigurations.getChildren().add(title);
        
        
        ObservableList<String> items =FXCollections.observableArrayList (
            "Configuration 1 - Risk Metric 1", 
            "Configuration 2 - Risk Metric 2", 
            "Configuration 3 - Risk Metric 3",
            "No Configuration is active");
        configurationListView.setItems(items);
        configurationListView.setPrefWidth(50);
        configurationListView.setPrefHeight(175);
        
        activateConfigurationSelection();
        
        vboxConfigurations.getChildren().add(configurationListView);
        
		return vboxConfigurations;
	}
	
	private void activateConfigurationSelection() {
		Configuration activeConfiguration = DeviceManager.getInstance().getActiveConfiguration();
		if (activeConfiguration.equals(Configuration.Config1)){
			configurationListView.getSelectionModel().select(0);
		} else if (activeConfiguration.equals(Configuration.Config2)){
			configurationListView.getSelectionModel().select(1);
		} if (activeConfiguration.equals(Configuration.Config3)){
			configurationListView.getSelectionModel().select(2);
		} else {
			configurationListView.getSelectionModel().select(4);
		}
	}
	

	public static void main(String[] args) {
    	try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	DynamicLineChart.launch(args);
    }
}