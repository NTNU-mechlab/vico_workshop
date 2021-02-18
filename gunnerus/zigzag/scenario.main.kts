@file:Repository("https://dl.bintray.com/ntnu-ihb/mvn")
@file:DependsOn("no.ntnu.ihb.vico:core:0.3.3")

import no.ntnu.ihb.vico.dsl.scenario

scenario {

    init {

        real("zigzagController.rudderSetting").set(10)
        real("zigzagController.turnOverHeading").set(10)
        real("zigzagController.rpmSetting").set(100)
        real("zigzagController.rudder_turning_rate").set(2)
    }

}
