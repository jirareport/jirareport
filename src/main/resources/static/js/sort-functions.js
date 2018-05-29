function sortByTask(a, b) {
    return a.task.split('-')[1] - b.task.split('-')[1];
}

function sortByDate(a, b) {
    return _parseDate(a.split("/")) - _parseDate(b.split("/"));
}

function _parseDate(part) {
    return Date.parse(part[1] + "/" + part[0] + "/" + part[2]);
}