package enhanced.search.service;

import enhanced.search.dto.*;
import enhanced.search.utils.GroupTypes;
import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.GitLabApiException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

@Service
public class GitlabGetService {
    static final GroupTypes gt = new GroupTypes();
    private GitLabApi gitLabApi;

    @Value("${gitlab.url}")
    private String gitlabUrl;

    public void initToken(final String token){
        this.gitLabApi = new GitLabApi(gitlabUrl, token);
    }

    public List<Group> getGroups() throws GitLabApiException {
        return getGroups(null);
    }

    Stream<org.gitlab4j.api.models.Group> getGroupsStream(final String type) throws GitLabApiException {
        Stream<org.gitlab4j.api.models.Group> groups = gitLabApi
                .getGroupApi()
                .getGroupsStream();
        if (type != null && !type.isEmpty()) {
            final Predicate<org.gitlab4j.api.models.Group> predicateGroup = g ->
                    type.equals(gt.id2type.get(g.getId()).getName());
            groups = groups.filter(predicateGroup);
        }
        return groups;
    }

    Stream<org.gitlab4j.api.models.Group> getGroupsStream(final SearchRequest request) throws GitLabApiException {
        return getGroupsStream(request != null ? request.getGroupType() : null);
    }

    public List<Group> getGroups(final SearchRequest request) throws GitLabApiException {
        return getGroupsStream(request)
                .map(g -> new Group(g.getId(), g.getName(), gt.group2TypeId(g)))
                .toList();
    }

    public List<GroupType> getGroupTypes() {
        return gt.types;
    }

    public List<Project> getProjects() throws GitLabApiException {
        return getProjects(null);
    }

    public List<Project> getProjects(final SearchRequest request) throws GitLabApiException {
        List<org.gitlab4j.api.models.Group> groups;
        if (request != null && request.getGroupId() != -1L) {
            groups = Collections.singletonList(
                    gitLabApi
                            .getGroupApi()
                            .getGroup(request.getGroupId())
            );
        } else {
            groups = getGroupsStream(request).toList();
        }
        List<Project> projects = new ArrayList<>();
        for (org.gitlab4j.api.models.Group g : groups) {
            gitLabApi.getGroupApi()
                    .getProjectsStream(g)
                    .map(p -> new Project(p.getId(), p.getName(), g.getId()))
                    .forEach(projects::add);
        }
        projects.sort(Comparator.comparing(Project::getName));
        return projects;
    }

    public List<Branch> getBranches() {
        List<Branch> res = new ArrayList<>();
        try {
            for (Project p : getProjects()) {
                gitLabApi
                        .getRepositoryApi()
                        .getBranchesStream(p.getId())
                        .map(b -> new Branch(b.getName(), p.getId()))
                        .forEach(res::add);
            }
        } catch (GitLabApiException ignored) {
        }
        return res;
    }

    public List<Branch> getBranches(final SearchRequest request) throws GitLabApiException {
        long projectId = request.getProjectId();
        if (projectId != -1L) {
            return gitLabApi
                    .getRepositoryApi()
                    .getBranchesStream(projectId)
                    .map(b -> new Branch(b.getName(), projectId))
                    .toList();
        } else {
            return Collections.emptyList();
        }
    }
}
