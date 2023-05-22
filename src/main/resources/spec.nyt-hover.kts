/**
 * Secondary navigation is hoverable (1.4.13 - Content on Hover or Focus)
 *
 * Trace To reproduce:
 * 1. Click on the navigation menu
 * 2. Hover over some link of the menu
 * 3. Wait and check the results
 *
 * Counterexamples can be observed at the end in the output folder,
 *    as `eval_*.png` snapshots.
 **/

import com.enniovisco.dsl.*

@Language("css")
val secondaryNav = "#secondaryNav"

@Language("css")
val primaryMenuItem = "nav li:hover"

Spec.atoms(
    select { primaryMenuItem },
    select { secondaryNav }
)

Spec.record(
    every(15_000),
    after { "click" },
    after { "touch" }
)

// Helper Formulae
val primaryMenuItemIsSelected = Spec.atoms[0]
val secondaryMenuIsPresent = Spec.atoms[1]

val secondaryMenuIsClose = somewhere(secondaryMenuIsPresent)
val secondaryMenuAppears = globally(secondaryMenuIsClose) within Interval(0, 1)

// Final formula
Spec.formula = primaryMenuItemIsSelected implies secondaryMenuAppears
