package aoc2020;

public class Pair<K,V> {
	private K key;
	private V value;
	
	Pair(K key, V value) {
		this.key = key;
		this.value = value;
	}
	
	public K getKey() {
		return key;
	}
	
	public V getValue() {
		return value;
	}
	
	public String toString() {
		return "(" + key.toString() + ", " + value.toString() + ")";
	}
	
	public boolean equals(Object o) {
		if (o == null) return false;
		try {
			Pair<K,V> other = (Pair<K,V>) o;
			if (other.key.equals(key) && other.value.equals(value)) {
				return true;
			} else {
				return false;
			}
		} catch (ClassCastException e){
			return false;
		}
	}
}
