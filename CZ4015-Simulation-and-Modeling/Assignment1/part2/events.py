class CallInitiationEvent:

    def __init__(self, time):
        self.time = time


class CallHandoverEvent:

    def __init__(self, time, call):
        self.time = time
        self.call = call


class CallTerminationEvent:

    def __init__(self, time, call):
        self.time = time
        self.call = call