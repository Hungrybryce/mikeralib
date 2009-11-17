package mikera.persistent;

import java.util.Collection;
import java.util.List;

public interface IPersistentList<T> extends IPersistentCollection<T>, List<T>, Comparable<PersistentList<T>> {
	
	// modifier methods
	
	public PersistentList<T> append(PersistentList<T> values);

	public PersistentList<T> append(Collection<T> values);

	public PersistentList<T> insert(int index, T value);
	
	public PersistentList<T> insert(int index, Collection<T> values);

	public PersistentList<T> delete(int index);

	public PersistentList<T> delete(int startIndex, int endIndex);

	public PersistentList<T> update(int index, T value);
	
	
	// access methods
	
	public PersistentList<T> subList(int fromIndex, int toIndex);
	
	public T get(int i);
	
}