package net.trajano.servicebus.master;

public interface ServiceBus {
	void tell(Object message);
}
