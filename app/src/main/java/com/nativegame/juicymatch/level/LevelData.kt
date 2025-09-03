package com.nativegame.juicymatch.level

/**
 * Created by Oscar Liang on 2022/02/23
 */
class LevelData(
    val level: Int,
    val row: Int,
    val column: Int,
    var move: Int,
    var fruitCount: Int,
//========================================================
    //--------------------------------------------------------
    // Getter and Setter
    //--------------------------------------------------------
    var grid: String?,
    tile: String?,
    ice: String?,
    honey: String?,
    sand: String?,
    shell: String?,
    lock: String?,
    entry: String?,
    generator: String?,
    tutorialHint: String?,
    tutorialType: String?,
    targetType: String?,
    targetCount: String?
) {
    val ice: String?
    val tile: String?
    val lock: String?
    val entry: String?
    val honey: String?
    val sand: String?
    val shell: String?
    val generator: String?
    val tutorialHint: String?
    val tutorialType: TutorialType
    val targetTypes: MutableList<TargetType?>
    val targetCounts: MutableList<Int?>

    var score: Int = 0
    var star: Int = 0

    //--------------------------------------------------------
    // Constructors
    //--------------------------------------------------------
    init {
        this.grid = grid
        this.tile = tile
        this.ice = ice
        this.honey = honey
        this.sand = sand
        this.shell = shell
        this.lock = lock
        this.entry = entry
        this.generator = generator
        this.tutorialHint = tutorialHint
        this.tutorialType = getTutorialType(tutorialType)
        this.targetTypes = getTargetTypes(targetType)
        this.targetCounts = getTargetCounts(targetCount)
    }

    //========================================================
    //--------------------------------------------------------
    // Methods
    //--------------------------------------------------------
    private fun getTutorialType(text: String?): TutorialType {
        if (text == null) {
            // If text is null means no tutorial in current level
            return TutorialType.NONE
        }

        when (text) {
            "match_3" -> return TutorialType.MATCH_3
            "match_4" -> return TutorialType.MATCH_4
            "match_t" -> return TutorialType.MATCH_T
            "match_l" -> return TutorialType.MATCH_L
            "match_5" -> return TutorialType.MATCH_5
            "combine" -> return TutorialType.COMBINE
            "lock" -> return TutorialType.LOCK
            "cookie" -> return TutorialType.COOKIE
            "cake" -> return TutorialType.CAKE
            "candy" -> return TutorialType.CANDY
            "pie" -> return TutorialType.PIE
            "ice" -> return TutorialType.ICE
            "honey" -> return TutorialType.HONEY
            "starfish" -> return TutorialType.STARFISH
            "shell" -> return TutorialType.SHELL
            "pipe" -> return TutorialType.PIPE
            "generator" -> return TutorialType.GENERATOR
            "hammer" -> return TutorialType.HAMMER
            "bomb" -> return TutorialType.BOMB
            "glove" -> return TutorialType.GLOVE
            else -> throw IllegalArgumentException("TutorialType not found!")
        }
    }

    private fun getTargetTypes(text: String?): MutableList<TargetType?> {
        var text = text
        val targetTypes: MutableList<TargetType?> = ArrayList<TargetType?>()
        text = text + " "
        var preIndex = 0
        val size = text.length
        for (i in 0..<size) {
            val c = text.get(i)
            if (c == ' ') {
                val s = text.substring(preIndex, i)
                var type: TargetType? = null
                when (s) {
                    ("strawberry") -> type = TargetType.STRAWBERRY
                    ("cherry") -> type = TargetType.CHERRY
                    ("lemon") -> type = TargetType.LEMON
                    ("cookie") -> type = TargetType.COOKIE
                    ("cake") -> type = TargetType.CAKE
                    ("pie") -> type = TargetType.PIE
                    ("candy") -> type = TargetType.CANDY
                    ("ice") -> type = TargetType.ICE
                    ("lock") -> type = TargetType.LOCK
                    ("starfish") -> type = TargetType.STARFISH
                    ("shell") -> type = TargetType.SHELL
                    ("honey") -> type = TargetType.HONEY
                }
                targetTypes.add(type)

                preIndex = i + 1
            }
        }

        return targetTypes
    }

    private fun getTargetCounts(text: String?): MutableList<Int?> {
        var text = text
        val targetNums: MutableList<Int?> = ArrayList<Int?>()
        text = text + " "
        var preIndex = 0
        val size = text.length
        for (i in 0..<size) {
            val c = text.get(i)
            if (c == ' ') {
                val s = text.substring(preIndex, i)
                targetNums.add(s.toInt())
                preIndex = i + 1
            }
        }

        return targetNums
    } //========================================================
}
