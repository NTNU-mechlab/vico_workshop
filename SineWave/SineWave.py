from pythonfmu.fmi2slave import Fmi2Slave, Real, Fmi2Causality, Fmi2Variability
import math

TWO_PHI = 2 * math.pi


class SineWave(Fmi2Slave):

    def __init__(self, **kwargs):
        super().__init__(**kwargs)

        self.A = 1
        self.f = 0.1
        self.phi = 0

        self.output = 0.0

        self.register_variable(Real("A", causality=Fmi2Causality.parameter, variability=Fmi2Variability.tunable))
        self.register_variable(Real("f", causality=Fmi2Causality.parameter, variability=Fmi2Variability.tunable))
        self.register_variable(Real("phi", causality=Fmi2Causality.parameter, variability=Fmi2Variability.tunable))

        self.register_variable(Real("output", causality=Fmi2Causality.output))

    def update_output(self, t: float):
        self.output = self.A * math.sin(TWO_PHI * self.f * t + self.phi)

    def exit_initialization_mode(self):
        self.update_output(0)

    def do_step(self, current_time: float, step_size: float) -> bool:
        self.update_output(current_time)
        return True
