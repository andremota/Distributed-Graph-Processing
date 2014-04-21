package pt.isel.ps1314v.g11.hama.util;

import java.util.Iterator;

import org.apache.hadoop.io.Writable;

public class IteratorsUtil {
	
	public static class CastIterable<V extends Writable, M extends Writable> implements Iterable<M> {

		private final Iterable<V> iterable;
		public CastIterable(Iterable<V> iter) {
			this.iterable = iter;
		}

		@Override
		public Iterator<M> iterator() {
			// TODO Auto-generated method stub
			return new Iterator<M>(){

				final Iterator<V> iterator = iterable.iterator();
				@Override
				public boolean hasNext() {
					// TODO Auto-generated method stub
					return iterator.hasNext();
				}

				@SuppressWarnings("unchecked")
				@Override
				public M next() {
					// TODO Auto-generated method stub
					return (M)iterator.next();
				}

				@Override
				public void remove() {
					iterator.remove();
					
				}
				
			};
		}

	}

	public static <E,K> void removeKFromIterator(K key, Iterator<E> it, KeyCompare<K, E> compare){
		 while(it.hasNext()) {
		        E elem = it.next();
		        if (compare.compareKeyToElem(key, elem)) {
		          it.remove();
		        }
		  }
	}

	public interface KeyCompare<K,E>{
		boolean compareKeyToElem(K key, E elem);
	}
	
	public static <V extends Writable, M extends Writable> Iterable<M> cast(Iterable<V> iter){
		return new CastIterable<V,M>(iter);
	}
}
