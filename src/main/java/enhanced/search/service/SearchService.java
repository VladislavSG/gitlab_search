package enhanced.search.service;

import enhanced.search.dto.SearchRequest;
import enhanced.search.utils.GroupTypes;
import org.gitlab4j.api.Constants;
import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.*;
import org.glassfish.jersey.internal.guava.Predicates;

import java.util.Collections;
import java.util.List;

public class SearchService {
    private final GitLabApi gitLabApi;
    private final GroupTypes gt = new GroupTypes();

    public SearchService() {
        this(new GitLabApi("http://localhost", "rsQKVaBP1-RvtYGD4eTW"));
    }

    public SearchService(final GitLabApi gitLabApi) {
        this.gitLabApi = gitLabApi;
    }

    public List<?> search(SearchRequest request, Constants.ProjectSearchScope scope) throws GitLabApiException {
        try {
            return gitLabApi
                    .getSearchApi()
                    .projectSearch(request.getProjectId(), scope, request.getSearchString());
        } catch (GitLabApiException exception) {
            return Collections.emptyList();
        }
    }

    @SuppressWarnings("unchecked")
    public List<SearchBlob> searchBlobs(SearchRequest request) throws GitLabApiException {
        List<SearchBlob> result = (List<SearchBlob>) search(request, Constants.ProjectSearchScope.BLOBS);
        return result
                .stream()
                .filter(Predicates.compose(request.getBranches()::contains, SearchBlob::getRef))
                .toList();
    }

    @SuppressWarnings("unchecked")
    public List<Issue> searchIssues(SearchRequest request) throws GitLabApiException {
        return  (List<Issue>) search(request, Constants.ProjectSearchScope.ISSUES);
    }

    @SuppressWarnings("unchecked")
    public List<MergeRequest> searchMergeRequest(SearchRequest request) throws GitLabApiException {
        return (List<MergeRequest>) search(request, Constants.ProjectSearchScope.MERGE_REQUESTS);
    }

    @SuppressWarnings("unchecked")
    public List<Commit> searchCommits(SearchRequest request) throws GitLabApiException {
        return (List<Commit>) search(request, Constants.ProjectSearchScope.COMMITS);
    }

    @SuppressWarnings("unchecked")
    public List<Milestone> searchMilestone(SearchRequest request) throws GitLabApiException {
        return (List<Milestone>) search(request, Constants.ProjectSearchScope.MILESTONES);
    }
}
