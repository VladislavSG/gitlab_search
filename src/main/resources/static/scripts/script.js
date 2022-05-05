const groupTypeSelect = document.getElementById("groupTypeId")
const groupSelect = document.getElementById("groupId")
const repositorySelect = document.getElementById("repositoryId")

function changeGroupType() {
    const groupTypeList = new Set()
    const groupTypeId = groupTypeSelect.options[groupTypeSelect.selectedIndex].value
    if (groupTypeId === "-1")
        document.querySelectorAll("#groupTypeId option").forEach(it => {
            const curId = it.value
            if (curId === "-1") return
            if (!it.disabled) groupTypeList.add(curId)
        })
    else groupTypeList.add(groupTypeId)
    document.querySelectorAll("#groupId option").forEach(it => {
        const parentId = it.getAttribute("parentid")
        const curId = it.value

        it.disabled = !groupTypeList.has(parentId) && curId !== "-1";
    })
    changeGroup()
}

function changeGroup() {
    const groupList = new Set()
    if (groupSelect.options[groupSelect.selectedIndex].disabled) {
        groupSelect.options[0].selected = true
    }
    const groupId = groupSelect.options[groupSelect.selectedIndex].value
    if (groupId === "-1")
        document.querySelectorAll("#groupId option").forEach(it => {
            const curId = it.value
            if (curId === "-1") return
            if (!it.disabled) groupList.add(curId)
        })
    else groupList.add(groupId)
    document.querySelectorAll("#repositoryId option").forEach(it => {
        const parentId = it.getAttribute("parentid")
        const curId = it.value

        it.disabled = !groupList.has(parentId) && curId !== "-1";
    })
    changeProject()
}

function changeProject() {
    const repList = new Set()
    let repositoryId = "-1"
    if (repositorySelect.options[repositorySelect.selectedIndex].disabled) {
        repositorySelect.options[0].selected = true
    } else {
        repositoryId = repositorySelect.options[repositorySelect.selectedIndex].value
    }
    if (repositoryId === "-1")
        document.querySelectorAll("#repositoryId option").forEach(it => {
            const curId = it.value
            if (curId === "-1") return
            if (!it.disabled) repList.add(curId)
        })
    else repList.add(repositoryId)
    document.querySelectorAll("#branches option").forEach(it => {
        const parentId = it.getAttribute("parentid")
        it.disabled = false
        it.selected = true
        if (!repList.has(parentId)) {
            it.disabled = true
            it.selected = false
        }
    })
    $('#groupId').selectpicker('refresh')
    $('#repositoryId').selectpicker('refresh')
    $('#branches').selectpicker('refresh')
}

function chooseProject() {

}

function chooseIssues() {

}

function chooseMergeRequests() {

}

function chooseMilestones() {

}

function chooseUsers() {

}

function chooseBlobs() {

}

function chooseCommits() {

}
