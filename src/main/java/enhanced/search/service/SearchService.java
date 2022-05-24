package enhanced.search.service;

import enhanced.search.dto.GroupType;
import enhanced.search.dto.SearchRequest;
import enhanced.search.utils.AllSearchScopes;
import enhanced.search.utils.ScopeType;
import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.*;
import org.glassfish.jersey.internal.guava.Predicates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class SearchService {
    private GitLabApi gitLabApi;
    private final GitlabGetService getService;

    @Value("${gitlab.url}")
    private String gitlabUrl = "http://localhost";

    public SearchService(@Autowired GitlabGetService service){
        this.getService = service;
    }

    public void initToken(String token) {
        this.gitLabApi = new GitLabApi(gitlabUrl, token);
        this.getService.initToken(token);
    }

    private Set<String> getBranches(SearchRequest request) {
        try {
            Set<String> res = request.getBranches();
            if (res.isEmpty()) {
                res = getService
                        .getBranches(request)
                        .stream()
                        .map(enhanced.search.dto.Branch::toString)
                        .collect(Collectors.toSet());
            }
            try {
                if (!request.getBranchMask().isEmpty()) {
                    Pattern mask = Pattern.compile(request.getBranchMask());
                    res = res
                            .stream()
                            .filter(mask.asPredicate())
                            .collect(Collectors.toSet());
                }
            } catch (PatternSyntaxException ignored) {}
            return res;
        } catch (GitLabApiException ignored) {
        }
        return Collections.emptySet();
    }

    private List<?> search(SearchRequest request, AllSearchScopes scope) {
        if (request.getSearchString().isBlank()) {
            return Collections.EMPTY_LIST;
        }
        final SearchState ss = new SearchState(request, scope);
        try {
            for (ScopeType st = request.getScope(); st != null; st = st.next()) {
                try {
                    return switch (st) {
                        case GLOBAL -> ss.searchInGlobal();
                        case GROUP_TYPE -> ss.searchInGroupTypes();
                        case GROUP -> ss.searchInGroups();
                        case PROJECT -> ss.searchInProjects();
                    };
                } catch (UnsupportedOperationException ignored) {
                }
            }
        } catch (GitlabRuntimeException ignored) {
        }
        // return EMPTY_LIST on fail. Some Gitlab api search requests can throw GitlabApiException
        // even when used correctly. For example search blobs in CE version.
        return Collections.EMPTY_LIST;
    }

    @SuppressWarnings("unchecked")
    public List<SearchBlob> searchBlobs(SearchRequest request, AllSearchScopes scope) {
        List<SearchBlob> result = (List<SearchBlob>) search(
                request,
                scope
        );
        return result
                .stream()
                .filter(Predicates.compose(
                        getBranches(request)::contains,
                        SearchBlob::getRef))
                .toList();
    }

    public List<SearchBlob> searchBlobs(SearchRequest request) {
        return searchBlobs(request, AllSearchScopes.BLOBS);
    }

    @SuppressWarnings("unchecked")
    public List<Issue> searchIssues(SearchRequest request) {
        return (List<Issue>) search(
                request,
                AllSearchScopes.ISSUES
        );
    }

    @SuppressWarnings("unchecked")
    public List<MergeRequest> searchMergeRequest(SearchRequest request) {
        List<MergeRequest> res = (List<MergeRequest>) search(
                request,
                AllSearchScopes.MERGE_REQUESTS
        );
        Set<String> branches = getBranches(request);
        if (!branches.isEmpty()) {
            res = res.stream()
                    .filter(mr -> branches.contains(mr.getSourceBranch() + " " + mr.getProjectId())
                            || branches.contains(mr.getTargetBranch() + " " + mr.getProjectId()))
                    .toList();
        }
        return res;
    }

    public List<SearchBlob> searchWiki(SearchRequest request) {
        return searchBlobs(request, AllSearchScopes.WIKI_BLOBS);
    }

    @SuppressWarnings("unchecked")
    public List<Commit> searchCommits(SearchRequest request) {
        return (List<Commit>) search(
                request,
                AllSearchScopes.COMMITS
        );
    }

    @SuppressWarnings("unchecked")
    public List<Note> searchComments(SearchRequest request) {
        return (List<Note>) search(
                request,
                AllSearchScopes.NOTES
        );
    }

    @SuppressWarnings("unchecked")
    public List<Milestone> searchMilestone(SearchRequest request) {
        return (List<Milestone>) search(
                request,
                AllSearchScopes.MILESTONES
        );
    }

    @SuppressWarnings("unchecked")
    public List<User> searchUsers(SearchRequest request) {
        return (List<User>) search(
                request,
                AllSearchScopes.USERS
        );
    }

    private static class GitlabRuntimeException extends RuntimeException {
        public GitlabRuntimeException(final Throwable cause) {
            super(cause);
        }
    }

    private class SearchState {
        private final String request;
        private final AllSearchScopes searchScope;

        private final Set<String> groupTypes;
        private List<Long> groupsId;
        private List<Long> projectsId;

        public SearchState(final SearchRequest request, final AllSearchScopes scope) {
            this.request = request.getSearchString();
            this.searchScope = scope;
            final String groupType = request.getGroupType();
            this.groupTypes = groupType.isEmpty() ? null : Set.of(groupType);
            long groupId = request.getGroupId();
            this.groupsId = groupId == -1L ? null : List.of(groupId);
            long projectId = request.getProjectId();
            this.projectsId = projectId == -1L ? null : List.of(projectId);
        }

        public List<?> searchInGlobal() {
            try {
                return gitLabApi
                        .getSearchApi()
                        .globalSearch(
                                searchScope.getGlobalScope(),
                                request
                        );
            } catch (GitLabApiException e) {
                throw new GitlabRuntimeException(e);
            }
        }

        public List<?> searchInGroupTypes() {
            updateGroups();
            return searchInGroups();
        }

        public List<?> searchInGroups() {
            updateGroups();
            return groupsId
                    .stream()
                    .map(this::searchInGroups)
                    .flatMap(Collection::stream)
                    .toList();
        }

        private List<?> searchInGroups(Object id) {
            try {
                return gitLabApi
                        .getSearchApi()
                        .groupSearch(
                                id,
                                searchScope.getGroupScope(),
                                request
                        );
            } catch (GitLabApiException e) {
                throw new GitlabRuntimeException(e);
            }
        }

        public List<?> searchInProjects() {
            updateProjects();
            return projectsId
                    .stream()
                    .map(this::searchInProjects)
                    .flatMap(Collection::stream)
                    .toList();
        }

        private List<?> searchInProjects(Object id) {
            try {
                return gitLabApi
                        .getSearchApi()
                        .projectSearch(
                                id,
                                searchScope.getProjectScope(),
                                request
                        );
            } catch (GitLabApiException e) {
                throw new GitlabRuntimeException(e);
            }
        }

        private void updateGroups() {
            try {
                if (groupsId == null) {
                    Stream<Long> groups = getService
                            .getGroups()
                            .stream()
                            .map(enhanced.search.dto.Group::getId);
                    if (groupTypes != null) {
                        groups = groups.filter(id -> {
                            GroupType gt = GitlabGetService.gt.id2type.get(id);
                            return gt != null && groupTypes.contains(gt.getName());
                        });
                    }
                    groupsId = groups.toList();
                }
            } catch (GitLabApiException e) {
                throw new GitlabRuntimeException(e);
            }
        }

        private void updateProjects() {
            if (projectsId == null) {
                projectsId = groupsId
                        .stream()
                        .flatMap(id -> {
                            try {
                                return gitLabApi
                                        .getGroupApi()
                                        .getProjectsStream(id);
                            } catch (GitLabApiException e) {
                                throw new GitlabRuntimeException(e);
                            }
                        })
                        .map(Project::getId)
                        .toList();
            }
        }
    }
}
