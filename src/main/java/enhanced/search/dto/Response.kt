package enhanced.search.dto

open class Response(
    val title: String,
    val location: String,
    val createdDate: String,
    val url: String
)

class DataResponse(
    _title: String,
    val data: String,
    _location: String,
    _createdDate: String,
    _url: String,
) : Response(_title, _location, _createdDate, _url)

class GitLabUser(
    _title: String,
    val imgSrc: String,
    _location: String,
    _createdDate: String,
    _url: String,
) : Response(_title, _location, _createdDate, _url)