package org.toilelibre.libe.phonecomposer.code;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class DTMFCode {

	public static final Map<Character, int []> FREQUENCIES =
			Collections.unmodifiableMap(new HashMap<Character, int []>(){
			{
				this.put('0', new int [] {941,1336});
				this.put('1', new int [] {697,1209});
				this.put('2', new int [] {697,1336});
				this.put('3', new int [] {697,1477});
				this.put('4', new int [] {770,1209});
				this.put('5', new int [] {770,1336});
				this.put('6', new int [] {770,1477});
				this.put('7', new int [] {852,1209});
				this.put('8', new int [] {852,1336});
				this.put('9', new int [] {852,1477});
				this.put('*', new int [] {941,1209});
				this.put('+', new int [] {941,1209});
				this.put('#', new int [] {941,1477});
				this.put('A', new int [] {770,1633});
				this.put('B', new int [] {941,1633});
				this.put('C', new int [] {852,1633});
				this.put('D', new int [] {941,1633});
			}});
}
