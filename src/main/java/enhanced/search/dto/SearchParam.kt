package enhanced.search.dto

sealed class SearchParam(
    var id: Long,
    var name: String
)

data class Group(
    private var _id: Long,
    private var _name: String,
    var parentId: Long
) : SearchParam(_id, _name)

data class GroupType(
    private var _id: Long,
    private var _name: String,
) : SearchParam(_id, _name)

data class Project(
    private var _id: Long,
    private var _name: String,
    var parentId: Long
) : SearchParam(_id, _name)

data class Branch(
    private var _id: Long,
    private var _name: String,
    var parentId: Long
) : SearchParam(_id, _name)

val ANY_GROUP = Group(-1, "Any Group", -1)
val ANY_GROUP_TYPE = GroupType(-1, "Any Group Type")
val ANY_PROJECT = Project(-1, "Any Project", -1)
val ANY_BRANCH = Branch(-1, "Any Branch", -1)