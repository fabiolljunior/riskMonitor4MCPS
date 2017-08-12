package measurementModel;

import java.util.ArrayList;
import java.util.List;

import exceptions.InvalidInputException;

public class MeasurementParser {
	
	public static Measurement stringToMeasurement(String input) throws InvalidInputException {
		if (input == null || input.isEmpty()) {
			throw new InvalidInputException("Invalid Measurement Input. Value is null or empty.");
		}
		
		Measurement measurement = new Measurement();
		
		if (input.contains(VARIABLE_INPUT_DEFAULT.UNIQUE_DEVICE_ID.getName())) {
			String value = getValueFromIndexes(VARIABLE_INPUT_DEFAULT.UNIQUE_DEVICE_ID, VARIABLE_INPUT_DEFAULT.METRIC_ID, input);
			
			if (!value.isEmpty() && value != null) {
				measurement.setDeviceId(value.trim());
			}
		}
		
		if (input.contains(VARIABLE_INPUT_DEFAULT.METRIC_ID.getName())) {
			String value = getValueFromIndexes(VARIABLE_INPUT_DEFAULT.METRIC_ID, VARIABLE_INPUT_DEFAULT.VENDOR_ID, input);
			
			if (!value.isEmpty() && value != null) {
				measurement.setMeasurementId(value.trim());
			}
		}
		
		if (input.contains(VARIABLE_INPUT_DEFAULT.UNIT_ID.getName())) {
			String value = "";
			try {
				value = getValueFromIndexes(VARIABLE_INPUT_DEFAULT.UNIT_ID, VARIABLE_INPUT_DEFAULT.FREQUENCY, input);
			} catch (StringIndexOutOfBoundsException e) {
				value = getValueFromIndexes(VARIABLE_INPUT_DEFAULT.UNIT_ID, VARIABLE_INPUT_DEFAULT.VALUE, input);
			}
			
			if (!value.isEmpty() && value != null) {
				measurement.setUnitId(value.trim());
			}
		}
		
		if (input.contains(VARIABLE_INPUT_DEFAULT.DEVICE_TIME.getName())) {
			String value = getValueFromIndexes(VARIABLE_INPUT_DEFAULT.SEC, VARIABLE_INPUT_DEFAULT.NANOSEC, input);
			
			if (!value.isEmpty() && value != null) {
				String valueString = value.trim();
				measurement.setTimeStampInMiliseconds(Long.valueOf(valueString));
			}
		}
		
		if (input.contains(VARIABLE_INPUT_DEFAULT.VALUE.getName() + ":")) {
			String value = getValueFromIndexes(VARIABLE_INPUT_DEFAULT.VALUE, VARIABLE_INPUT_DEFAULT.DEVICE_TIME, input);
			
			if (!value.isEmpty() && value != null) {
				String valueString = value.trim();
				measurement.setValue(new Double(valueString));
			}
		}
		
		if (input.contains(VARIABLE_INPUT_DEFAULT.VALUES.getName())) {
			String value = getValueFromIndexes(VARIABLE_INPUT_DEFAULT.USER_DATA, VARIABLE_INPUT_DEFAULT.DEVICE_TIME, input);
			
			if (!value.isEmpty() && value != null) {
				String valueString = value.trim();
				
				if (valueString != null && !valueString.isEmpty()) {
					String[] doubleAsString = valueString.split(",");
					
					if (doubleAsString.length > 0) {
						List<Double> values = convertArrayStringToArrayDouble(doubleAsString);
						measurement.setValues(values);
					}
				}
			}
		}
		
		return measurement;
	}

	private static List<Double> convertArrayStringToArrayDouble(String[] split) {
		List<Double> values = new ArrayList<Double>();
		for (String val : split) {
			values.add(new Double(val));
		}
		return values;
	}
	
	private static String getValueFromIndexes(VARIABLE_INPUT_DEFAULT first, VARIABLE_INPUT_DEFAULT end,
			String input) {
		
		String variable = first.getName()+":";
		
		int index = input.indexOf(variable) + variable.length();
		int indexOf = input.indexOf(end.getName());
		return input.substring(index, indexOf-1);
	}
}
