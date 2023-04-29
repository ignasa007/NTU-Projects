import random
from scipy.stats import expon, norm

N_STATIONS = 20
N_CHANNELS = 10
CELL_DIAM = 2000

CALL_ARRIVAL_GEN = lambda: expon.rvs(loc=0.000025, scale=1.369792, size=1)[0]
CALL_DURATION_GEN = lambda: expon.rvs(loc=10.003952,scale=99.831949, size=1)[0]
CALL_STATION_GEN = lambda: random.randint(0, N_STATIONS-1)

CAR_POSITION_GEN = lambda: random.uniform(0, CELL_DIAM)
CAR_SPEED_GEN = lambda: norm.rvs(loc=33.353361, scale=2.505169, size=1)[0]
CAR_DIRECTION_GEN = lambda: random.choice((-1, +1))