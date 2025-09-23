# Prompt: How to Apply Custom Fonts to a Jetpack Compose Application

## Objective

Your task is to apply all custom fonts found in the project to the entire Jetpack Compose application by setting a default font family in the app's theme. A key requirement is to ensure the font application is **unique** with each execution of this prompt.

## Instructions

### Step 1: Locate Custom Fonts

1.  Identify the custom font files included in the project.
2.  These are typically located in the `src/main/res/font` directory.
3.  List the names of the font files (e.g., `custom_font.ttf`, `trade_gothic_bold.ttf`, `baloo.ttf`).

### Step 2: Locate Theme Definition Files

1.  Find the theme definition files for the Jetpack Compose application.
2.  These are usually named `Theme.kt` and `Type.kt`.
3.  They are located in a directory similar to `src/main/java/com/package/name/ui/theme/`.

### Step 3: Define a Unique `FontFamily` in `Type.kt`

1.  Open the `Type.kt` file.
2.  Define a new `FontFamily` that includes all the fonts you located in Step 1.
3.  **Uniquification Requirement:** You MUST randomly assign the located fonts to different `FontWeight`s (e.g., `Normal`, `Bold`, `Medium`). The goal is to produce a different font-to-weight mapping each time this prompt is executed. You must use all available fonts.

**Example of two different unique outcomes:**

'''kotlin
// Outcome 1:
val AppFontFamily = FontFamily(
    Font(R.font.custom_font, FontWeight.Normal),
    Font(R.font.trade_gothic_bold, FontWeight.Bold),
    Font(R.font.baloo, FontWeight.Medium)
)

// Outcome 2 (different execution):
val AppFontFamily = FontFamily(
    Font(R.font.baloo, FontWeight.Normal),
    Font(R.font.custom_font, FontWeight.Bold),
    Font(R.font.trade_gothic_bold, FontWeight.Medium)
)
'''

### Step 4: Define `Typography` in `Type.kt`

1.  In the same `Type.kt` file, define a `Typography` object.
2.  This object will define the text styles for your application (e.g., `bodyLarge`, `titleLarge`).
3.  Set the `fontFamily` property of each text style to the unique `AppFontFamily` you created in the previous step.

**Example `Type.kt` content (continued):**

'''kotlin
import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp

// Define the Typography for the theme
val AppTypography = Typography(
    bodyLarge = TextStyle(
        fontFamily = AppFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    // Define other text styles (titleLarge, headlineSmall, etc.) in a similar way
    // ...
)
'''

### Step 5: Apply `Typography` in `Theme.kt`

1.  Open the `Theme.kt` file.
2.  Locate the `MaterialTheme` composable within your app's theme function (e.g., `YourAppTheme`).
3.  Add the `typography` parameter to the `MaterialTheme` call and pass the `AppTypography` object you created in `Type.kt`.

**Example `Theme.kt` modification:**

'''kotlin
// Before
MaterialTheme(
    colorScheme = DarkColorScheme,
    content = content
)

// After
MaterialTheme(
    colorScheme = DarkColorScheme,
    typography = AppTypography, // Add this line
    content = content
)
'''

## Summary

By following these steps, you will have successfully configured the application to use a **unique, randomly assigned combination** of the provided custom fonts as the default for all standard Jetpack Compose text components.