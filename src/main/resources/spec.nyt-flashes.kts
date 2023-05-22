/**
 * Stocks visible (2.3.1 - Three Flashes or Below Threshold)
 *
 * Trace To reproduce:
 * 1. No special action is required, just run and wait.
 *
 * Counterexamples can be observed at the end in the output folder,
 *    as `eval_*.png` snapshots.
 **/

import com.enniovisco.dsl.*

@Language("css")
val stocks = "#masthead-bar-one-widgets div"

Spec.atoms(
    select { stocks } read "display" equalTo "none"
)

Spec.record(
    every(500),
    after { "click" },
    after { "touch" }
)

// Helper Formulae
val screen = Spec.screen
val stocksHidden = Spec.atoms[0]

// Final formula
Spec.formula = screen and not(stocksHidden) implies
        not(
            eventually(stocksHidden) within (Interval(0, 1))
                    and (eventually(not(stocksHidden)) within (Interval(0, 1)))
        )
