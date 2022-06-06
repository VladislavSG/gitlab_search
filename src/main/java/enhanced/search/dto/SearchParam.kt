package enhanced.search.dto


data class GroupType(
    var id: Long,
    var name: String,
)

data class Group(
    var id: Long,
    var name: String,
    var parentId: Long?
)

data class Project(
    var id: Long,
    var name: String,
    var parentId: Long
)

data class Branch(
    var name: String,
    var parentId: Long
) {
    override fun toString(): String {
        return "$name $parentId"
    }
    companion object {
        fun parse(fullname : String) : Branch {
            val words : List<String> = fullname.split(" ")
            return Branch(words[0], words[1].toLong())
        }
    }
}

val ANY_GROUP_TYPE = GroupType(-1, "Any Group Type")
val ANY_GROUP = Group(-1, "Any Group", -1)
val ANY_PROJECT = Project(-1, "Any Project", -1)
