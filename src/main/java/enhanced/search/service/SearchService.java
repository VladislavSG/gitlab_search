package enhanced.search.service;

import enhanced.search.dto.Branch;
import enhanced.search.dto.Group;
import enhanced.search.dto.GroupType;
import enhanced.search.dto.Repository;
import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.GitLabApiException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("searchService")
public class SearchService {
    private final GitLabApi gitLabApi;

    public SearchService() {
        this(new GitLabApi("http://localhost", "ETdrQeZweNu3yRgtDzxr"));
    }

    public SearchService(final GitLabApi gitLabApi) {
        this.gitLabApi = gitLabApi;
    }

    public List<Group> getGroups() throws GitLabApiException {
        return gitLabApi
                .getGroupApi()
                .getGroupsStream()
                .map(g -> new Group(g.getId(), g.getName()))
                .toList();
    }

    public List<GroupType> getGroupTypes() {
        return List.of();
    }

    public List<Repository> getRepositories() {
        return List.of();
    }

    public List<Branch> getBranches() {
        return List.of();
    }
}
