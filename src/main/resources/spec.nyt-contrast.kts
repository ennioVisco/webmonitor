/**
 * Titles contrast ratio (1.4.6 - Contrast (Enhanced))
 *
 * Trace To reproduce:
 * 1. No special action is required, just run and wait.
 *
 * Counterexamples can be observed at the end in the output folder,
 *    as `eval_*.png` snapshots.
 **/

import at.ac.tuwien.trustcps.dsl.*

@Language("css")
val titles = "div h3"

@Language("css")
val titlesParents = "div:has(h3)"

Spec.atoms(
    select { titles } read "color" bind "titlesColor",
    select { titlesParents } read "background-color" bind "titlesColor" applying
            { value, bound ->
                val bgColor = value.asColor()
                val textColor = bound.asColor()
                textColor.contrastRatio(bgColor) > 4.5
            },
)

Spec.record(
    every(1000),
    after { "click" },
    after { "touch" }
)

// Helper Formulae
val titlesColor = Spec.atoms[0]
val titlesParentsBgColor = Spec.atoms[1]

// Final formula
Spec.formula = titlesColor and titlesParentsBgColor
