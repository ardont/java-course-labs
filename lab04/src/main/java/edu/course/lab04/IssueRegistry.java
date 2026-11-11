package edu.course.lab04;

import java.util.HashMap;
import java.util.Map;

/**
 * Реестр задач на основе HashMap<IssueId, ProjectTask>.
 */
public class IssueRegistry {

    private final Map<IssueId, ProjectTask> storage = new HashMap<>();

    public void put(IssueId id, ProjectTask task) {
        if (id == null) {
            throw new IllegalArgumentException("IssueId must not be null");
        }
        if (task == null) {
            throw new IllegalArgumentException("ProjectTask must not be null");
        }
        storage.put(id, task);
    }

    public ProjectTask get(IssueId id) {
        if (id == null) {
            throw new IllegalArgumentException("IssueId must not be null");
        }
        return storage.get(id);
    }

    public ProjectTask remove(IssueId id) {
        if (id == null) {
            throw new IllegalArgumentException("IssueId must not be null");
        }
        return storage.remove(id);
    }

    public boolean containsKey(IssueId id) {
        if (id == null) {
            throw new IllegalArgumentException("IssueId must not be null");
        }
        return storage.containsKey(id);
    }

    public int size() {
        return storage.size();
    }

    public boolean isEmpty() {
        return storage.isEmpty();
    }

    public void clear() {
        storage.clear();
    }
}
