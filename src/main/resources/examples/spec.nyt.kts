import com.enniovisco.dsl.*

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
val cookieInfoIsVisible = cookieInfo and screen
val learnMoreIsVisible = learnMoreButton and screen
val bothVisibleAtSameTime = cookieInfoIsVisible and learnMoreIsVisible
val alwaysVisible = globally(bothVisibleAtSameTime)

Spec.formula = alwaysVisible
