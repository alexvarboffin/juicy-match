package com.walhalla.uniqulizer

import java.io.File

object Uniqualizer {

    private const val projectRoot = "C:\\Users\\combo\\Desktop\\juicy-match"

    fun run() {
        println("=== Starting Uniqualizer Run (First Time on Clean Directory) ===")
        processDirectory(
            dir = File(projectRoot, "zsdk/src/main/res/layout"),
            resourceType = "layout",
            rules = mapOf(
                "dialog_" to "dialog_abc_",
                "activity_" to "match_activity_"
            )
        )
        processDirectory(
            dir = File(projectRoot, "zsdk/src/main/res/raw"),
            resourceType = "raw",
            rules = mapOf(), // No prefix-based rules for raw
            extensionRules = mapOf(
                "mp3" to "music_",
                "wav" to "sound_effect_"
            )
        )
        println("=== Uniqualizer Run Finished ===")
    }

    private fun processDirectory(
        dir: File,
        resourceType: String,
        rules: Map<String, String>,
        extensionRules: Map<String, String> = emptyMap()
    ) {
        println("\n--- Processing directory: " + dir.path + " ---")
        if (!dir.exists() || !dir.isDirectory) {
            println("ERROR: Directory not found or is not a directory.")
            return
        }

        dir.listFiles()?.forEach { file ->
            val oldName = file.nameWithoutExtension
            var baseName = oldName
            var prefix = ""

            if (resourceType == "layout") {
                for ((oldP, newP) in rules) {
                    if (oldName.startsWith(oldP)) {
                        prefix = newP
                        baseName = oldName.removePrefix(oldP)
                        break
                    }
                }
            } else if (resourceType == "raw") {
                prefix = extensionRules[file.extension] ?: "sound_effect_" // Default for others
                if (prefix == "sound_effect_") {
                    // No base name change if it's a default
                } else {
                    baseName = oldName // Keep full name if it's music
                }
            }

            val newName = if (prefix.isNotEmpty()) {
                if (resourceType == "raw" && extensionRules.containsValue(prefix)) {
                     "${prefix}${baseName}_${generateRandomString(6)}"
                } else {
                    "${prefix}${baseName}_${generateRandomString(6)}"
                }
            } else {
                "${baseName}_${generateRandomString(6)}"
            }

            // Correction for raw files where prefix is the new name
            val finalNewName = if (resourceType == "raw" && extensionRules.containsKey(file.extension)) {
                "${prefix}${oldName}_${generateRandomString(6)}"
            } else {
                newName
            }

            updateFile(file, oldName, finalNewName, resourceType)
        }
    }

    private fun updateFile(file: File, oldName: String, newName: String, resourceType: String) {
        val newFile = File(file.parent, newName + "." + file.extension)
        if (file.renameTo(newFile)) {
            println("Renamed " + file.name + " to " + newFile.name)
            updateCodeReferences(resourceType, oldName, newName)
        } else {
            println("ERROR: Failed to rename " + file.name)
        }
    }

    private fun updateCodeReferences(resourceType: String, oldName: String, newName: String) {
        val oldRef = "R." + resourceType + "." + oldName
        val newRef = "R." + resourceType + "." + newName

        println("  Updating references from " + oldRef + " to " + newRef)
        File(projectRoot).walkTopDown().forEach { file ->
            if (file.isFile && (file.extension == "kt" || file.extension == "java" || file.extension == "xml")) {
                try {
                    val content = file.readText()
                    if (content.indexOf(oldRef) != -1) {
                        val newContent = content.replace(oldRef, newRef)
                        file.writeText(newContent)
                        println("    Updated reference in " + file.relativeTo(File(projectRoot)))
                    }
                } catch (e: Exception) {
                    println("    ERROR: Could not process file " + file.path + ": " + e.message)
                }
            }
        }
    }

    private fun generateRandomString(length: Int): String {
        val allowedChars = ('a'..'z') + ('0'..'9')
        return (1..length)
            .map { allowedChars.random() }
            .joinToString("")
    }
}

class M {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Uniqualizer.run()
        }
    }
}