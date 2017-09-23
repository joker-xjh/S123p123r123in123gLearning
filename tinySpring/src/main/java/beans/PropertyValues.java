package beans;

import java.util.ArrayList;
import java.util.List;

//包装一个对象所有的PropertyValue

public class PropertyValues {
	
	private List<PropertyValue> propertyValues;
	
	public PropertyValues() {
		propertyValues = new ArrayList<PropertyValue>();
	}
	
	public void addPropertyValue(PropertyValue propertyValue) {
		propertyValues.add(propertyValue);
	}
	
	public List<PropertyValue> getPropertyValues(){
		return propertyValues;
	}

}
