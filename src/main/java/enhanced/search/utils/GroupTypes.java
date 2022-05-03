package enhanced.search.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class GroupTypes {
    private static final Path path = Path.of("./group_types.txt");
    public final Map<Long, String> id2type;
    public final List<String> types;

    public GroupTypes() {
        id2type = new HashMap<>();
        types = new ArrayList<>();
        if (!Files.exists(path)) {
            return;
        }
        try {
            for (String line : Files.readAllLines(path)) {
                String[] tokens = line.split(":");
                if (tokens.length >= 2) {
                    try {
                        Long key = Long.parseLong(tokens[0].trim());
                        String type = tokens[1].trim();
                        id2type.put(key, type);
                        types.add(type);
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
