package mycorp.examples.timeservice.sr.host;

import mycorp.examples.timeservice.ITimeService;

public class TimeServiceImpl implements ITimeService {

	/**
	 * Implementation of my time service.
	 */
	public Long getCurrentTime() {
		// Print out to host std out that a call to this service was received.
		System.out
				.println("TimeServiceImpl.  Received call to getCurrentTime()");
		return new Long(System.currentTimeMillis());
	}

}
