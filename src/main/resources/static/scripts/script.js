const groupTypeSelect = document.getElementById("groupType")
const groupSelect = document.getElementById("groupId")
const projectSelect = document.getElementById("projectId")

function changeGroupType(selectBranches = true) {
    const groupTypeSet = getPossibleGroupTypes()
    disableGroups(groupTypeSet)

    changeGroup(true, selectBranches)
}

function changeGroup(flag = false, selectBranches = true) {
    const groupTypeId = groupTypeSelect.options[groupTypeSelect.selectedIndex].getAttribute("groupTypeId")
    if (flag && (groupTypeId === "-1" || groupSelect.options[groupSelect.selectedIndex].disabled)) {
        groupSelect.options[0].selected = true
    }

    const groupId = groupSelect.options[groupSelect.selectedIndex].value
    if (groupId !== "-1") goUp(true)

    const groupSet = getPossibleGroups()
    disableProjects(groupSet)

    changeProject(true, selectBranches)
}

function changeProject(flag = false, selectBranches = true) {

    const groupId = groupSelect.options[groupSelect.selectedIndex].value

    if (flag && (groupId === "-1" || projectSelect.options[projectSelect.selectedIndex].disabled))
        projectSelect.options[0].selected = true

    const repositoryId = projectSelect.options[projectSelect.selectedIndex].value
    if (repositoryId !== "-1") goUp()

    const projectSet = getPossibleProjects()

    disableBranches(projectSet, selectBranches)

    refreshAll()
}

function changeMask(selectMask) {
    const mask = document.getElementById("branchMask").value
    changeProject(true, false)
    document.querySelectorAll("#branches option").forEach(it => {
        if (it.disabled) return
        const curBranch = it.value
        if (!curBranch.includes(mask)) {
            it.disabled = true
            it.selected = false
        } else {
            it.selected = it.selected || selectMask
        }
    })
    refreshAll()
}

function getPossibleGroupTypes() {
    const groupTypeList = new Set()
    const groupTypeId = groupTypeSelect.options[groupTypeSelect.selectedIndex].getAttribute("groupTypeId")
    if (groupTypeId !== "-1") {
        groupTypeList.add(groupTypeId)
    } else {
        document.querySelectorAll("#groupType option").forEach(it => {
            const curId = it.getAttribute("groupTypeId")
            if (curId === "-1") return
            groupTypeList.add(curId)
        })
    }
    return groupTypeList
}

function disableGroups(groupTypeSet) {
    document.querySelectorAll("#groupId option").forEach(it => {
        const parentId = it.getAttribute("parentid")
        const curId = it.value
        if (curId === "-1") return
        it.disabled = !groupTypeSet.has(parentId)
    })
}

function getPossibleGroups() {
    const groupId = groupSelect.options[groupSelect.selectedIndex].value
    const groupList = new Set()

    if (groupId === "-1")
        document.querySelectorAll("#groupId option").forEach(it => {
            const curId = it.value
            if (curId === "-1") return
            if (!it.disabled) groupList.add(curId)
        })
    else {
        groupList.add(groupId)
    }
    return groupList
}

function disableProjects(groupSet) {
    document.querySelectorAll("#projectId option").forEach(it => {
        const parentId = it.getAttribute("parentid")
        const curId = it.value

        it.disabled = !groupSet.has(parentId) && curId !== "-1";
    })
}

function getPossibleProjects() {
    const repList = new Set()
    const repositoryId = projectSelect.options[projectSelect.selectedIndex].value
    if (repositoryId === "-1")
        document.querySelectorAll("#projectId option").forEach(it => {
            const curId = it.value
            if (curId === "-1") return
            if (!it.disabled) repList.add(curId)
        })
    else {
        repList.add(repositoryId)
    }
    return repList
}

function disableBranches(repSet, selectBranches) {
    document.querySelectorAll("#branches option").forEach(it => {
        const parentId = it.getAttribute("parentid")
        it.disabled = false
        it.selected = it.selected || selectBranches
        if (!repSet.has(parentId)) {
            it.disabled = true
            it.selected = false
        }
    })
}

function refreshAll() {
    $('#groupType').selectpicker('refresh')
    $('#groupId').selectpicker('refresh')
    $('#projectId').selectpicker('refresh')
    $('#branches').selectpicker('refresh')
}

function goUp(fromGroup = false) {
    const repositoryId = projectSelect.options[projectSelect.selectedIndex].value
    if (repositoryId !== "-1" && !fromGroup) {
        const repParent = projectSelect.options[projectSelect.selectedIndex].getAttribute("parentid")
        document.querySelectorAll("#groupId option").forEach(it => {
            const curId = it.value
            it.selected = repParent === curId
        })
    }

    const groupId = groupSelect.options[groupSelect.selectedIndex].value
    if (groupId !== "-1") {
        const groupParent = groupSelect.options[groupSelect.selectedIndex].getAttribute("parentid")
        document.querySelectorAll("#groupType option").forEach(it => {
            const curId = it.getAttribute("groupTypeId")
            it.selected = groupParent === curId
        })
    }
}

function loadPage() {
    changeGroupType(false)
    changeMask(false)
    chooseIssues()
}

function hideAll() {
    document.querySelectorAll(".scope").forEach(it => {
        it.style.display = "none"
    })
    document.querySelectorAll(".scope-label").forEach(it => {
        it.classList.remove("active")
    })
}

function chooseIssues() {
    hideAll()
    document.getElementById("issues").classList.add("active")
    document.querySelectorAll(".issues").forEach(it => {
        it.style.display = "block"
    })
}

function chooseMergeRequests() {
    hideAll()
    document.getElementById("merge-request").classList.add("active")
    document.querySelectorAll(".mergeRequests").forEach(it => {
        it.style.display = "block"
    })
}

function chooseMilestones() {
    hideAll()
    document.getElementById("milestones").classList.add("active")
    document.querySelectorAll(".milestones").forEach(it => {
        it.style.display = "block"
    })
}

function chooseUsers() {
    hideAll()
    document.getElementById("users").classList.add("active")
    document.querySelectorAll(".users").forEach(it => {
        it.style.display = "block"
    })
}

function chooseBlobs() {
    hideAll()
    document.getElementById("blobs").classList.add("active")
    document.querySelectorAll(".blobs").forEach(it => {
        it.style.display = "block"
    })
}

function chooseCommits() {
    hideAll()
    document.getElementById("commits").classList.add("active")
    document.querySelectorAll(".commits").forEach(it => {
        it.style.display = "block"
    })
}

function chooseWiki() {
    hideAll()
    document.getElementById("wiki").classList.add("active")
    document.querySelectorAll(".wiki").forEach(it => {
        it.style.display = "block"
    })
}

function chooseComments() {
    hideAll()
    document.getElementById("comments").classList.add("active")
    document.querySelectorAll(".comments").forEach(it => {
        it.style.display = "block"
    })
}