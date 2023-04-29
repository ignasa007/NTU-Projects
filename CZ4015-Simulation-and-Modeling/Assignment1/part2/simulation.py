from heapq import heappush, heappop

from config import *
from events import *


class Call:

    def __init__(self, start_time, end_time, station, channel, car_position, car_speed, car_direction):

        self.start_time = start_time
        self.end_time = end_time
        self.curr_station = station
        self.channel = channel
        self.car_position = car_position
        self.car_speed = car_speed
        self.car_direction = car_direction


class Station:

    def __init__(self, n_channels, n_reserved=0):

        self.occupied = [False for _ in range(n_channels)]
        self.n_reserved = n_reserved

    def available_channel(self, init):

        if (init and not all(self.occupied[:len(self.occupied)-self.n_reserved])) or (not init and not all(self.occupied)):
            return self.occupied.index(False)
        else:
            return None
        
    def allocate_channel(self, idx):

        self.occupied[idx] = True

    def free_channel(self, idx):

        self.occupied[idx] = False


class Simulator:

    def __init__(self, n_stations, n_channels, n_reserved):

        self.stations = [Station(n_channels, n_reserved) for _ in range(n_stations)]
        self.fel = list()

        self.init_stats()

    def init_stats(self):

        self.clock = 0
        self.total_calls = 0
        self.blocked_calls = 0
        self.dropped_calls = 0
        self.finished_calls = 0

    def schedule_call_init_event(self, time):

        event = CallInitiationEvent(time)
        heappush(self.fel, (time, event))

    def schedule_call_handover_event(self, time, call):

        event = CallHandoverEvent(time, call)
        heappush(self.fel, (time, event))

    def schedule_call_termination_event(self, time, call):

        event = CallTerminationEvent(time, call)
        heappush(self.fel, (time, event))

    def schedule_handover_or_termination(self, call):

        distance_till_exit = call.car_position if call.car_direction == -1 else CELL_DIAM-call.car_position
        time_till_exit = distance_till_exit / call.car_speed

        if call.end_time-self.clock <= time_till_exit:
            self.schedule_call_termination_event(call.end_time, call)
        elif (call.curr_station == 0 and call.car_direction == -1) or (call.curr_station == N_STATIONS-1 and call.car_direction == 1):
                self.schedule_call_termination_event(self.clock+time_till_exit, call)
        else:
            self.schedule_call_handover_event(self.clock+time_till_exit, call)

    def handle_call_init_event(self, event):

        self.clock = event.time
        self.total_calls += 1

        next_arrival_time = self.clock + CALL_ARRIVAL_GEN()
        self.schedule_call_init_event(next_arrival_time)

        base_station = CALL_STATION_GEN()
        channel = self.stations[base_station].available_channel(init=True)

        if channel is None:
            self.blocked_calls += 1
            return
        
        call_duration = CALL_DURATION_GEN()
        car_position = CAR_POSITION_GEN()
        car_speed = CAR_SPEED_GEN()
        car_direction = CAR_DIRECTION_GEN()

        call = Call(self.clock, self.clock+call_duration, base_station, channel, car_position, car_speed, car_direction)
        self.stations[base_station].allocate_channel(channel)

        self.schedule_handover_or_termination(call)

    def handle_call_handover_event(self, event):

        self.clock, call = event.time, event.call
        self.stations[call.curr_station].free_channel(call.channel)

        next_station = call.curr_station + call.car_direction
        channel = self.stations[next_station].available_channel(init=False)

        if channel is None:
            self.dropped_calls += 1
            return
        
        self.stations[next_station].allocate_channel(channel)
        call.curr_station = next_station
        call.channel = channel
        call.car_position = CELL_DIAM if call.car_direction == -1 else 0

        self.schedule_handover_or_termination(call)

    def handle_call_termination_event(self, event):

        self.clock, call = event.time, event.call
        self.stations[call.curr_station].free_channel(call.channel)

        self.finished_calls += 1

    def one_step(self):

        _, event = heappop(self.fel)
        if isinstance(event, CallInitiationEvent):
            self.handle_call_init_event(event)
        elif isinstance(event, CallHandoverEvent):
            self.handle_call_handover_event(event)
        elif isinstance(event, CallTerminationEvent):
            self.handle_call_termination_event(event)

    def run(self, warm_up=0, termination_cond=10_000):

        self.schedule_call_init_event(CALL_ARRIVAL_GEN())
        for _ in range(warm_up):
            self.one_step()

        self.init_stats()
        while self.finished_calls < termination_cond:
            self.one_step()


if __name__ == '__main__':

    from tqdm import tqdm
    
    import numpy as np
    import pandas as pd
    import matplotlib.pyplot as plt

    FONTDICT = {
        "family": "serif", 
        "color": "darkred", 
        "weight": "normal", 
        "size": 12
    }

    t_blocked_calls, t_dropped_calls = list(), list()
    for reserved in range(10):
        r_blocked_calls, r_dropped_calls = list(), list()
        for run in tqdm(range(20)):
            s = Simulator(N_STATIONS, N_CHANNELS, reserved)
            s.run(warm_up=15_000)
            r_blocked_calls.append(100*s.blocked_calls/s.total_calls); r_dropped_calls.append(100*s.dropped_calls/s.total_calls)
        t_blocked_calls.append((np.mean(r_blocked_calls), np.std(r_blocked_calls)))
        t_dropped_calls.append((np.mean(r_dropped_calls), np.std(r_dropped_calls)))

    plt.figure(figsize=(6, 4))
    plt.errorbar(range(10), [mean for mean, std in t_blocked_calls], yerr=[std for mean, std in t_blocked_calls], capsize=5, color='blue', label='blocked calls')
    plt.errorbar(range(10), [mean for mean, std in t_dropped_calls], yerr=[std for mean, std in t_dropped_calls], capsize=5, color='green', label='dropped calls')
    plt.xlabel('number of reserved channels', fontdict=FONTDICT, labelpad=12)
    plt.ylabel('percentages', fontdict=FONTDICT, labelpad=12)
    plt.legend(loc='center left', bbox_to_anchor=(1, 0.5))
    plt.grid()
    plt.savefig('assets/results/results.png', bbox_inches='tight')

    df = pd.DataFrame(data=zip([f'${mean:.3f} \\pm {std:.3f}$' for mean, std in t_blocked_calls], [f'${mean:.3f} \\pm {std:.3f}$' for mean, std in t_dropped_calls]), index=range(10), columns=['blocked calls (mean $\\pm$ std)', 'dropped calls (mean $\\pm$ std)'])
    df.index.rename('reserved channels', inplace=True)
    df.to_csv('assets/results/results.csv', sep='&')