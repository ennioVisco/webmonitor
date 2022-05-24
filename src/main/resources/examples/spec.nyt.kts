import at.ac.tuwien.trustcps.*
import eu.quanticol.moonlight.formula.classic.*
import eu.quanticol.moonlight.formula.temporal.*

Spec.atoms = listOf(
    select { "#dfp-ad-top" }, 
    select { ".gdpr" }
)

//Spec.record = listOf(
//    after { "keydown" }
//)

val screen = Spec.screen
val learnMoreButton = Spec.atoms[0]
val cookieInfo = Spec.atoms[1]
val cookieInfoIsVisible = AndFormula(cookieInfo, screen)
val learnMoreIsVisible = AndFormula(learnMoreButton, screen)
val bothVisibleAtSameTime = AndFormula(cookieInfoIsVisible, learnMoreIsVisible)
//val alwaysVisible = GloballyFormula(bothVisibleAtSameTime)

Spec.formula = cookieInfoIsVisible
