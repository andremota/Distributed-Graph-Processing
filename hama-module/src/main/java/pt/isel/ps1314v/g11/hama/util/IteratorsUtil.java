package pt.isel.ps1314v.g11.hama.util;

import java.util.Iterator;

public class IteratorsUtil {
	
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
	
}
