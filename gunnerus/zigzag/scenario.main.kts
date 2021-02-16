@file:Repository("https://dl.bintray.com/ntnu-ihb/mvn")
@file:DependsOn("no.ntnu.ihb.vico:core:0.3.3")

import no.ntnu.ihb.vico.dsl.scenario

//val tReset = 50.0

scenario {
    init {
//        real("vesselModel.slaveComponent.global_cur_dir").set(45)
//        real("vesselModel.slaveComponent.global_cur_vel").set(0.2)
//        real("vesselModel.slaveComponent.input_global_wind_dir").set(45)
//        real("vesselModel.slaveComponent.input_global_wind_vel").set(4)
//        real("vesselModel.slaveComponent.wave_height").set(1)
//        real("vesselModel.slaveComponent.peak_period").set(10)
//        real("vesselModel.slaveComponent.wave_direction").set(45)

        real("zigzagController.rudderSetting").set(10)
        real("zigzagController.turnOverHeading").set(10)
        real("zigzagController.rpmSetting").set(100)
        real("zigzagController.rudder_turning_rate").set(2)
    }
//    invokeAt(1000.0){
//        real("zigzagController.slaveComponent.headingControllerSaturation").set(20)
//        real("zigzagController.slaveComponent.turnOverHeading").set(20)
//    }
//    invokeAt(2000.0){
//        real("zigzagController.slaveComponent.headingControllerSaturation").set(30)
//        real("zigzagController.slaveComponent.turnOverHeading").set(30)
//    }
//    invokeAt(tReset) {
//        bool("vesselModel.reset_position").set(true)
//    }
//    invokeAt(tReset + 0.1) {
//        bool("vesselModel.reset_position").set(false)
//        bool("zigzagController.enable").set(true)
//    }

}
