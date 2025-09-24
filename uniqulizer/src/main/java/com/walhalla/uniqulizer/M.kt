package com.walhalla.uniqulizer

import java.io.File

object Uniqualizer {

    private const val projectRoot = "C:\\Users\\combo\\Desktop\\juicy-match"

    fun run() {
        println("=== Starting Uniqualizer Run ===")
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
            rules = mapOf(
                "" to "sound_effect_" // Default for all raw files
            ),
            extensionRules = mapOf(
                "mp3" to "music_"
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
        println("\n--- Processing directory: ${dir.path} ---")
        if (!dir.exists() || !dir.isDirectory) {
            println("ERROR: Directory not found or is not a directory.")
            return
        }

        dir.listFiles()?.forEach { file ->
            val oldName = file.nameWithoutExtension
            var newName: String? = null
            var nameForCodeUpdate: String = oldName

            val processedRegex = "(.+)_([a-z0-9]{6})".toRegex()
            val match = processedRegex.find(oldName)

            if (match != null) {
                // File has been processed before, just update the random part
                val (baseName, _) = match.destructured
                newName = "${baseName}_${generateRandomString(6)}"
            } else {
                // First time processing
                for ((oldPrefix, newPrefix) in rules) {
                    val prefixToApply = extensionRules[file.extension] ?: rules[""] ?: newPrefix
                    if (oldName.startsWith(oldPrefix)) {
                        val baseName = oldName.removePrefix(oldPrefix)
                        newName = "${prefixToApply}${baseName}_${generateRandomString(6)}"
                        break
                    }
                }
            }

            if (newName != null) {
                val newFile = File(file.parent, "$newName.${file.extension}")
                if (file.renameTo(newFile)) {
                    println("Renamed ${file.name} to ${newFile.name}")
                    updateCodeReferences(resourceType, nameForCodeUpdate, newName)
                } else {
                    println("ERROR: Failed to rename ${file.name}")
                }
            }
        }
    }

    private fun updateCodeReferences(resourceType: String, oldName: String, newName: String) {
        val oldReference = "R.$resourceType.$oldName"
        val newReference = "R.$resourceType.$newName"

        println("  Updating references from $oldReference to $newReference")
        File(projectRoot).walkTopDown().forEach { file ->
            if (file.isFile && (file.extension == "kt" || file.extension == "java")) {
                try {
                    var content = file.readText()
                    if (content.contains(oldReference)) {
                        content = content.replace(oldReference, newReference)
                        file.writeText(content)
                        println("    Updated reference in ${file.relativeTo(File(projectRoot))}")
                    }
                } catch (e: Exception) {
                    println("    ERROR: Could not process file ${file.path}: ${e.message}")
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
