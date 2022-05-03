package enhanced.search.service;

import enhanced.search.dto.*;
import enhanced.search.utils.GroupTypes;
import kotlin.text.Regex;
import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.GitLabApiException;
import org.glassfish.jersey.internal.guava.Predicates;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

@Service("searchService")
public class GitlabGetService {
    private final GitLabApi gitLabApi;
    private final GroupTypes gt = new GroupTypes();

    public GitlabGetService() throws GitLabApiException {
        this(new GitLabApi("http://localhost", "rsQKVaBP1-RvtYGD4eTW"));
    }

    public GitlabGetService(final GitLabApi gitLabApi) {
        this.gitLabApi = gitLabApi;
    }

    public List<Group> getGroups() throws GitLabApiException {
        return getGroups(null);
    }

    private Stream<org.gitlab4j.api.models.Group> getGroupsStream(final SearchRequest request) throws GitLabApiException {
        Stream<org.gitlab4j.api.models.Group> groups = gitLabApi
                .getGroupApi()
                .getGroupsStream();
        if (request != null && request.getGroupType() != null) {
            final Predicate<String> predicateType = Predicates.equalTo(request.getGroupType());
            final Predicate<Long> predicateID = Predicates.compose(predicateType, gt.id2type::get);
            final Predicate<org.gitlab4j.api.models.Group> predicateGroup =
                    Predicates.compose(predicateID, org.gitlab4j.api.models.Group::getId);
            groups = groups.filter(predicateGroup);
        }
        return groups;
    }

    public List<Group> getGroups(final SearchRequest request) throws GitLabApiException {
        return getGroupsStream(request)
                .map(g -> new Group(g.getId(), g.getName()))
                .toList();
    }

    public List<GroupType> getGroupTypes() {
        return gt.types
                .stream()
                .map(t -> new GroupType(0, t))
                .toList();
    }

    public List<Project> getProjects() throws GitLabApiException {
        return getProjects(null);
    }

    public List<Project> getProjects(final SearchRequest request) throws GitLabApiException {
        List<org.gitlab4j.api.models.Group> groups;
        if (request != null && request.getGroupId() != null) {
            groups = Collections.singletonList(
                    gitLabApi
                            .getGroupApi()
                            .getGroup(request.getGroupId())
            );
        } else {
            groups = getGroupsStream(request).toList();
        } ;
        List<Project> projects = new ArrayList<>();
        for (org.gitlab4j.api.models.Group g : groups) {
            gitLabApi.getGroupApi()
                    .getProjectsStream(g)
                    .map(p -> new Project(p.getId(), p.getName()))
                    .forEach(projects::add);
        }
        projects.sort(Comparator.comparing(Project::getName));
        return projects;
    }



    public List<Branch> getBranches(final SearchRequest request) throws GitLabApiException {
        Long projectId = request.getProjectId();
        if (projectId != null) {
            return gitLabApi
                    .getRepositoryApi()
                    .getBranchesStream(projectId)
                    .map(b -> new Branch(0, b.getName()))
                    .toList();
        } else {
            return Collections.emptyList();
        }
    }

    public List<Branch> getBranches(final SearchRequest request, Regex regex) throws GitLabApiException {
        List<Branch> branches = getBranches(request);
        if (regex != null) {
            branches = branches
                    .stream()
                    .filter(Predicates.compose(regex::matches, Branch::getName))
                    .toList();
        }
        return branches;
    }
}
