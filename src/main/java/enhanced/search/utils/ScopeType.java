package enhanced.search.utils;

public enum ScopeType {
    PROJECT,
    GROUP,
    GROUP_TYPE,
    GLOBAL;

    public ScopeType next() {
        int ord = ordinal();
        return ord == 0 ? null : values()[ord - 1];
    }
}
