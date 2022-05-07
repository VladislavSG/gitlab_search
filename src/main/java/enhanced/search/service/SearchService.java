package enhanced.search.service;

import enhanced.search.dto.SearchRequest;
import org.gitlab4j.api.Constants.*;
import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.GitLabApiException;
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

    private List<?> search(SearchRequest request, Object scope) {
        try {
            if (scope instanceof ProjectSearchScope projectScope) {
                return gitLabApi
                        .getSearchApi()
                        .projectSearch(request.getProjectId(), projectScope, request.getSearchString());
            }
            if (scope instanceof  SearchScope globalScope) {
                return gitLabApi
                        .getSearchApi()
                        .globalSearch(globalScope, request.getSearchString());
            }
        } catch (GitLabApiException ignored) {}
        return Collections.EMPTY_LIST;
    }

    @SuppressWarnings("unchecked")
    public List<SearchBlob> searchBlobs(SearchRequest request) {
        List<SearchBlob> result = (List<SearchBlob>) search(
                request,
                request.isGlobal() ? ProjectSearchScope.BLOBS : SearchScope.BLOBS
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
                request.isGlobal() ? SearchScope.ISSUES : ProjectSearchScope.ISSUES
        );
    }

    @SuppressWarnings("unchecked")
    public List<MergeRequest> searchMergeRequest(SearchRequest request) {
        return (List<MergeRequest>) search(
                request,
                request.isGlobal() ? SearchScope.MERGE_REQUESTS : ProjectSearchScope.MERGE_REQUESTS
        );
    }

    @SuppressWarnings("unchecked")
    public List<SearchBlob> searchWiki(SearchRequest request) {
        return (List<SearchBlob>) search(
                request,
                request.isGlobal() ? SearchScope.WIKI_BLOBS : ProjectSearchScope.WIKI_BLOBS
        );
    }

    @SuppressWarnings("unchecked")
    public List<Commit> searchCommits(SearchRequest request) {
        return (List<Commit>) search(
                request,
                request.isGlobal() ? SearchScope.COMMITS : ProjectSearchScope.COMMITS
        );
    }

    @SuppressWarnings("unchecked")
    public List<Note> searchComments(SearchRequest request) {
        if (request.getProjectId() == -1L) {
            return Collections.emptyList();
            //throw new IllegalArgumentException();
        }
        return (List<Note>) search(
                request,
                ProjectSearchScope.NOTES
        );
    }

    @SuppressWarnings("unchecked")
    public List<Milestone> searchMilestone(SearchRequest request) {
        return (List<Milestone>) search(
                request,
                request.isGlobal() ? SearchScope.MILESTONES : ProjectSearchScope.MILESTONES
        );
    }

    @SuppressWarnings("unchecked")
    public List<User> searchUsers(SearchRequest request) {
        return (List<User>) search(
                request,
                request.isGlobal() ? SearchScope.USERS : ProjectSearchScope.USERS
        );
    }
}
