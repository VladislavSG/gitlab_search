package enhanced.search.dto

sealed class SearchParam(
    var id: Long,
    var name: String
)

data class Group(
    private var _id: Long,
    private var _name: String,
) : SearchParam(_id, _name)

data class GroupType(
    private var _id: Long,
    private var _name: String,
) : SearchParam(_id, _name)

data class Repository(
    private var _id: Long,
    private var _name: String,
) : SearchParam(_id, _name)

data class Branch(
    private var _id: Long = -1,
    private var _name: String = "Any",
) : SearchParam(_id, _name)
