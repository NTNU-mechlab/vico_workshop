@file:Repository("https://dl.bintray.com/ntnu-ihb/mvn")
@file:DependsOn("no.ntnu.ihb.sspgen:dsl:0.4.3")

import no.ntnu.ihb.sspgen.dsl.ssp

ssp("SineWave") {

    resources {
        pythonfmu("../SineWave.py")
    }

    ssd("SineWave") {

        system("SineWave system") {

            elements {

                component("SineWave", "resources/SineWave.fmu") {

                    connectors {
                        real("output", output)
                    }

                    parameterBindings {
                        parameterSet("initialValues") {
                            real("A", 2.0)
                        }
                    }

                }

            }

        }

    }

}.build()
