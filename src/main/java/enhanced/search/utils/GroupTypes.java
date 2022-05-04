package enhanced.search.utils;

import enhanced.search.dto.GroupType;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class GroupTypes {
    private static final Path path = Path.of("./group_types.txt"); //TODO: add path to config
    public final Map<Long, GroupType> id2type;
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

        try {
            for (String line : Files.readAllLines(path)) {
                String[] tokens = line.split(":");
                if (tokens.length >= 2) {
                    try {
                        long key = Long.parseLong(tokens[0].trim());
                        String type = tokens[1].trim();
                        GroupType gt;
                        int types_size = types.size();
                        if (key < types_size) {
                            gt = types.get(Math.toIntExact(key));
                        } else {
                            gt = new GroupType(types_size, type);
                            types.add(gt);
                        }
                        id2type.put(key, gt);
                    } catch (NumberFormatException ignored) {
                        //ignore line with invalid id
                    }
                }
            }
        } catch (IOException ignored) {
            //TODO: нормальную обработку исключений
        }
    }
}
