/**
 * Ad popup does not initiate a change of context (3.2.1 - On Focus)
 *
 * Trace To reproduce:
 * 1. As soon as the ad appears, hover with the mouse on the ad popup.
 * 2. Stay over it until it disappears.
 * 3. Click anywhere on the page (not on links or buttons) to force an event.
 *
 * Counterexamples can be observed at the end in the output folder,
 *    as `eval_*.png` snapshots.
 **/
import com.enniovisco.dsl.*

Spec.atoms(
    select { ".welcomeAdLayout:hover" },
    select { ".welcomeAdLayout" }
)

Spec.record(
    every(5000),
    after { "click" },
    after { "touch" }
)

// Helper Formulae
val popupIsSelected = Spec.atoms[0]
val popupIsPresent = Spec.atoms[1]


// Final formula
Spec.formula = popupIsSelected implies
        globally(not(popupIsSelected) or popupIsPresent)

