/**
 * Cookie popup info text (1.4.10 - Reflow)
 *
 * Trace To reproduce:
 * 1. No special action is required, just run and wait.
 *
 * Counterexamples can be observed at the end in the output folder,
 *    as `eval_*.png` snapshots.
 **/

import at.ac.tuwien.trustcps.dsl.*

@Language("css")
val cookieInfoText = ".gdpr > div:nth-child(2) p:nth-child(1)"

Spec.atoms(
    select { cookieInfoText },
    select { cookieInfoText } read "height" greaterThan 320,
    select { cookieInfoText } read "width" greaterThan 256,
)

Spec.record(
    every(1000),
    after { "click" },
    after { "touch" }
)

// Helper Formulae
val cookiePopupText = Spec.atoms[0]
val cookiePopupTextHeight = Spec.atoms[1]
val cookiePopupTextWidth = Spec.atoms[2]


// Final formula
Spec.formula = cookiePopupText implies
        (cookiePopupTextHeight and cookiePopupTextWidth)

