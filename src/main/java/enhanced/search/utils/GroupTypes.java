package enhanced.search.utils;

import enhanced.search.dto.GroupType;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class GroupTypes {
    private static final Path path = Path.of("./group_types.txt"); //TODO: add path to config
    public final Map<Long, String> id2type;
    public final List<GroupType> types;

    public GroupTypes() {
        if (!Files.exists(path)) {
            id2type = Collections.emptyMap();
            types = Collections.emptyList();
            return;
        } else {
            id2type = new HashMap<>();
            types = new ArrayList<>();
        }
        Set<String> types_set = new HashSet<>();
        try {
            for (String line : Files.readAllLines(path)) {
                String[] tokens = line.split(":");
                if (tokens.length >= 2) {
                    try {
                        Long key = Long.parseLong(tokens[0].trim());
                        String type = tokens[1].trim();
                        id2type.put(key, type);
                        types_set.add(type);
                    } catch (NumberFormatException ignored) {
                        //ignore line with invalid id
                    }
                }
            }
        } catch (IOException ignored) {
            //TODO: нормальную обработку исключений
        }
        int i = 0;
        for (String t : types_set) {
            types.add(new GroupType(i++, t));
        }
    }
}
