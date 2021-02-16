@file:Repository("https://dl.bintray.com/ntnu-ihb/mvn")

@file:DependsOn("no.ntnu.ihb.sspgen:dsl:0.4.3")

import no.ntnu.ihb.sspgen.dsl.ssp
import kotlin.math.PI

ssp("gunnerus-zigzag-trajectory") {

    resources {
        url("https://github.com/gunnerus-case/gunnerus-fmus-bin/raw/master/VesselFmu2.fmu")
        url("https://github.com/gunnerus-case/gunnerus-fmus-bin/raw/master/PMAzimuth-proxy.fmu")
        pythonfmu("zigzag_controller.py")
    }

    ssd("KPN Twinship Gunnerus case") {

        system("gunnerus-trajectory") {

            elements {

                component("vesselModel", "resources/VesselFmu2.fmu") {
                    connectors {
                        real("additionalBodyForce[0].force.heave", input)
                        real("additionalBodyForce[0].force.surge", input)
                        real("additionalBodyForce[0].force.sway", input)
                        real("additionalBodyForce[0].pointOfAttackRel2APAndBL.xpos", input)
                        real("additionalBodyForce[0].pointOfAttackRel2APAndBL.ypos", input)
                        real("additionalBodyForce[0].pointOfAttackRel2APAndBL.zpos", input)

                        real("additionalBodyForce[1].force.heave", input)
                        real("additionalBodyForce[1].force.surge", input)
                        real("additionalBodyForce[1].force.sway", input)
                        real("additionalBodyForce[1].pointOfAttackRel2APAndBL.xpos", input)
                        real("additionalBodyForce[1].pointOfAttackRel2APAndBL.ypos", input)
                        real("additionalBodyForce[1].pointOfAttackRel2APAndBL.zpos", input)

                        real("cg_x_rel_ap", output)
                        real("cg_y_rel_cl", output)
                        real("cg_z_rel_bl", output)
                        real("cgShipMotion.nedDisplacement.north", output)
                        real("cgShipMotion.nedDisplacement.east", output)
                        real("cgShipMotion.linearVelocity.surge", output)
                        real("cgShipMotion.linearVelocity.sway", output)
                        real("cgShipMotion.angularVelocity.yaw", output)
                        real("cgShipMotion.angularDisplacement.yaw", output)
                        real("cgShipMotion.angularVelocity.yaw", output)
                        real("cgShipMotion.angularAcceleration.yaw", output)
                        real("cgShipMotion.linearAcceleration.surge", output)

                    }
                    parameterBindings {
                        parameterSet("initialValues") {
                            string("vesselZipFile", "%fmu%/resources/ShipModel-gunnerus-elongated.zip")
                            boolean("additionalBodyForce[0].enabled", true)
                            boolean("additionalBodyForce[1].enabled", true)
                        }
                    }
                }

                component("azimuth0", "resources/PMAzimuth-proxy.fmu") {
                    connectors {
                        real("input_act_revs", input)
                        real("input_act_angle", input)
                        real("input_cg_x_rel_ap", input)
                        real("input_cg_y_rel_cl", input)
                        real("input_cg_z_rel_bl", input)
                        real("input_cg_surge_vel", input)
                        real("input_cg_sway_vel", input)
                        real("input_yaw_vel", input)

                        real("output_torque", output)
                        real("output_force_heave", output)
                        real("output_force_surge", output)
                        real("output_force_sway", output)
                        real("output_x_rel_ap", output)
                        real("output_y_rel_cl", output)
                        real("output_z_rel_bl", output)
                    }
                    parameterBindings {
                        parameterSet("initialValues") {
                            real("input_x_rel_ap", 0)
                            real("input_y_rel_cl", -2.7)
                            real("input_z_rel_bl", 0.55)
                            real("input_prop_diam", 1.9)
                            real("input_distancetohull", 1.5)
                            real("input_bilgeradius", 3)
                            real("input_rho", 1025)
                            real("input_lpp", 33.9)
                        }
                    }
                }

                component("azimuth1", "resources/PMAzimuth-proxy.fmu") {
                    connectors {
                        copyFrom("azimuth0")
                    }
                    parameterBindings {
                        copyFrom("azimuth0", "initialValues") {
                            real("input_y_rel_cl", 2.7)
                        }
                    }
                }

                component("zigzagController", "resources/Zigzag.fmu"){
                    connectors {
                        real("heading", input)
                        real("headingVelocity", input)

                        real("rudderCommand", output)
                        real("rpmCommand", output)
                    }
                    parameterBindings {
                        parameterSet("initialValues"){
                            boolean("enable", true)
                        }
                    }
                }

            }

            connections(inputsFirst = true) {
                "zigzagController.heading" to "vesselModel.cgShipMotion.angularDisplacement.yaw"
                "zigzagController.headingVelocity" to "vesselModel.cgShipMotion.angularVelocity.yaw"

                "azimuth0.input_act_revs" to "zigzagController.rpmCommand"
                "azimuth0.input_act_angle" to "zigzagController.rudderCommand"
                "azimuth0.input_cg_x_rel_ap" to "vesselModel.cg_x_rel_ap"
                "azimuth0.input_cg_y_rel_cl" to "vesselModel.cg_y_rel_cl"
                "azimuth0.input_cg_z_rel_bl" to "vesselModel.cg_z_rel_bl"
                "azimuth0.input_cg_surge_vel" to "vesselModel.cgShipMotion.linearVelocity.surge"
                "azimuth0.input_cg_sway_vel" to "vesselModel.cgShipMotion.linearVelocity.sway"
                "azimuth0.input_yaw_vel" to "vesselModel.cgShipMotion.angularVelocity.yaw"

                "azimuth1.input_act_revs" to "zigzagController.rpmCommand"
                "azimuth1.input_act_angle" to "zigzagController.rudderCommand"
                "azimuth1.input_cg_x_rel_ap" to "vesselModel.cg_x_rel_ap"
                "azimuth1.input_cg_y_rel_cl" to "vesselModel.cg_y_rel_cl"
                "azimuth1.input_cg_z_rel_bl" to "vesselModel.cg_z_rel_bl"
                "azimuth1.input_cg_surge_vel" to "vesselModel.cgShipMotion.linearVelocity.surge"
                "azimuth1.input_cg_sway_vel" to "vesselModel.cgShipMotion.linearVelocity.sway"
                "azimuth1.input_yaw_vel" to "vesselModel.cgShipMotion.angularVelocity.yaw"

                "vesselModel.additionalBodyForce[0].force.heave" to "azimuth0.output_force_heave"
                "vesselModel.additionalBodyForce[0].force.surge" to "azimuth0.output_force_surge"
                "vesselModel.additionalBodyForce[0].force.sway" to "azimuth0.output_force_sway"
                "vesselModel.additionalBodyForce[0].pointOfAttackRel2APAndBL.xpos" to "azimuth0.output_x_rel_ap"
                "vesselModel.additionalBodyForce[0].pointOfAttackRel2APAndBL.ypos" to "azimuth0.output_y_rel_cl"
                "vesselModel.additionalBodyForce[0].pointOfAttackRel2APAndBL.zpos" to "azimuth0.output_z_rel_bl"

                "vesselModel.additionalBodyForce[1].force.heave" to "azimuth1.output_force_heave"
                "vesselModel.additionalBodyForce[1].force.surge" to "azimuth1.output_force_surge"
                "vesselModel.additionalBodyForce[1].force.sway" to "azimuth1.output_force_sway"
                "vesselModel.additionalBodyForce[1].pointOfAttackRel2APAndBL.xpos" to "azimuth1.output_x_rel_ap"
                "vesselModel.additionalBodyForce[1].pointOfAttackRel2APAndBL.ypos" to "azimuth1.output_y_rel_cl"
                "vesselModel.additionalBodyForce[1].pointOfAttackRel2APAndBL.zpos" to "azimuth1.output_z_rel_bl"
            }

        }

    }

}.build()
