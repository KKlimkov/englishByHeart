package org.example.englishByHeart.dto;

import java.util.List;
import java.util.stream.Collectors;

public class IdNamePairHelper {

    public static List<IdNamePair> formIdNamePairs(List<String> ids, List<String> names) {
        return ids.stream().map(id -> {
            IdNamePair pair = new IdNamePair();
            pair.setId(Long.valueOf(id));
            pair.setName(names.get(ids.indexOf(id)));
            return pair;
        }).collect(Collectors.toList());
    }
}
