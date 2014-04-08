package org.pikater.shared.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class BidiMap<K,V>
{
	private final Map<K,V> keyToValue;
	private final Map<V,K> valueToKey;

	public BidiMap()
	{
		this.keyToValue = new HashMap<K,V>();
		this.valueToKey = new HashMap<V,K>();
	}
	
	public int size()
	{
		return keyToValue.size();
	}

	public boolean isEmpty()
	{
		return keyToValue.isEmpty();
	}
	
	public V getValue(K key)
	{
		return keyToValue.get(key);
	}
	
	public K getKey(V value)
	{
		return valueToKey.get(value);
	}
	
	public void put(K key, V value)
	{
		valueToKey.put(value, key);
		keyToValue.put(key, value);
	}
	
	public void putAll(Map<? extends K, ? extends V> m)
	{
		for(Entry<? extends K, ? extends V> entry : m.entrySet())
		{
			put(entry.getKey(), entry.getValue());
		}
	}
	
	public void remove(K key)
	{
		valueToKey.remove(getValue(key));
		keyToValue.remove(key);
	}
	
	public void clear()
	{
		keyToValue.clear();
		valueToKey.clear();
	}
	
	public boolean containsKey(K key)
	{
		return keyToValue.containsKey(key);
	}

	public boolean containsValue(V value)
	{
		return valueToKey.containsKey(value);
	}

	public Set<K> keySet()
	{
		return keyToValue.keySet();
	}
	
	public Set<V> valueSet()
	{
		return valueToKey.keySet();
	}

	public Set<java.util.Map.Entry<K, V>> entrySet()
	{
		return keyToValue.entrySet();
	}
}
