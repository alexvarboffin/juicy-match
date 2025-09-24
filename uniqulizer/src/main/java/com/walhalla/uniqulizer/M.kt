package com.walhalla.uniqulizer

import java.io.File

object LayoutUniqualizer {

    private const val projectRoot = "C:\\Users\\combo\\Desktop\\juicy-match"
    private const val module = "zsdk"
    private val layoutDir = File(projectRoot, "$module/src/main/res/layout")

    private val renamingMap = mapOf(
        "dialog_" to "dialog_abc_",
        "activity_" to "match_activity_"
    )

    fun run() {
        println("Starting layout refactoring...")
        renameLayoutFiles()
        println("Layout refactoring complete.")
    }

    private fun renameLayoutFiles() {
        layoutDir.listFiles()?.forEach {
            renamingMap.forEach { (oldPrefix, newPrefix) ->
                if (it.name.startsWith(oldPrefix)) {
                    val oldNameWithoutExtension = it.nameWithoutExtension
                    val newName = it.name.replaceFirst(oldPrefix, newPrefix)
                    val newFile = File(it.parent, newName)

                    if (it.renameTo(newFile)) {
                        println("Renamed ${it.name} to $newName")
                        updateCodeReferences(oldNameWithoutExtension, newName.removeSuffix(".xml"))
                    } else {
                        println("ERROR: Failed to rename ${it.name}")
                    }
                }
            }
        }
    }

    private fun updateCodeReferences(oldName: String, newName: String) {
        val oldReference = "." + oldName
        val newReference = "." + newName

        File(projectRoot).walkTopDown().forEach {
            if (it.isFile && (it.extension == "kt" || it.extension == "java" || it.extension == "xml")) {
                try {
                    var content = it.readText()
                    if (content.contains(oldReference)) {
                        content = content.replace(oldReference, newReference)
                        it.writeText(content)
                        println("  Updated reference in ${it.relativeTo(File(projectRoot))}")
                    }
                } catch (e: Exception) {
                    println("ERROR: Could not process file ${it.path}: ${e.message}")
                }
            }
        }
    }
}

object RawUniqualizer {

    private const val projectRoot = "C:\\Users\\combo\\Desktop\\juicy-match"
    private const val module = "zsdk"
    private val rawDir = File(projectRoot, "$module/src/main/res/raw")

    fun run() {
        println("Starting raw resources refactoring...")
        renameRawFiles()
        println("Raw resources refactoring complete.")
    }

    private fun renameRawFiles() {
        rawDir.listFiles()?.forEach {
            val oldNameWithoutExtension = it.nameWithoutExtension
            val newPrefix = when (it.extension) {
                "mp3" -> "music_"
                "wav" -> "sound_effect_"
                else -> ""
            }
            val newName = newPrefix + it.name
            val newFile = File(it.parent, newName)

            if (it.renameTo(newFile)) {
                println("Renamed ${it.name} to $newName")
                updateCodeReferences(oldNameWithoutExtension, newName.removeSuffix(".${it.extension}"))
            } else {
                println("ERROR: Failed to rename ${it.name}")
            }
        }
    }

    private fun updateCodeReferences(oldName: String, newName: String) {
        val oldReference = "R.raw.$oldName"
        val newReference = "R.raw.$newName"

        File(projectRoot).walkTopDown().forEach {
            if (it.isFile && (it.extension == "kt" || it.extension == "java")) {
                try {
                    var content = it.readText()
                    if (content.contains(oldReference)) {
                        content = content.replace(oldReference, newReference)
                        it.writeText(content)
                        println("  Updated reference in ${it.relativeTo(File(projectRoot))}")
                    }
                } catch (e: Exception) {
                    println("ERROR: Could not process file ${it.path}: ${e.message}")
                }
            }
        }
    }
}

class M {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            //LayoutUniqualizer.run()
            RawUniqualizer.run()
        }
    }
}