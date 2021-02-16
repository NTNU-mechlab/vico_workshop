from pythonfmu import Fmi2Causality, Fmi2Slave, Real, Fmi2Variability, Boolean



class Zigzag(Fmi2Slave):
    author = "Tongtong Wang"
    description = "Zigzag Controller"

    def __init__(self, **kwargs):
        super().__init__(**kwargs)
        # output
        self.rpmCommand = 0
        self.rudderCommand = 0
        # input
        self.heading = 0  # deg
        self.headingVelocity = 0
        # parameters
        self.rudderSetting = 0
        self.turnOverHeading = 0
        self.rpmSetting = 0
        self.enable = True
        self.rudder_turning_rate = 1  # deg/s
        self.rudder_maximum = 45  # deg

        self.register_variable(
            Real('rudder_turning_rate', causality=Fmi2Causality.parameter, variability=Fmi2Variability.fixed))
        self.register_variable(
            Real('rudder_maximum', causality=Fmi2Causality.parameter, variability=Fmi2Variability.fixed))
        self.register_variable(Real("rudderSetting", causality=Fmi2Causality.parameter, variability=Fmi2Variability.fixed))
        self.register_variable(Real("turnOverHeading", causality=Fmi2Causality.parameter, variability=Fmi2Variability.fixed))
        self.register_variable(Real("rpmSetting", causality=Fmi2Causality.parameter, variability=Fmi2Variability.fixed))
        self.register_variable(Boolean("enable", causality=Fmi2Causality.parameter, variability=Fmi2Variability.fixed))

        self.register_variable(Real("heading", causality=Fmi2Causality.input))
        self.register_variable(Real("headingVelocity", causality=Fmi2Causality.input))

        self.register_variable(Real("rpmCommand", causality=Fmi2Causality.output))
        self.register_variable(Real("rudderCommand", causality=Fmi2Causality.output))


    def exit_initialization_mode(self):
        self.rpmCommand = self.rpmSetting
        # self.rudderCommand = self.rudderSetting


    def do_step(self, current_time, step_size):
        h = step_size

        if self.enable:

            if current_time < 20:
                # self.rpmCommand = self.rpmSetting
                self.rudderCommand = 0
            else:

                if self.heading > 180:
                    self.heading = self.heading - 360

                if -self.turnOverHeading < self.heading < self.turnOverHeading:
                    if self.headingVelocity >= 0:
                        self.rudderCommand = self.rudderCommand - self.rudder_turning_rate * h
                        if self.rudderCommand < -self.rudderSetting:
                            self.rudderCommand = -self.rudderSetting
                    else:
                        self.rudderCommand = self.rudderCommand + self.rudder_turning_rate * h
                        if self.rudderCommand > self.rudderSetting:
                            self.rudderCommand = self.rudderSetting

                elif self.heading > self.turnOverHeading:
                    self.rudderCommand = self.rudderCommand + self.rudder_turning_rate * h
                    if self.rudderCommand > self.rudderSetting:
                        self.rudderCommand = self.rudderSetting

                elif self.heading < -self.turnOverHeading:
                    self.rudderCommand = self.rudderCommand - self.rudder_turning_rate * h
                    if self.rudderCommand < -self.rudderSetting:
                        self.rudderCommand = -self.rudderSetting



        return True


#   test algorithm
# slave = Zigzag(instance_name="instance")
# slave.setup_experiment(0.0)
# slave.heading = 30
# slave.headingVelocity = 0.1
# slave.rudderSetting = 20
# slave.turnOverHeading = 20
#
# for i in range(100):
#     slave.do_step(i, 1)
#     print(slave.rudderCommand)
# slave.terminate()
