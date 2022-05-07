package enhanced.search.utils;

import enhanced.search.dto.GroupType;
import org.gitlab4j.api.models.Group;

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
            Map<String, Integer> type2Id = new HashMap<>();
            for (String line : Files.readAllLines(path)) {
                String[] tokens = line.split(":");
                if (tokens.length >= 2) {
                        String type = tokens[0].trim();
                        if (type.isEmpty()) {
                            continue;
                        }
                        int typeId = type2Id.compute(type, (t, id) -> {
                            if (id == null) {
                                int newId = types.size();
                                types.add(new GroupType(newId, t));
                                return newId;
                            } else {
                                return id;
                            }
                        });
                        GroupType gt = types.get(typeId);

                        String[] groups = tokens[1].split(",");
                        for (String groupId : groups) {
                            try {
                                Long id = Long.parseLong(groupId.trim());
                                id2type.putIfAbsent(id, gt);
                            } catch (NumberFormatException ignored) {}
                        }
                }
            }
        } catch (IOException ignored) {
            //TODO: нормальную обработку исключений
        }
    }

    public Long group2TypeId(Group g) {
        GroupType gt = id2type.get(g.getId());
        return gt == null? null : gt.getId();
    }
}
