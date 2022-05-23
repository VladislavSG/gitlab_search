package enhanced.search.utils;

import org.gitlab4j.api.Constants.*;

public enum AllSearchScopes {
    BLOBS          (SearchScope.BLOBS,
                    null,
                    ProjectSearchScope.BLOBS),
    ISSUES         (SearchScope.ISSUES,
                    GroupSearchScope.ISSUES,
                    ProjectSearchScope.ISSUES),
    MERGE_REQUESTS (SearchScope.MERGE_REQUESTS,
                    GroupSearchScope.MERGE_REQUESTS,
                    ProjectSearchScope.MERGE_REQUESTS),
    WIKI_BLOBS     (SearchScope.WIKI_BLOBS,
                    null,
                    ProjectSearchScope.WIKI_BLOBS),
    COMMITS        (SearchScope.COMMITS,
                    null,
                    ProjectSearchScope.COMMITS),
    NOTES          (null,
                    null,
                    ProjectSearchScope.NOTES),
    MILESTONES     (SearchScope.MILESTONES,
                    GroupSearchScope.MILESTONES,
                    ProjectSearchScope.MILESTONES),
    USERS          (SearchScope.USERS,
                    GroupSearchScope.USERS,
                    ProjectSearchScope.USERS);

    private final SearchScope GLOBAL_SCOPE;
    private final GroupSearchScope GROUP_SCOPE;
    private final ProjectSearchScope PROJECT_SCOPE;

    AllSearchScopes(final SearchScope global,
                    final GroupSearchScope group,
                    final ProjectSearchScope project) {
        GLOBAL_SCOPE = global;
        GROUP_SCOPE = group;
        PROJECT_SCOPE = project;
    }

    private <T> T nullCheck(T obj) {
        if (obj == null) {
            throw new UnsupportedOperationException();
        } else {
            return obj;
        }
    }

    public SearchScope getGlobalScope() {
        return nullCheck(GLOBAL_SCOPE);
    }

    public GroupSearchScope getGroupScope() {
        return nullCheck(GROUP_SCOPE);
    }

    public ProjectSearchScope getProjectScope() {
        return nullCheck(PROJECT_SCOPE);
    }
}
