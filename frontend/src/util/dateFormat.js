export function dateToString(standardDateFormat) {
    const dateFormatForBackend = standardDateFormat.split("-").join("")
    return dateFormatForBackend
}