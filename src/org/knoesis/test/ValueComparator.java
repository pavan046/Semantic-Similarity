package org.knoesis.test;

import java.util.Comparator;
import java.util.Map;

/**
 * This class is used in sorting the map based on values.
 * @author koneru
 *
 */
public class ValueComparator implements Comparator {
	Map hashMap;
	
	public ValueComparator(Map hashMap) {
		this.hashMap = hashMap;
	}

	@Override
	public int compare(Object o1, Object o2) {
		// TODO Auto-generated method stub
		
		if((Double)hashMap.get(o1) < (Double)hashMap.get(o2)){
			return 1;
		} else if ((Double)hashMap.get(o1) == (Double)hashMap.get(o2)) {
			return 0;
		} else {
			return -1;
		}
	}
}
