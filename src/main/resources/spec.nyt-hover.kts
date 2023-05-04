import at.ac.tuwien.trustcps.dsl.*

@Language("css")
val cookieInfoText = ".gdpr > div:nth-child(2) p:nth-child(1)"

@Language("css")
val titles = "div h3"

@Language("css")
val titlesParents = "div:has(h3)"

@Language("css")
//val stocks = "#masthead-bar-one-widgets div"
val stocks = "div"

Spec.atoms(
    // Cookie popup info text (1.4.10 - Reflow)
    select { cookieInfoText },
    select { cookieInfoText } read "height" greaterThan 320,
    select { cookieInfoText } read "width" greaterThan 256,

    // Titles contrast ratio (1.4.6 - Contrast (Enhanced))
    select { titles } read "color" bind "titlesColor",
    select { titlesParents } read "background-color" bind "titlesColor" applying
            { value, bound ->
                val bgColor = value.asColor()
                val textColor = bound.asColor()
                textColor.contrastRatio(bgColor) > 4.5
            },

    // Stocks visible (2.3.1 - Three Flashes or Below Threshold)
    select { stocks } read "display" equalTo "none",

    select { ".welcomeAdLayout" }
// ReactModal__Body--open
//    select { "body" },  // [0]
//    select { ".btn-open-survey" },  // [0]
    //read "height"
//    select { ".left-bordered-title" },  // [1]
    // read "height"
//    select { ".colored-block__title.clearfix" },
    // .quick-search--input
//    select { "a[data-target='global-menu']" }
//    select { "p" },
//    select { ":is([data-testid='gdpr-dock'] p):nth-of-type(2)" } read "width" greaterThan 256,
//    select { ":is(.item-meta-row p):nth-of-type(1)" },
//    select { ".owl-item.active" } read "color" bind "bg",
//    select { ".owl-item.active" },
)

Spec.record(
    after { "click" },
    after { "touch" }
)

// -- Helper Formulae: --
val screen = Spec.screen

// Cookie popup info text (1.4.10 - Reflow)
val cookiePopupText = Spec.atoms[0]
val cookiePopupTextHeight = Spec.atoms[1]
val cookiePopupTextWidth = Spec.atoms[2]

// Titles contrast ratio (1.4.6 - Contrast (Enhanced))
val titlesColor = Spec.atoms[3]
val titlesParentsBgColor = Spec.atoms[4]

// Stocks visible (2.3.1 - Three Flashes or Below Threshold)
val stocksHidden = Spec.atoms[5]


val f1 = cookiePopupText implies
        (cookiePopupTextHeight and cookiePopupTextWidth)


val f2 = titlesColor and titlesParentsBgColor

val f3 = screen and not(stocksHidden) implies
        not(
            eventually(stocksHidden) within (Interval(0, 1))
                    and (eventually(not(stocksHidden)) within (Interval(0, 1))
                    )
        )


// Final formula
Spec.formula = globally (Spec.atoms[6]) within Interval(0, 1)

