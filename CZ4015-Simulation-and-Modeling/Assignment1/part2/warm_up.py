from config import N_STATIONS, N_CHANNELS
import matplotlib.pyplot as plt
from simulation import Simulator


FONTDICT = {
    "family": "serif", 
    "color": "darkred", 
    "weight": "normal", 
    "size": 12
}


class ModifiedSimulator(Simulator):

    def __init__(self, N_STATIONS, N_CHANNELS, reserved):

        super(ModifiedSimulator, self).__init__(N_STATIONS, N_CHANNELS, reserved)
        self.perc_blocked_calls = list()
        self.perc_dropped_calls = list()

    def handle_call_termination_event(self, event):
        
        self.clock, call = event.time, event.call
        self.stations[call.curr_station].free_channel(call.channel)
        self.finished_calls += 1

        self.update_results()

    def update_results(self):

        self.perc_blocked_calls.append(100*self.blocked_calls/self.total_calls)
        self.perc_dropped_calls.append(100*self.dropped_calls/self.total_calls)


for reserved in range(10):
    
    s = ModifiedSimulator(N_STATIONS, N_CHANNELS, reserved)
    s.run(termination_cond=20_000)

    plt.figure(figsize=(6, 4))
    plt.plot(range(1, len(s.perc_blocked_calls)+1), s.perc_blocked_calls, color='blue', label='blocked calls')
    plt.plot(range(1, len(s.perc_dropped_calls)+1), s.perc_dropped_calls, color='green', label='dropped calls')
    plt.title(f'{reserved} reserved channels', fontdict=FONTDICT, pad=12)
    plt.xlabel('number of calls finished', fontdict=FONTDICT, labelpad=12)
    plt.ylabel('percentages',  fontdict=FONTDICT, labelpad=12)
    plt.legend(loc='center left', bbox_to_anchor=(1, 0.5))
    plt.grid()
    plt.savefig(f'assets/warm-up/reserved-{reserved}.png', bbox_inches='tight')