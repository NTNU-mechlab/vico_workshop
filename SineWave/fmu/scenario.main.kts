@file:Repository("https://dl.bintray.com/ntnu-ihb/mvn")
@file:DependsOn("no.ntnu.ihb.vico:core:0.3.3")

import no.ntnu.ihb.vico.dsl.scenario
import kotlin.math.PI

scenario {

    invokeAt(10.0) {
        real("SineWave.f") *= 2
        real("SineWave.phi").set(PI/2)
    }

}
