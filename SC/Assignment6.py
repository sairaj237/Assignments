import numpy as np
import skfuzzy as fuzz
from skfuzzy import control as ctrl
import matplotlib.pyplot as plt

# Define fuzzy variables
temperature = ctrl.Antecedent(np.arange(0, 41, 1), 'temperature')
humidity = ctrl.Antecedent(np.arange(0, 101, 1), 'humidity')
fan_speed = ctrl.Consequent(np.arange(0, 101, 1), 'fan_speed')

# Define membership functions for temperature
temperature['cold'] = fuzz.trimf(temperature.universe, [0, 0, 20])
temperature['moderate'] = fuzz.trimf(temperature.universe, [10, 20, 30])
temperature['hot'] = fuzz.trimf(temperature.universe, [20, 40, 40])

# Define membership functions for humidity
humidity['low'] = fuzz.trimf(humidity.universe, [0, 0, 50])
humidity['medium'] = fuzz.trimf(humidity.universe, [30, 50, 70])
humidity['high'] = fuzz.trimf(humidity.universe, [50, 100, 100])

# Define membership functions for fan speed
fan_speed['slow'] = fuzz.trimf(fan_speed.universe, [0, 0, 50])
fan_speed['medium'] = fuzz.trimf(fan_speed.universe, [25, 50, 75])
fan_speed['fast'] = fuzz.trimf(fan_speed.universe, [50, 100, 100])

# Define fuzzy rules
rule1 = ctrl.Rule(temperature['cold'] & humidity['low'], fan_speed['slow'])
rule2 = ctrl.Rule(temperature['cold'] & humidity['medium'], fan_speed['slow'])
rule3 = ctrl.Rule(temperature['cold'] & humidity['high'], fan_speed['medium'])
rule4 = ctrl.Rule(temperature['moderate'] & humidity['low'], fan_speed['medium'])
rule5 = ctrl.Rule(temperature['moderate'] & humidity['medium'], fan_speed['medium'])
rule6 = ctrl.Rule(temperature['moderate'] & humidity['high'], fan_speed['fast'])
rule7 = ctrl.Rule(temperature['hot'] & humidity['low'], fan_speed['fast'])
rule8 = ctrl.Rule(temperature['hot'] & humidity['medium'], fan_speed['fast'])
rule9 = ctrl.Rule(temperature['hot'] & humidity['high'], fan_speed['fast'])

# Create control system
fan_ctrl = ctrl.ControlSystem([rule1, rule2, rule3, rule4, rule5, 
                                rule6, rule7, rule8, rule9])
fan_simulation = ctrl.ControlSystemSimulation(fan_ctrl)

# Test the system
fan_simulation.input['temperature'] = 35
fan_simulation.input['humidity'] = 80
fan_simulation.compute()

print(f"Temperature: 35°C, Humidity: 80%")
print(f"Fan Speed: {fan_simulation.output['fan_speed']:.2f}%")

# Visualize membership functions
temperature.view()
humidity.view()
fan_speed.view()
plt.show()
