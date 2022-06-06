package enhanced.search.utils

import enhanced.search.dto.*
import java.text.SimpleDateFormat
import org.gitlab4j.api.models.Author
import org.gitlab4j.api.models.Issue

object SearchUtils {
    const val badUrl = "http://c630bb74d5d2"
    var gitlabUrl = "http://localhost"
}


val dateFormat = SimpleDateFormat("dd MMM yyyy")

fun List<GroupType>.findById(groupTypeId: Long?): GroupType? = this.find { groupTypeId == it.id }
fun List<Group>.findById(groupId: Long): Group = this.find { groupId == it.id }!!
fun List<Project>.findById(projectId: Long): Project = this.find { projectId == it.id }!!
fun getAuthorName(a: Author?): String = if (a == null) "" else " by ${a.name} @${a.username}"
fun Issue.getIssueStatus() = if (this.state != null) "(${this.state.toValue().lowercase()}) " else null

fun Issue.makeResponse(
    groupTypeName: String,
    groupName: String,
    projectName: String
) = DataResponse(
    (this.getIssueStatus() ?: "") + (this.title ?: ""),
    this.description ?: "",
    "$groupTypeName / $groupName / $projectName",
    dateFormat.format(this.createdAt) + getAuthorName(this.author),
    this.webUrl?.makeUrl() ?: ""
)

fun org.gitlab4j.api.models.MergeRequest.makeResponse(
    groupTypeName: String,
    groupName: String,
    projectName: String
): Response {
    val created = dateFormat.format(this.createdAt) + getAuthorName(this.author)
    val fromBranch = this.sourceBranch
    val toBranch = this.targetBranch
    val merge = if (fromBranch == null || toBranch == null) "" else "/ $fromBranch -> $toBranch"

    return Response(
        this.title ?: "",
        "$groupTypeName / $groupName / $projectName $merge",
        created,
        this.webUrl?.makeUrl() ?: ""
    )
}

fun org.gitlab4j.api.models.SearchBlob.makeResponse(
    groupTypeName: String,
    groupName: String,
    projectName: String
) = DataResponse(
    this.filename,
    this.data.makeShorter(120),
    "$groupTypeName / $groupName / $projectName / ${this.ref}",
    "",
    formBlobUrl(groupName, projectName, this.ref, this.basename, this.filename)
)

fun org.gitlab4j.api.models.Commit.makeResponse(): Response {
    var authorName = getAuthorName(this.author)
    if (authorName.isEmpty() && this.authorName != null) authorName = " by ${this.authorName}"

    val created = dateFormat.format(this.createdAt) + authorName
    return Response(
        this.title ?: "",
        "",
        created,
        this.webUrl?.makeUrl() ?: ""
    )
}

fun org.gitlab4j.api.models.Note.makeResponse(
    noteableType: String
): Response {
    val created = dateFormat.format(this.createdAt) + getAuthorName(this.author)

    return Response(
        this.body?.makeShorter(60) ?: "...",
        "$noteableType with id=${this.noteableId}",
        created,
        ""
    )
}

fun org.gitlab4j.api.models.Milestone.makeResponse(
    groupTypeName: String,
    groupName: String,
    projectName: String
) = Response(
    this.title ?: "",
    "$groupTypeName / $groupName / $projectName",
    dateFormat.format(this.createdAt),
    ""
)

fun org.gitlab4j.api.models.User.makeResponse() = GitLabUser(
    "${this.name ?: ""} @${this.username ?: ""}",
    this.avatarUrl ?: "",
    "",
    if (this.confirmedAt == null) "" else dateFormat.format(this.confirmedAt),
    this.webUrl?.makeUrl() ?: ""
)

fun String.makeShorter(len: Int): String {
    return if (this.length < len + 3) this
    else "${this.substring(0, len)}..."
}

fun String.makeUrl() = this.replaceFirst(SearchUtils.badUrl, SearchUtils.gitlabUrl)

fun formBlobUrl(
    groupName: String,
    projectName: String,
    branch: String,
    basename: String,
    filename: String
): String {
    return "${SearchUtils.gitlabUrl}/${groupName.replace(" ", "-")}/${projectName.replace(" ", "-")}/-/blob/$branch/${merge(basename, filename)}"
}

fun merge(basename: String, filename: String): String {
    val dotInd = filename.indexOfFirst { it == '.' }
    return if (dotInd == -1) basename
    else basename + filename.substring(dotInd)
}

fun User.getFullName() = (this.firstName ?: "") + " " + (this.secondName ?: "")
