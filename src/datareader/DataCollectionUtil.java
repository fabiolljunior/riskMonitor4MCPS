package datareader;

import java.text.SimpleDateFormat;
import java.util.Date;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import riskAssessment.Risk;

public class DataCollectionUtil {
	
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
	
	private static DataCollectionUtil singleton = null;
	private static XYChart.Series<String, String> seriesChina = new XYChart.Series<String, String>();
	private static Data<String, String> newData = new XYChart.Data<String, String>();
	
	
	public static void addData(Risk newRisk, long currentTimeMilis) {
		Date d = new Date(currentTimeMilis);
		String strDate = dateFormat.format(d);
		
		newData = new XYChart.Data<String, String>(strDate, newRisk.getName());
		seriesChina.getData().add(newData);
	}
	
	public static ObservableList<XYChart.Series<String, String>> getSeries() 
	{
		seriesChina = new XYChart.Series<String, String>();
		seriesChina.setName("China");
//		seriesChina.getData().add(new XYChart.Data<Number, Number>(1950, 555));
//		seriesChina.getData().add(new XYChart.Data<Number, Number>(2000, 1275));
//		seriesChina.getData().add(new XYChart.Data<Number, Number>(2050, 1395));
//		seriesChina.getData().add(new XYChart.Data<Number, Number>(2100, 1182));
//		seriesChina.getData().add(new XYChart.Data<Number, Number>(2150, 1149));
		
		ObservableList<XYChart.Series<String, String>> data =
			FXCollections.<XYChart.Series<String, String>>observableArrayList();
		data.add(seriesChina);
		return data;
	}
	
	public static DataCollectionUtil getInstance() {
		if (singleton == null) {
			singleton = new DataCollectionUtil();
		}
		return singleton;
	}

	public static Data<String, String> getNewData() {
		return newData;
	}

	public static void setNewData(XYChart.Data<String, String> teste) {
		DataCollectionUtil.newData = teste;
	}

}
