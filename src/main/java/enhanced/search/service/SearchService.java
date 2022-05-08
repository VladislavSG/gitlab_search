package enhanced.search.service;

import enhanced.search.dto.SearchRequest;
import enhanced.search.utils.AllSearchScopes;
import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.SearchApi;
import org.gitlab4j.api.models.*;
import org.glassfish.jersey.internal.guava.Predicates;

import java.util.Collections;
import java.util.List;

public class SearchService {
    private final GitLabApi gitLabApi;

    public SearchService() {
        this(new GitLabApi("http://localhost", "rsQKVaBP1-RvtYGD4eTW"));
    }

    public SearchService(final GitLabApi gitLabApi) {
        this.gitLabApi = gitLabApi;
    }

    private List<?> search(SearchRequest request, AllSearchScopes scope) {
        try {
            SearchApi searchApi = gitLabApi.getSearchApi();
            return switch (request.getScope()) {
                case GLOBAL, GROUP_TYPE -> searchApi.globalSearch(
                        scope.getGlobalScope(),
                        request.getSearchString()
                );
                case GROUP -> searchApi.groupSearch(
                        request.getGroupId(),
                        scope.getGroupScope(),
                        request.getSearchString()
                );
                case PROJECT -> searchApi.projectSearch(
                        request.getProjectId(),
                        scope.getProjectScope(),
                        request.getSearchString()
                );
            };
        } catch (GitLabApiException | IllegalArgumentException ignored) {
            return Collections.EMPTY_LIST;
        }
    }

    @SuppressWarnings("unchecked")
    public List<SearchBlob> searchBlobs(SearchRequest request) {
        List<SearchBlob> result = (List<SearchBlob>) search(
                request,
                AllSearchScopes.BLOBS
        );
        return result
                .stream()
                .filter(Predicates.compose(request.getBranches()::contains, SearchBlob::getRef))
                .toList();
    }

    @SuppressWarnings("unchecked")
    public List<Issue> searchIssues(SearchRequest request) {
        return  (List<Issue>) search(
                request,
                AllSearchScopes.ISSUES
        );
    }

    @SuppressWarnings("unchecked")
    public List<MergeRequest> searchMergeRequest(SearchRequest request) {
        return (List<MergeRequest>) search(
                request,
                AllSearchScopes.MERGE_REQUESTS
        );
    }

    @SuppressWarnings("unchecked")
    public List<SearchBlob> searchWiki(SearchRequest request) {
        return (List<SearchBlob>) search(
                request,
                AllSearchScopes.WIKI_BLOBS
        );
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
}
