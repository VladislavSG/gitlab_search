package enhanced.search.utils

import enhanced.search.dto.*
import java.text.SimpleDateFormat

val dateFormat = SimpleDateFormat("dd MMM yyyy")

fun List<GroupType>.findById(groupTypeId: Long?): GroupType? = this.find { groupTypeId == it.id }
fun List<Group>.findById(groupId: Long): Group = this.find { groupId == it.id }!!
fun List<Project>.findById(projectId: Long): Project = this.find { projectId == it.id }!!


fun org.gitlab4j.api.models.Issue.makeResponse(
    groupTypeName: String,
    groupName: String,
    projectName: String
) = Response(
    this.title,
    "$groupTypeName / $groupName / $projectName",
    dateFormat.format(this.createdAt) + " by ${this.author.name}"
)

fun org.gitlab4j.api.models.MergeRequest.makeResponse(
    groupTypeName: String,
    groupName: String,
    projectName: String
) = Response(
    this.title,
    "$groupTypeName / $groupName / $projectName / ${this.sourceBranch} -> ${this.targetBranch}",
    dateFormat.format(this.createdAt) + " by ${this.author.name}"
)

fun org.gitlab4j.api.models.SearchBlob.makeResponse(
    groupTypeName: String,
    groupName: String,
    projectName: String
) = Response(
    this.basename,
    "$groupTypeName / $groupName / $projectName",
    ""
)

fun org.gitlab4j.api.models.Commit.makeResponse(
    groupTypeName: String,
    groupName: String,
    projectName: String
) = Response(
    this.title,
    "$groupTypeName / $groupName / $projectName",
    dateFormat.format(this.createdAt) + " by ${this.author.name}"
)

fun org.gitlab4j.api.models.Note.makeResponse(
    noteableType: String
) = Response(
    this.body.makeShorter(),
    "$noteableType with id=${this.noteableId}",
    dateFormat.format(this.createdAt) + " by ${this.author.name}"
)

fun org.gitlab4j.api.models.Milestone.makeResponse(
    groupTypeName: String,
    groupName: String,
    projectName: String
) = Response(
    this.title,
    "$groupTypeName / $groupName / $projectName",
    dateFormat.format(this.createdAt)
)

fun org.gitlab4j.api.models.User.makeResponse(
    location: String,
) = Response(
    this.name,
    location,
    if (this.createdAt == null) "" else dateFormat.format(this.createdAt)
)

fun String.makeShorter(): String {
    return if (this.length < 63) this
    else "${this.substring(0, 60)}..."
}

fun String.makeUrl() = this.replaceFirst("c630bb74d5d2", "localhost")