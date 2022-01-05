package etu.simonzo.competition.util;

import java.util.ArrayList ;
import java.util.LinkedHashMap;
import java.util.List ;
import java.util.Map;
import java.util.Map.Entry;

public class MapUtil {
    public static <K, V extends Comparable<? super V>>
    
    Map<K, V> sortByDescendingValue(Map<K, V> map) {
        List<Entry<K, V>> sortedEntries = new ArrayList<>(map.entrySet());        
        sortedEntries.sort(Entry.comparingByValue((V v1, V v2) -> v2.compareTo(v1)));
        Map<K, V> result = new LinkedHashMap<>();
        sortedEntries.forEach((Entry<K, V> e) -> result.put(e.getKey(),e.getValue()));
        return result;
    }
}
