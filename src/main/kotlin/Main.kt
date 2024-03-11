package org.example

import java.time.LocalDate
import java.time.format.DateTimeFormatter

val formatters = listOf(
    DateTimeFormatter.ofPattern("MMMM dd, yyyy"),
    DateTimeFormatter.ofPattern("MMM d, yyyy"),
    DateTimeFormatter.ofPattern("MM/dd/yyyy"),
    DateTimeFormatter.ofPattern("MM-dd-yyyy")
)

fun String.formatToDate(): String? {
    var parsedDate: String? = null

    for (formatter in formatters) {
        try {
            LocalDate.parse(this.removeCardinals(), formatter)
            parsedDate = this
            break
        } catch (e: Exception) {
            parsedDate = null
        }
    }
    return parsedDate
}

fun String.removeCardinals() =
    this.replace(Regex("(?<=\\d)(st|nd|rd|th)"), "")

fun findDates(text: String): List<String> {
    val dateRegex = """(?:(?:Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)[a-z]*[-./]?\s*\d{1,2}(?:st|nd|rd|th)?[-./,]?\s*\d{4})|\b\d{2}[/-]\d{2}[/-]\d{4}\b"""
    val matches = dateRegex.toRegex(RegexOption.IGNORE_CASE).findAll(text)
    val result = mutableListOf<String>()

    for (match in matches) {
        val date = match.groups[0]?.value?.capitalize() ?: continue

        date.formatToDate()?.let { result.add(it) }
    }

    return result
}

fun main() {
    val text = "The Mexican Revolution, which occurred from 1910 to 1920, was a pivotal period in Mexican history. " +
            "It began on November 20, 1910, with an armed uprising against President Porfirio Díaz, who had held power for over three decades. " +
            "The promulgation of the Mexican Constitution on Feb 5th, 1917, introduced significant reforms, addressing issues such as land reform, " +
            "workers' rights, and the separation of church and state. March 21st, 1917, marked the presidency of Venustiano Carranza, " +
            "whose tenure focused on consolidating power and implementing reforms. " +
            "The assassination of Emiliano Zapata on 04/10/1919, deeply impacted the revolution, particularly the agrarian movement. " +
            "Finally, on 05-21-1920, Álvaro Obregón led a successful revolt against Carranza's government, resulting in his presidency. " +
            "These important dates highlight key moments in the Mexican Revolution's trajectory, shaping the course of Mexico's future."

    val dates = findDates(text)
    println(dates.joinToString("\n"))
}