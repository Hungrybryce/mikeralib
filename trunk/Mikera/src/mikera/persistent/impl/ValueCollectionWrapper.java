package mikera.persistent.impl;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import mikera.persistent.*;

public class ValueCollectionWrapper<K,V> extends PersistentCollection<V> {
	PersistentSet<Map.Entry<K,V>> source;
	
	public ValueCollectionWrapper(PersistentSet<Map.Entry<K, V>> base) {
		source=base;
	}

	@Override
	public int size() {
		return source.size();
	}

	public Iterator<V> iterator() {
		return new ValueCollectionIterator<K, V>(source);
	}
	
	public static class ValueCollectionIterator<K,V> implements Iterator<V> {
		private Iterator<Map.Entry<K,V>> source;
		
		public ValueCollectionIterator(PersistentSet<Map.Entry<K,V>> base) {
			source=base.iterator();
		}

		public boolean hasNext() {
			return source.hasNext();
		}

		public V next() {
			Map.Entry<K,V> next=source.next();
			return next.getValue();
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}
	}

	@Override
	public PersistentCollection<V> include(V value) {
		return ListFactory.create(this).include(value);
	}

}