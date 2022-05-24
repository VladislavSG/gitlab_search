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
    this.title ?: "",
    "$groupTypeName / $groupName / $projectName",
    dateFormat.format(this.createdAt) + " by ${this.author.name}",
    this.webUrl?.makeUrl() ?: ""
)

fun org.gitlab4j.api.models.MergeRequest.makeResponse(
    groupTypeName: String,
    groupName: String,
    projectName: String
): Response {
    val author = this.author?.name
    val created = dateFormat.format(this.createdAt) + if (author == null) "" else " by $author"
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
) = Response(
    this.basename,
    "$groupTypeName / $groupName / $projectName",
    "",
    ""
)

fun org.gitlab4j.api.models.Commit.makeResponse(): Response {
    val author = this.author?.name
    val created = dateFormat.format(this.createdAt) + if (author == null) "" else " by $author"

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
    val author = this.author?.name
    val created = dateFormat.format(this.createdAt) + if (author == null) "" else " by $author"

    return Response(
        this.body?.makeShorter() ?: "...",
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

fun org.gitlab4j.api.models.User.makeResponse(
    location: String,
) = Response(
    this.name ?: "",
    location,
    if (this.createdAt == null) "" else dateFormat.format(this.createdAt),
    this.webUrl?.makeUrl() ?: ""
)

fun String.makeShorter(): String {
    return if (this.length < 63) this
    else "${this.substring(0, 60)}..."
}

fun String.makeUrl() = this.replaceFirst("http://c630bb74d5d2", "http://localhost")

fun User.getFullName() = (this.firstName ?: "") + " " + (this.secondName ?: "")
